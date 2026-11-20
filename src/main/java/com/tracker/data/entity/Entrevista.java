package com.tracker.data.entity;

import java.time.LocalDate;

/**
 * Entrevista asociada a una oferta.
 *
 * @param id         identificador único de la entrevista.
 * @param idOferta   identificador de la oferta asociada.
 * @param fecha      fecha en que se celebra la entrevista.
 * @param tipo       tipo de entrevista.
 * @param notas      notas sobre la entrevista.
 * @param resultado  resultado de la entrevista.
 */
public record Entrevista(Integer id, Integer idOferta, LocalDate fecha, String tipo, String notas, String resultado) {}
