package com.tracker.data.repository;

import com.tracker.data.entity.Entrevista;
import com.tracker.data.entity.EntrevistaDetalle;

import java.util.List;

/**
 * Repositorio de entrevistas en la base de datos.
 */
public interface EntrevistaRepository extends Repositorio<Entrevista> {

    /**
     * Busca las entrevistas de una oferta con datos de la oferta y la empresa.
     *
     * @param idOferta identificador de la oferta.
     * @return lista de detalles de entrevistas.
     */
    List<EntrevistaDetalle> buscarPorOferta(Integer idOferta);

    /**
     * Actualiza los datos de una entrevista existente.
     *
     * @param entrevista entrevista con los datos actualizados y su identificador.
     * @return la entrevista actualizada.
     */
    Entrevista actualizar(Entrevista entrevista);
}
