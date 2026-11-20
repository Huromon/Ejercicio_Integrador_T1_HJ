package com.tracker.business;

import com.tracker.data.entity.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicios de negocio para la gestión de ofertas y candidaturas.
 */
public interface OfertaService {

    /**
     * Crea una nueva oferta asociada a una empresa existente.
     *
     * @param idEmpresa         identificador de la empresa asociada.
     * @param titulo            título de la oferta, obligatorio.
     * @param descripcion       descripción de la oferta.
     * @param estado            estado inicial de la candidatura.
     * @param fechaPostulacion  fecha de postulación.
     * @param enlace            enlace a la oferta.
     * @param salario           salario ofrecido.
     * @param notas             notas sobre la oferta.
     * @return la oferta creada con su identificador asignado.
     */
    Oferta crear(Integer idEmpresa, String titulo, String descripcion, Estado estado,
                 LocalDate fechaPostulacion, String enlace, BigDecimal salario, String notas);

    /**
     * Filtra ofertas por cualquier combinación de empresa, estado, salario, fechas y texto.
     *
     * @param filtro criterios de filtrado.
     * @return lista de ofertas con datos de la empresa que cumplen los criterios.
     */
    List<OfertaDetalle> filtrar(FiltroOferta filtro);

    /**
     * Devuelve todas las ofertas registradas.
     *
     * @return lista de ofertas.
     */
    List<Oferta> listarTodas();

    /**
     * Busca una oferta por su identificador.
     *
     * @param id identificador de la oferta.
     * @return un {@link Optional} con la oferta si existe, o vacío en caso contrario.
     */
    Optional<Oferta> buscarPorId(Integer id);

    /**
     * Elimina una oferta y todas sus entrevistas en una sola transacción.
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
