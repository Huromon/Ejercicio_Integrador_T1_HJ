package com.tracker.data.repository;

import com.tracker.data.entity.Empresa;
import java.util.Optional;

public interface EmpresaRepository extends Repositorio<Empresa> {
    Optional<Empresa> buscarPorNombre(String nombre);
}
