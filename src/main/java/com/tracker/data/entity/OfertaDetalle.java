package com.tracker.data.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Oferta con el nombre de su empresa, resultado de una consulta con JOIN.
 * <p>
 * Reúne el identificador y el título de la oferta, el nombre de la empresa, el
 * estado, la fecha de postulación, el enlace, la descripción, el salario y las
 * notas en un único objeto de transporte.
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OfertaDetalle {

    private Integer id;

    private String titulo;

    private String empresa;

    private Estado estado;

    private LocalDate fechaPostulacion;

    private String enlace;

    private String descripcion;

    private BigDecimal salario;

    private String notas;
}
