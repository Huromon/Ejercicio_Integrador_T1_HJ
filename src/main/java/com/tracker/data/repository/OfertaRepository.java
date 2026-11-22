package com.tracker.data.repository;

import com.tracker.data.entity.Estado;
import com.tracker.data.entity.FiltroOferta;
import com.tracker.data.entity.Oferta;

import java.util.List;

/**
 * Repositorio de ofertas en la base de datos.
 */
public interface OfertaRepository extends Repositorio<Oferta> {

    /**
     * Busca ofertas que cumplen una combinación de criterios.
     *
     * @param filtro criterios de filtrado.
     * @return lista de ofertas que cumplen los criterios.
     */
    List<Oferta> filtrar(FiltroOferta filtro);

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
}
