package com.tracker.business;

import com.tracker.data.entity.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OfertaService {
    Oferta crear(Integer idEmpresa, String titulo, String descripcion, Estado estado, LocalDate fechaPostulacion, String enlace, BigDecimal salario, String notas);
    List<OfertaDetalle> filtrar(FiltroOferta filtro);
    List<Oferta> listarTodas();
    Optional<Oferta> buscarPorId(Integer id);
    void eliminarConDependencias(Integer id);
}
