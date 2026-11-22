package com.tracker.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entrevista asociada a una oferta de empleo.
 * <p>
 * Contiene el identificador de la oferta como campo simple, la fecha en que se
 * celebra, el tipo de entrevista, las notas y el resultado obtenido.
 * </p>
 */
@Entity
@Table(name = "ENTREVISTA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Entrevista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "id_oferta")
    private Integer idOferta;

    private LocalDate fecha;

    private String tipo;

    private String notas;

    private String resultado;
}
