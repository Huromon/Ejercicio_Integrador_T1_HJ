package com.tracker.data.candidatura;

import com.tracker.data.entity.Estado;

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

        return id + " - " + titulo+ " - " + empresa + " - "
                + estado + " - " + fechaPostulacion + " - "
                + ((enlace=="")?(enlace + " - "):"") + descripcion + " - " + salario + "€" ;
    }
}
