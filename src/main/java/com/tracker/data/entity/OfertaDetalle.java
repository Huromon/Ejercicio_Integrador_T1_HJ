package com.tracker.data.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OfertaDetalle(Integer id, String titulo, String empresa, Estado estado, LocalDate fechaPostulacion, 
                            String enlace, String descripcion, BigDecimal salario, String notas) {}
