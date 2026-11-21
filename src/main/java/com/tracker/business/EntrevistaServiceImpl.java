package com.tracker.business;

import com.tracker.common.AppContext;
import com.tracker.data.TransaccionManager;
import com.tracker.data.entity.Entrevista;
import com.tracker.data.entity.EntrevistaDetalle;
import com.tracker.data.entity.Estado;
import com.tracker.data.repository.EntrevistaRepository;
import com.tracker.data.repository.OfertaRepository;
import com.tracker.exception.NoEncontradoException;
import com.tracker.exception.ValidacionException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de los servicios de negocio de entrevistas.
 */
public class EntrevistaServiceImpl implements EntrevistaService {

    private final EntrevistaRepository entrevistaRepository;
    private final OfertaRepository ofertaRepository;
    private final TransaccionManager transaccionManager;

    /**
     * Crea el servicio con los repositorios y el gestor de transacciones que debe utilizar.
     *
     * @param entrevistaRepository repositorio de entrevistas.
     * @param ofertaRepository     repositorio de ofertas.
     * @param transaccionManager   gestor de transacciones.
     */
    public EntrevistaServiceImpl(EntrevistaRepository entrevistaRepository,
                                 OfertaRepository ofertaRepository,
                                 TransaccionManager transaccionManager) {
        this.entrevistaRepository = entrevistaRepository;
        this.ofertaRepository = ofertaRepository;
        this.transaccionManager = transaccionManager;
        AppContext.getInstance().put("entrevistaService", this );
    }

    /**
     * Inserta la entrevista y cambia el estado de la oferta en la misma transacción.
     *
     * @param idOferta  identificador de la oferta a la que pertenece la entrevista.
     * @param fecha     fecha de la entrevista, obligatoria.
     * @param tipo      tipo de entrevista.
     * @param notas     notas sobre la entrevista.
     * @param resultado resultado de la entrevista.
     * @return la entrevista registrada con su identificador asignado.
     */
    @Override
    public Entrevista registrar(Integer idOferta, LocalDate fecha, String tipo, String notas, String resultado) {
        if (idOferta == null || ofertaRepository.buscarPorId(idOferta).isEmpty()) {
            throw new ValidacionException("La entrevista debe asociarse a una oferta existente.");
        }
        if (fecha == null) {
            throw new ValidacionException("La fecha de la entrevista es obligatoria.");
        }
        return transaccionManager.ejecutarEnTransaccion(conn -> {
            Entrevista nueva = new Entrevista(null, idOferta, fecha, tipo, notas, resultado);
            Entrevista guardada = entrevistaRepository.guardar(nueva, conn);

            ofertaRepository.cambiarEstado(idOferta, Estado.ENTREVISTA, conn);
            return guardada;
        });
    }

    /**
     * Valida los datos y actualiza una entrevista, conservando su identificador y su oferta.
     *
     * @param id        identificador de la entrevista a modificar.
     * @param fecha     nueva fecha de la entrevista, obligatoria.
     * @param tipo      nuevo tipo de entrevista.
     * @param notas     nuevas notas sobre la entrevista.
     * @param resultado nuevo resultado de la entrevista.
     * @return la entrevista con los datos actualizados.
     */
    @Override
    public Entrevista actualizar(Integer id, LocalDate fecha, String tipo, String notas, String resultado) {
        Entrevista existente = buscarPorId(id).orElseThrow(() ->
                new NoEncontradoException("No se encuentra la entrevista con ID: " + id));
        if (fecha == null) {
            throw new ValidacionException("La fecha de la entrevista es obligatoria.");
        }
        return entrevistaRepository.actualizar(new Entrevista(
                existente.id(), existente.idOferta(), fecha, tipo, notas, resultado));
    }

    /**
     * Devuelve todas las entrevistas registradas.
     *
     * @return lista de entrevistas.
     */
    @Override
    public List<Entrevista> listarTodas() {
        return entrevistaRepository.buscarTodos();
    }

    /**
     * Devuelve las entrevistas de una oferta junto con datos de la oferta y la empresa.
     *
     * @param idOferta identificador de la oferta.
     * @return lista de detalles de entrevistas.
     */
    @Override
    public List<EntrevistaDetalle> listarPorOferta(Integer idOferta) {
        if (idOferta == null) {
            throw new ValidacionException("El ID de la oferta no puede ser nulo.");
        }
        return entrevistaRepository.buscarPorOferta(idOferta);
    }

    /**
     * Busca una entrevista por su identificador.
     *
     * @param id identificador de la entrevista.
     * @return un {@link Optional} con la entrevista si existe, o vacío en caso contrario.
     */
    @Override
    public Optional<Entrevista> buscarPorId(Integer id) {
        if (id == null) {
            throw new ValidacionException("El ID de la entrevista no puede ser nulo.");
        }
        return entrevistaRepository.buscarPorId(id);
    }

    /**
     * Comprueba que la entrevista existe y la elimina.
     *
     * @param id identificador de la entrevista a eliminar.
     */
    @Override
    public void eliminar(Integer id) {
        if (id == null || entrevistaRepository.buscarPorId(id).isEmpty()) {
            throw new NoEncontradoException("No se encuentra la entrevista con ID: " + id);
        }
        entrevistaRepository.eliminar(id);
    }
}
