package com.tracker.business;

public interface OfertaServicesint {
        String verCandidatura(Integer id);
        String eliminarCandidatura(Integer id);
        String listar();
        String anadir(String titulo, String empresa, String estadoStr, String fechaStr, String enlace, String descripcion, Double salario);
        String filtrar(String empresaFiltro, String estadoFiltro);
        String importar(String rutaFichero);
}