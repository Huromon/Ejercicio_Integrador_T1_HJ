package com.tracker.data.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FiltroOferta(String empresa, Estado estado, BigDecimal salarioMin, BigDecimal salarioMax, 
                           LocalDate fechaDesde, LocalDate fechaHasta, String texto) {}
