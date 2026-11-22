package com.tracker.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Oferta de empleo a la que se ha presentado una candidatura.
 * <p>
 * Sus campos son {@code id} (identificador generado), {@code idEmpresa}
 * (identificador de la empresa como campo simple, sin relación JPA),
 * {@code titulo}, {@code descripcion}, {@code estado} (guardado como cadena),
 * {@code fechaPostulacion}, {@code enlace}, {@code salario} y {@code notas}.
 * </p>
 */
@Entity
@Table(name = "OFERTA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "id_empresa")
    private Integer idEmpresa;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Column(name = "fechaPostulacion")
    private LocalDate fechaPostulacion;

    private String enlace;

    private BigDecimal salario;

    @Column(columnDefinition = "TEXT")
    private String notas;
}
