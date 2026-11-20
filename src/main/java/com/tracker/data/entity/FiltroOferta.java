package com.tracker.data.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Criterios de filtrado de ofertas. Los campos nulos se ignoran en la consulta.
 *
 * @param empresa    texto a buscar en el nombre de la empresa.
 * @param estado     estado de la candidatura.
 * @param salarioMin salario mínimo de la oferta.
 * @param salarioMax salario máximo de la oferta.
 * @param fechaDesde fecha de postulación mínima.
 * @param fechaHasta fecha de postulación máxima.
 * @param texto      texto a buscar en el título o la descripción.
 */
public record FiltroOferta(String empresa, Estado estado, BigDecimal salarioMin, BigDecimal salarioMax,
                           LocalDate fechaDesde, LocalDate fechaHasta, String texto) {}
