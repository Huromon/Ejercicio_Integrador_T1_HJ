package com.tracker.data.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Oferta de empleo a la que se ha presentado una candidatura.
 *
 * @param id               identificador único de la oferta.
 * @param idEmpresa        identificador de la empresa asociada.
 * @param titulo           título de la oferta.
 * @param descripcion      descripción de la oferta.
 * @param estado           estado actual de la candidatura.
 * @param fechaPostulacion fecha en que se produjo la postulación.
 * @param enlace           enlace a la oferta publicada.
 * @param salario          salario ofrecido.
 * @param notas            notas sobre la oferta.
 */
public record Oferta(Integer id, Integer idEmpresa, String titulo, String descripcion, Estado estado,
                     LocalDate fechaPostulacion, String enlace, BigDecimal salario, String notas) {}
