package com.tracker.data.repository;

import com.tracker.data.entity.Empresa;
import java.util.Optional;

/**
 * Repositorio de empresas en la base de datos.
 */
public interface EmpresaRepository extends Repositorio<Empresa> {

    /**
     * Busca una empresa por su nombre exacto.
     *
     * @param nombre nombre de la empresa.
     * @return un {@link Optional} con la empresa si existe, o vacío en caso contrario.
     */
    Optional<Empresa> buscarPorNombre(String nombre);

    /**
     * Actualiza los datos de una empresa existente.
     *
     * @param empresa empresa con los datos actualizados y su identificador.
     * @return la empresa actualizada.
     */
    Empresa actualizar(Empresa empresa);
}
