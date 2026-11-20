package com.tracker.data.entity;

import java.time.LocalDate;

/**
 * Entrevista con los títulos de la oferta y la empresa, resultado de un JOIN de tres tablas.
 *
 * @param id           identificador de la entrevista.
 * @param idOferta     identificador de la oferta asociada.
 * @param tituloOferta título de la oferta asociada.
 * @param empresa      nombre de la empresa asociada a la oferta.
 * @param fecha        fecha de la entrevista.
 * @param tipo         tipo de entrevista.
 * @param notas        notas sobre la entrevista.
 * @param resultado    resultado de la entrevista.
 */
public record EntrevistaDetalle(Integer id, Integer idOferta, String tituloOferta, String empresa,
                                LocalDate fecha, String tipo, String notas, String resultado) {}
