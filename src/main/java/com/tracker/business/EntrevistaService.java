package com.tracker.business;

import com.tracker.data.entity.Entrevista;
import com.tracker.data.entity.EntrevistaDetalle;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EntrevistaService {
    Entrevista registrar(Integer idOferta, LocalDate fecha, String tipo, String notas, String resultado);
    Entrevista actualizar(Integer id, LocalDate fecha, String tipo, String notas, String resultado);
    List<Entrevista> listarTodas();
    List<EntrevistaDetalle> listarPorOferta(Integer idOferta);
    Optional<Entrevista> buscarPorId(Integer id);
    void eliminar(Integer id);
}
