package com.tracker.business;

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

public class OfertaServiceImpl implements OfertaService {

    private final OfertaRepository ofertaRepository;
    private final EmpresaRepository empresaRepository;
    private final EntrevistaRepository entrevistaRepository;
    private final TransaccionManager transaccionManager;

    public OfertaServiceImpl(OfertaRepository ofertaRepository, 
                             EmpresaRepository empresaRepository, 
                             EntrevistaRepository entrevistaRepository, 
                             TransaccionManager transaccionManager) {
        this.ofertaRepository = ofertaRepository;
        this.empresaRepository = empresaRepository;
        this.entrevistaRepository = entrevistaRepository;
        this.transaccionManager = transaccionManager;
    }

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

    @Override
    public List<OfertaDetalle> filtrar(FiltroOferta filtro) {
        return ofertaRepository.filtrar(filtro != null ? filtro : new FiltroOferta(null, null, null, null, null, null, null));
    }

    @Override
    public List<Oferta> listarTodas() {
        return ofertaRepository.buscarTodos();
    }

    @Override
    public Optional<Oferta> buscarPorId(Integer id) {
        if (id == null) {
            throw new ValidacionException("El ID de la oferta no puede ser nulo.");
        }
        return ofertaRepository.buscarPorId(id);
    }

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
