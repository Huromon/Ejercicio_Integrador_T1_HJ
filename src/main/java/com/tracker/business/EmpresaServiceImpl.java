package com.tracker.business;

import com.tracker.data.entity.Empresa;
import com.tracker.data.repository.EmpresaRepository;
import com.tracker.exception.NoEncontradoException;
import com.tracker.exception.ValidacionException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    public Empresa crear(String nombre, String sector, String web) {
        if (nombre == null || nombre.isBlank()) {
            throw new ValidacionException("El nombre de la empresa es obligatorio.");
        }
        if (empresaRepository.buscarPorNombre(nombre.trim()).isPresent()) {
            throw new ValidacionException("Ya existe una empresa con ese nombre.");
        }
        Empresa nueva = new Empresa(null, nombre.trim(), sector, web, LocalDate.now());
        return empresaRepository.guardar(nueva);
    }

    @Override
    public List<Empresa> listarTodas() {
        return empresaRepository.buscarTodos();
    }

    @Override
    public Optional<Empresa> buscarPorId(Integer id) {
        if (id == null) {
            throw new ValidacionException("El ID de la empresa no puede ser nulo.");
        }
        return empresaRepository.buscarPorId(id);
    }

    @Override
    public void eliminar(Integer id) {
        if (id == null || empresaRepository.buscarPorId(id).isEmpty()) {
            throw new NoEncontradoException("No se encuentra la empresa con ID: " + id);
        }
        empresaRepository.eliminar(id);
    }
}
