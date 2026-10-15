package org.example;

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
}
