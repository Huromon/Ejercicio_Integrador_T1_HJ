package com.tracker.data.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Oferta(Integer id, Integer idEmpresa, String titulo, String descripcion, Estado estado, 
                     LocalDate fechaPostulacion, String enlace, BigDecimal salario, String notas) {}
