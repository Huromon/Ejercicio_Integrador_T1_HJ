package com.tracker.data.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record Oferta(
        Integer id,
        String titulo,
        String empresa,
        Estado estado,
        LocalDate fechaPostulacion,
        String enlace,
        String descripcion,
        Double salario
) {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public String toString() {
        return "ID: " + id +
                " | Título: " + titulo +
                " | Empresa: " + empresa +
                " | Estado: " + estado +
                " | Fecha: " + (fechaPostulacion != null ? fechaPostulacion.format(FORMATO) : "") +
                (enlace != null && !enlace.isBlank() ? " | Enlace: " + enlace : "") +
                (descripcion != null && !descripcion.isBlank() ? " | Descripción: " + descripcion : "") +
                (salario != null && salario != 0.0 ? " | Salario: " + salario : "");
    }
}
