package com.tracker.data.repository;

import com.tracker.data.entity.Entrevista;
import com.tracker.data.entity.EntrevistaDetalle;

import java.sql.Connection;
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

    /**
     * Inserta una entrevista usando una conexión existente.
     *
     * @param entrevista entrevista a guardar.
     * @param conn       conexión abierta por el gestor de transacciones.
     * @return la entrevista guardada con su identificador asignado.
     */
    Entrevista guardar(Entrevista entrevista, Connection conn);

    /**
     * Borra todas las entrevistas asociadas a una oferta usando una conexión existente.
     *
     * @param idOferta identificador de la oferta.
     * @param conn     conexión abierta por el gestor de transacciones.
     */
    void eliminarPorOferta(Integer idOferta, Connection conn);
}
