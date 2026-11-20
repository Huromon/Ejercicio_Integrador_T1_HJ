package com.tracker.business;

import com.tracker.data.entity.Empresa;
import java.util.List;
import java.util.Optional;

public interface EmpresaService {
    Empresa crear(String nombre, String sector, String web);
    List<Empresa> listarTodas();
    Optional<Empresa> buscarPorId(Integer id);
    void eliminar(Integer id);
}
