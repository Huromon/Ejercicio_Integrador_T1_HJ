package com.tracker.data.repository;

import com.tracker.data.entity.Oferta;

import java.util.List;

public interface OfertaRepository {
    List<Oferta> listarOfertas();
    Oferta buscarPorId(Integer id);
    void guardarOferta(Oferta oferta);
    boolean eliminarPorId(Integer id);
    void guardarTodas(List<Oferta> ofertas);
}
