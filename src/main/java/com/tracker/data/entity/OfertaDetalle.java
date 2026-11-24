package com.tracker.data.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Oferta con el nombre de su empresa, resultado de una consulta con JOIN.
 *
 * @param id               identificador de la oferta.
 * @param titulo           título de la oferta.
 * @param empresa          nombre de la empresa asociada.
 * @param estado           estado actual de la candidatura.
 * @param fechaPostulacion fecha de la postulación.
 * @param enlace           enlace a la oferta publicada.
 * @param descripcion      descripción de la oferta.
 * @param salario          salario ofrecido.
 * @param notas            notas sobre la oferta.
 */
public record OfertaDetalle(Integer id, String titulo, String empresa, Estado estado, LocalDate fechaPostulacion,
                            String enlace, String descripcion, BigDecimal salario, String notas) {}
