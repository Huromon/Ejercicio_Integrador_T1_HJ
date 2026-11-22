package com.tracker.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Empresa a la que pertenece una candidatura.
 * <p>
 * Sus campos son {@code id} (identificador generado), {@code nombre}
 * (obligatorio y único), {@code sector}, {@code web} y
 * {@code fechaRegistro}.
 * </p>
 */
@Entity
@Table(name = "EMPRESA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    private String sector;

    private String web;

    @Column(name = "fechaRegistro")
    private LocalDate fechaRegistro;
}
