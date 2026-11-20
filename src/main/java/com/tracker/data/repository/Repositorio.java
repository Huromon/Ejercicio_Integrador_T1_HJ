package com.tracker.data.repository;

import java.util.List;
import java.util.Optional;

public interface Repositorio<T> {
    T guardar(T t);
    void eliminar(Integer id);
    Optional<T> buscarPorId(Integer id);
    List<T> buscarTodos();
}
