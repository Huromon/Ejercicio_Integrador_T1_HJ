package com.tracker.data.entity;

import java.time.LocalDate;

/**
 * Empresa a la que pertenece una candidatura.
 *
 * @param id            identificador único de la empresa.
 * @param nombre        nombre de la empresa.
 * @param sector        sector al que pertenece.
 * @param web           sitio web de la empresa.
 * @param fechaRegistro fecha en que se dio de alta la empresa.

*/
/**
public record Empresa(Integer id, String nombre, String sector, String web, LocalDate fechaRegistro) {}
*/

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "EMPRESA") // Cambia "empresas" si tu tabla en MySQL se llama distinto
@Getter
@Setter
@NoArgsConstructor  // Constructor vacío obligatorio para Hibernate
@AllArgsConstructor // Constructor completo (como tu antiguo record)
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
