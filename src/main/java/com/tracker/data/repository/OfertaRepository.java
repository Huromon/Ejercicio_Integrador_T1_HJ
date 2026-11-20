package com.tracker.data.repository;

import com.tracker.data.entity.Estado;
import com.tracker.data.entity.FiltroOferta;
import com.tracker.data.entity.Oferta;
import com.tracker.data.entity.OfertaDetalle;

import java.util.List;

public interface OfertaRepository extends Repositorio<Oferta> {
    List<OfertaDetalle> filtrar(FiltroOferta filtro);
    void eliminarConDependencias(Integer id);
    void cambiarEstado(Integer id, Estado estado);
}
