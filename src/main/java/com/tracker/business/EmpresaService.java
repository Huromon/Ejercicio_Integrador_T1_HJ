package com.tracker.business;

import com.tracker.data.entity.Empresa;
import java.util.List;
import java.util.Optional;

/**
 * Servicios de negocio para la gestión de empresas.
 */
public interface EmpresaService {

    /**
     * Crea una nueva empresa.
     *
     * @param nombre nombre de la empresa, obligatorio y único.
     * @param sector sector al que pertenece la empresa.
     * @param web    sitio web de la empresa.
     * @return la empresa creada con su identificador asignado.
     */
    Empresa crear(String nombre, String sector, String web);

    /**
     * Modifica los datos de una empresa existente.
     *
     * @param id     identificador de la empresa a modificar.
     * @param nombre nuevo nombre de la empresa, obligatorio y único.
     * @param sector nuevo sector de la empresa.
     * @param web    nuevo sitio web de la empresa.
     * @return la empresa con los datos actualizados.
     */
    Empresa actualizar(Integer id, String nombre, String sector, String web);

    /**
     * Devuelve todas las empresas registradas.
     *
     * @return lista de empresas.
     */
    List<Empresa> listarTodas();

    /**
     * Busca una empresa por su identificador.
     *
     * @param id identificador de la empresa.
     * @return un {@link Optional} con la empresa si existe, o vacío en caso contrario.
     */
    Optional<Empresa> buscarPorId(Integer id);

    /**
     * Elimina una empresa por su identificador.
     *
     * @param id identificador de la empresa a eliminar.
     */
    void eliminar(Integer id);
}
