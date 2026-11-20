package com.tracker.data.repository;

import com.tracker.data.entity.Estado;
import com.tracker.data.entity.FiltroOferta;
import com.tracker.data.entity.Oferta;
import com.tracker.data.entity.OfertaDetalle;

import java.sql.Connection;
import java.util.List;

/**
 * Repositorio de ofertas en la base de datos.
 */
public interface OfertaRepository extends Repositorio<Oferta> {

    /**
     * Busca ofertas que cumplen una combinación de criterios, con datos de su empresa.
     *
     * @param filtro criterios de filtrado.
     * @return lista de ofertas con el nombre de la empresa.
     */
    List<OfertaDetalle> filtrar(FiltroOferta filtro);

    /**
     * Elimina una oferta y las entrevistas asociadas.
     *
     * @param id identificador de la oferta a eliminar.
     */
    void eliminarConDependencias(Integer id);

    /**
     * Cambia el estado de una candidatura.
     *
     * @param id     identificador de la oferta.
     * @param estado nuevo estado de la candidatura.
     */
    void cambiarEstado(Integer id, Estado estado);

    /**
     * Cambia el estado de una candidatura usando una conexión existente.
     *
     * @param id     identificador de la oferta.
     * @param estado nuevo estado de la candidatura.
     * @param conn   conexión abierta por el gestor de transacciones.
     */
    void cambiarEstado(Integer id, Estado estado, Connection conn);

    /**
     * Borra una oferta usando una conexión existente.
     *
     * @param id   identificador de la oferta a eliminar.
     * @param conn conexión abierta por el gestor de transacciones.
     */
    void eliminar(Integer id, Connection conn);
}
