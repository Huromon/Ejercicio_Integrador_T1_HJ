package com.tracker.data.repository;

import com.tracker.data.entity.Entrevista;
import com.tracker.data.entity.EntrevistaDetalle;

import java.sql.Connection;
import java.util.List;

public interface EntrevistaRepository extends Repositorio<Entrevista> {
    List<EntrevistaDetalle> buscarPorOferta(Integer idOferta);
    Entrevista actualizar(Entrevista entrevista);

    /** Versión transaccional: usa la Connection del TransaccionManager. */
    Entrevista guardar(Entrevista entrevista, Connection conn);

    /** Versión transaccional: borra todas las entrevistas de una oferta. */
    void eliminarPorOferta(Integer idOferta, Connection conn);
}
