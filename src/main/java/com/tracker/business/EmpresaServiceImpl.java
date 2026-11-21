package com.tracker.business;

import com.tracker.data.entity.Empresa;
import com.tracker.data.repository.EmpresaRepository;
import com.tracker.exception.NoEncontradoException;
import com.tracker.exception.ValidacionException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de los servicios de negocio de empresas.
 */
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;

    /**
     * Crea el servicio con el repositorio de empresas que debe utilizar.
     *
     * @param empresaRepository repositorio de empresas.
     */
    public EmpresaServiceImpl(EmpresaRepository empresaRepository) {

        this.empresaRepository = empresaRepository;
    }

    /**
     * Valida los datos y crea una nueva empresa con la fecha de registro actual.
     *
     * @param nombre nombre de la empresa, obligatorio y único.
     * @param sector sector al que pertenece la empresa.
     * @param web    sitio web de la empresa.
     * @return la empresa creada con su identificador asignado.
     */
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

    /**
     * Valida los datos y actualiza una empresa existente, conservando su fecha de registro.
     *
     * @param id     identificador de la empresa a modificar.
     * @param nombre nuevo nombre de la empresa, obligatorio y único.
     * @param sector nuevo sector de la empresa.
     * @param web    nuevo sitio web de la empresa.
     * @return la empresa con los datos actualizados.
     */
    @Override
    public Empresa actualizar(Integer id, String nombre, String sector, String web) {
        Empresa existente = buscarPorId(id).orElseThrow(() ->
                new NoEncontradoException("No se encuentra la empresa con ID: " + id));
        if (nombre == null || nombre.isBlank()) {
            throw new ValidacionException("El nombre de la empresa es obligatorio.");
        }
        Empresa duplicada = empresaRepository.buscarPorNombre(nombre.trim()).orElse(null);
        if (duplicada != null && !duplicada.getId().equals(id)) {
            throw new ValidacionException("Ya existe una empresa con ese nombre.");
        }
        return empresaRepository.actualizar(new Empresa(
                existente.getId(), nombre.trim(), sector, web, existente.getFechaRegistro()));
    }

    /**
     * Devuelve todas las empresas registradas.
     *
     * @return lista de empresas.
     */
    @Override
    public List<Empresa> listarTodas() {
        return empresaRepository.buscarTodos();
    }

    /**
     * Busca una empresa por su identificador.
     *
     * @param id identificador de la empresa.
     * @return un {@link Optional} con la empresa si existe, o vacío en caso contrario.
     */
    @Override
    public Optional<Empresa> buscarPorId(Integer id) {
        if (id == null) {
            throw new ValidacionException("El ID de la empresa no puede ser nulo.");
        }
        return empresaRepository.buscarPorId(id);
    }

    /**
     * Comprueba que la empresa existe y la elimina.
     *
     * @param id identificador de la empresa a eliminar.
     */
    @Override
    public void eliminar(Integer id) {
        if (id == null || empresaRepository.buscarPorId(id).isEmpty()) {
            throw new NoEncontradoException("No se encuentra la empresa con ID: " + id);
        }
        empresaRepository.eliminar(id);
    }
}
