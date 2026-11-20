package com.tracker.data.repository;

import com.tracker.data.entity.Entrevista;
import com.tracker.data.entity.EntrevistaDetalle;
import java.util.List;

public interface EntrevistaRepository extends Repositorio<Entrevista> {
    List<EntrevistaDetalle> buscarPorOferta(Integer idOferta);
}
