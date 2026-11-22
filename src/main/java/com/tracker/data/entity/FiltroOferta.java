package com.tracker.data.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Criterios de filtrado de ofertas. Los campos nulos se ignoran en la consulta.
 * <p>
 * Permite buscar por nombre de empresa, estado, salario mínimo y máximo, rango
 * de fechas de postulación y texto libre en el título o la descripción.
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FiltroOferta {

    private String empresa;

    private Estado estado;

    private BigDecimal salarioMin;

    private BigDecimal salarioMax;

    private LocalDate fechaDesde;

    private LocalDate fechaHasta;

    private String texto;
}
