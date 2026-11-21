package com.tracker.data.entity;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Oferta de empleo a la que se ha presentado una candidatura.
 *
 * @param id               identificador único de la oferta.
 * @param idEmpresa        identificador de la empresa asociada.
 * @param titulo           título de la oferta.
 * @param descripcion      descripción de la oferta.
 * @param estado           estado actual de la candidatura.
 * @param fechaPostulacion fecha en que se produjo la postulación.
 * @param enlace           enlace a la oferta publicada.
 * @param salario          salario ofrecido.
 * @param notas            notas sobre la oferta.
 */
/**
public record Oferta(Integer id, Integer idEmpresa, String titulo, String descripcion, Estado estado,
                     LocalDate fechaPostulacion, String enlace, BigDecimal salario, String notas) {}
*/


@Entity
@Table(name = "OFERTA") // Cambia "ofertas" si tu tabla en MySQL se llama distinto

@NoArgsConstructor  // Constructor vacío obligatorio para Hibernate
@AllArgsConstructor // Constructor completo (como tu antiguo record)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Evita bucles y fallos con entidades
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "id_empresa")
    private Integer idEmpresa;

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public LocalDate getFechaPostulacion() {
        return fechaPostulacion;
    }

    public void setFechaPostulacion(LocalDate fechaPostulacion) {
        this.fechaPostulacion = fechaPostulacion;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
