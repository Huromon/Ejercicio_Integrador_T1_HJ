package com.tracker.data.entity;

import lombok.*;

import java.time.LocalDate;

/**
 * Entrevista con los datos de la oferta y la empresa, resultado de un JOIN de tres tablas.
 * <p>
 * Incluye el identificador de la entrevista y de la oferta, el título de la
 * oferta, el nombre de la empresa, la fecha, el tipo, las notas y el resultado.
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EntrevistaDetalle {

    private Integer id;

    private Integer idOferta;

    private String tituloOferta;

    private String empresa;

    private LocalDate fecha;

    private String tipo;

    private String notas;

    private String resultado;
}
