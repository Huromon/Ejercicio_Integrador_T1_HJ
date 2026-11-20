package com.tracker.data.entity;

import java.time.LocalDate;

public record Empresa(Integer id, String nombre, String sector, String web, LocalDate fechaRegistro) {}
