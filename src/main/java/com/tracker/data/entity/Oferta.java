package com.tracker.data.entity;

import java.time.LocalDate;

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


    @Override
    public String toString() {
        return "Oferta{" +
                "id = " + id +
                ", titulo = '" + titulo + '\'' +
                ", empresa = '" + empresa + '\'' +
                ", estado = " + estado +
                ", fechaPostulacion=" + fechaPostulacion +
                ((enlace.equals(""))?(", enlace='" + enlace + '\''):"") +
                ((descripcion.equals(""))?(", descripcion = '" + descripcion + '\''):"") +
                ((salario == 0.0)?(", salario = '" + salario ):"") +
                '}';
    }
}
