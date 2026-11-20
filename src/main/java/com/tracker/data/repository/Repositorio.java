package com.tracker.data.repository;

import java.util.List;
import java.util.Optional;

/**
 * Contrato genérico de persistencia para una entidad.
 *
 * @param <T> tipo de entidad gestionada por el repositorio.
 */
public interface Repositorio<T> {

    /**
     * Almacena una entidad y devuelve el resultado persistido con su identificador.
     *
     * @param t entidad a almacenar.
     * @return la entidad persistida.
     */
    T guardar(T t);

    /**
     * Elimina una entidad por su identificador.
     *
     * @param id identificador de la entidad a eliminar.
     */
    void eliminar(Integer id);

    /**
     * Busca una entidad por su identificador.
     *
     * @param id identificador de la entidad.
     * @return un {@link Optional} con la entidad si existe, o vacío en caso contrario.
     */
    Optional<T> buscarPorId(Integer id);

    /**
     * Devuelve todas las entidades almacenadas.
     *
     * @return lista de entidades.
     */
    List<T> buscarTodos();
}
