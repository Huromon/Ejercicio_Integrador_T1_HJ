package com.tracker.data.entity;

import java.time.LocalDate;

public record Entrevista(Integer id, Integer idOferta, LocalDate fecha, String tipo, String notas, String resultado) {}
