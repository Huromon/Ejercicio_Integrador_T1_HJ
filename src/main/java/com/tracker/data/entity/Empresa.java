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
public record Empresa(Integer id, String nombre, String sector, String web, LocalDate fechaRegistro) {}
