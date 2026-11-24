package com.tracker.data.entity;

import lombok.*;

import java.time.LocalDate;

/**
 * Empresa a la que pertenece una candidatura.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Empresa {

    @EqualsAndHashCode.Include
    private Integer id;

    private String nombre;

    private String sector;

    private String web;

    private LocalDate fechaRegistro;
}
