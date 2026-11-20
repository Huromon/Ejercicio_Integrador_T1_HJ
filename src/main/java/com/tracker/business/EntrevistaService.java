package com.tracker.business;

import com.tracker.data.entity.Entrevista;
import com.tracker.data.entity.EntrevistaDetalle;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicios de negocio para la gestión de entrevistas.
 */
public interface EntrevistaService {

    /**
     * Registra una entrevista y actualiza el estado de la oferta asociada.
     *
     * @param idOferta  identificador de la oferta a la que pertenece la entrevista.
     * @param fecha     fecha de la entrevista, obligatoria.
     * @param tipo      tipo de entrevista.
     * @param notas     notas sobre la entrevista.
     * @param resultado resultado de la entrevista.
     * @return la entrevista registrada con su identificador asignado.
     */
    Entrevista registrar(Integer idOferta, LocalDate fecha, String tipo, String notas, String resultado);

    /**
     * Modifica los datos de una entrevista existente.
     *
     * @param id        identificador de la entrevista a modificar.
     * @param fecha     nueva fecha de la entrevista, obligatoria.
     * @param tipo      nuevo tipo de entrevista.
     * @param notas     nuevas notas sobre la entrevista.
     * @param resultado nuevo resultado de la entrevista.
     * @return la entrevista con los datos actualizados.
     */
    Entrevista actualizar(Integer id, LocalDate fecha, String tipo, String notas, String resultado);

    /**
     * Devuelve todas las entrevistas registradas.
     *
     * @return lista de entrevistas.
     */
    List<Entrevista> listarTodas();

    /**
     * Devuelve las entrevistas de una oferta junto con datos de la oferta y la empresa.
     *
     * @param idOferta identificador de la oferta.
     * @return lista de detalles de entrevistas.
     */
    List<EntrevistaDetalle> listarPorOferta(Integer idOferta);

    /**
     * Busca una entrevista por su identificador.
     *
     * @param id identificador de la entrevista.
     * @return un {@link Optional} con la entrevista si existe, o vacío en caso contrario.
     */
    Optional<Entrevista> buscarPorId(Integer id);

    /**
     * Elimina una entrevista por su identificador.
     *
     * @param id identificador de la entrevista a eliminar.
     */
    void eliminar(Integer id);
}
