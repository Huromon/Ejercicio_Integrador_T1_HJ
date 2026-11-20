package com.tracker.data.entity;

import java.time.LocalDate;

public record EntrevistaDetalle(Integer id, Integer idOferta, String tituloOferta, String empresa, 
                                LocalDate fecha, String tipo, String notas, String resultado) {}
