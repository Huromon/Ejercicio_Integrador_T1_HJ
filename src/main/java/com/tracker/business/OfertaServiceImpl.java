package com.tracker.business;

import com.tracker.common.AppContext;
import com.tracker.common.ContextKeys;
import com.tracker.data.TransaccionManager;
import com.tracker.data.entity.*;
import com.tracker.data.repository.EmpresaRepository;
import com.tracker.data.repository.EntrevistaRepository;
import com.tracker.data.repository.OfertaRepository;
import com.tracker.exception.NoEncontradoException;
import com.tracker.exception.ValidacionException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de los servicios de negocio de ofertas.
 */
public class OfertaServiceImpl implements OfertaService {

    private final OfertaRepository ofertaRepository;
    private final EmpresaRepository empresaRepository;
    private final EntrevistaRepository entrevistaRepository;
    private final TransaccionManager transaccionManager;

    /**
     * Crea el servicio con los repositorios y el gestor de transacciones que debe utilizar.
     *
     * @param ofertaRepository      repositorio de ofertas.
     * @param empresaRepository     repositorio de empresas.
     * @param entrevistaRepository  repositorio de entrevistas.
     * @param transaccionManager    gestor de transacciones.
     */
    public OfertaServiceImpl(OfertaRepository ofertaRepository,
                             EmpresaRepository empresaRepository,
                             EntrevistaRepository entrevistaRepository,
                             TransaccionManager transaccionManager) {
        this.ofertaRepository = ofertaRepository;
        this.empresaRepository = empresaRepository;
        this.entrevistaRepository = entrevistaRepository;
        this.transaccionManager = transaccionManager;
        AppContext.getInstance().put(ContextKeys.OFERTA_SERVICE, this);
    }

    /**
     * Valida la empresa y el título, y crea la oferta con valores por defecto
     * para el estado y la fecha cuando no se indican.
     *
     * @param idEmpresa        identificador de la empresa asociada.
     * @param titulo           título de la oferta, obligatorio.
     * @param descripcion      descripción de la oferta.
     * @param estado           estado inicial de la candidatura.
     * @param fechaPostulacion fecha de postulación.
     * @param enlace           enlace a la oferta.
     * @param salario          salario ofrecido.
     * @param notas            notas sobre la oferta.
     * @return la oferta creada con su identificador asignado.
     */
    @Override
    public Oferta crear(Integer idEmpresa, String titulo, String descripcion, Estado estado,
                        LocalDate fechaPostulacion, String enlace, BigDecimal salario, String notas) {
        if (idEmpresa == null || empresaRepository.buscarPorId(idEmpresa).isEmpty()) {
            throw new ValidacionException("La oferta debe estar asociada a una empresa existente.");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new ValidacionException("El título de la oferta es obligatorio.");
        }
        Estado estadoFinal = estado != null ? estado : Estado.PENDIENTE;
        LocalDate fechaFinal = fechaPostulacion != null ? fechaPostulacion : LocalDate.now();

        Oferta oferta = new Oferta(null, idEmpresa, titulo.trim(), descripcion, estadoFinal, fechaFinal, enlace, salario, notas);
        return ofertaRepository.guardar(oferta);
    }

    /**
     * Filtra ofertas por cualquier combinación de criterios; un filtro nulo devuelve todas.
     *
     * @param filtro criterios de filtrado.
     * @return lista de ofertas que cumplen los criterios.
     */
    @Override
    public List<Oferta> filtrar(FiltroOferta filtro) {
        return ofertaRepository.filtrar(filtro != null ? filtro : new FiltroOferta(null, null, null, null, null, null, null));
    }

    /**
     * Devuelve todas las ofertas registradas.
     *
     * @return lista de ofertas.
     */
    @Override
    public List<Oferta> listarTodas() {
        return ofertaRepository.buscarTodos();
    }
    public OfertaRepository getOfertaRepository() {
        return this.ofertaRepository;
    }
    /**
     * Busca una oferta por su identificador.
     *
     * @param id identificador de la oferta.
     * @return un {@link Optional} con la oferta si existe, o vacío en caso contrario.
     */
    @Override
    public Optional<Oferta> buscarPorId(Integer id) {
        if (id == null) {
            throw new ValidacionException("El ID de la oferta no puede ser nulo.");
        }
        return ofertaRepository.buscarPorId(id);
    }

    /**
     * Borra las entrevistas de la oferta y la oferta usando la misma conexión transaccional.
     *
     * @param id identificador de la oferta a eliminar.
     */
    @Override
    public void eliminarConDependencias(Integer id) {
        if (id == null || ofertaRepository.buscarPorId(id).isEmpty()) {
            throw new NoEncontradoException("No se encuentra la oferta con ID: " + id);
        }
        transaccionManager.ejecutarEnTransaccion(conn -> {
            entrevistaRepository.eliminarPorOferta(id, conn);
            ofertaRepository.eliminar(id, conn);
            return null;
        });
    }

    /**
     * Comprueba que la oferta existe y le asigna un nuevo estado.
     *
     * @param id     identificador de la oferta.
     * @param estado nuevo estado de la candidatura.
     */
    @Override
    public void cambiarEstado(Integer id, Estado estado) {
        if (id == null || estado == null) {
            throw new ValidacionException("El ID y el estado de la oferta son obligatorios.");
        }
        if (ofertaRepository.buscarPorId(id).isEmpty()) {
            throw new NoEncontradoException("No se encuentra la oferta con ID: " + id);
        }
        ofertaRepository.cambiarEstado(id, estado);
    }
}
