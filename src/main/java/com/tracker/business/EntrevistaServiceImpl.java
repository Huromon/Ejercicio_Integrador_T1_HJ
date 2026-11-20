package com.tracker.business;

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

public class EntrevistaServiceImpl implements EntrevistaService {

    private final EntrevistaRepository entrevistaRepository;
    private final OfertaRepository ofertaRepository;
    private final TransaccionManager transaccionManager;

    public EntrevistaServiceImpl(EntrevistaRepository entrevistaRepository,
                                 OfertaRepository ofertaRepository,
                                 TransaccionManager transaccionManager) {
        this.entrevistaRepository = entrevistaRepository;
        this.ofertaRepository = ofertaRepository;
        this.transaccionManager = transaccionManager;
    }

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

    @Override
    public List<Entrevista> listarTodas() {
        return entrevistaRepository.buscarTodos();
    }

    @Override
    public List<EntrevistaDetalle> listarPorOferta(Integer idOferta) {
        if (idOferta == null) {
            throw new ValidacionException("El ID de la oferta no puede ser nulo.");
        }
        return entrevistaRepository.buscarPorOferta(idOferta);
    }

    @Override
    public Optional<Entrevista> buscarPorId(Integer id) {
        if (id == null) {
            throw new ValidacionException("El ID de la entrevista no puede ser nulo.");
        }
        return entrevistaRepository.buscarPorId(id);
    }

    @Override
    public void eliminar(Integer id) {
        if (id == null || entrevistaRepository.buscarPorId(id).isEmpty()) {
            throw new NoEncontradoException("No se encuentra la entrevista con ID: " + id);
        }
        entrevistaRepository.eliminar(id);
    }
}
