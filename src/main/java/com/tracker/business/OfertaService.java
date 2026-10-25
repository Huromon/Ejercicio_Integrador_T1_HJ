package com.tracker.business;

import com.tracker.data.entity.Estado;
import com.tracker.data.entity.Oferta;
import com.tracker.data.repository.OfertaRepository;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OfertaService implements OfertaServicesint {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final OfertaRepository repository;

    /**
     * Constructor que inyecta la dependencia del repositorio.
     * @param repository repositorio de datos
     */
    public OfertaService(OfertaRepository repository) {
        this.repository = repository;
    }

    /**
     * Busca y devuelve la representación en cadena de una candidatura por su ID.
     * @param id identificador de la candidatura
     * @return representación en texto de la candidatura o mensaje de error
     */
    @Override
    public String verCandidatura(Integer id) {
        Oferta oferta = repository.buscarPorId(id);
        boolean existeOferta = oferta != null;

        String resultado = "No se ha encontrado ninguna candidatura con el ID: " + id;
        if (existeOferta) {
            resultado = oferta.toString();
        }
        return resultado;
    }

    /**
     * Elimina una candidatura del sistema según su identificador.
     * @param id identificador de la candidatura a eliminar
     * @return mensaje indicando el éxito o el fallo de la operación
     */
    @Override
    public String eliminarCandidatura(Integer id) {
        boolean eliminada = repository.eliminarPorId(id);
        String resultado = "No se ha encontrado ninguna candidatura con el ID: " + id;

        if (eliminada) {
            resultado = "Candidatura con ID " + id + " eliminada con éxito.";
        }
        return resultado;
    }

    /**
     * Lista todas las candidaturas registradas en el sistema.
     * @return cadena con todas las ofertas formateadas o mensaje de registro vacío
     */
    @Override
    public String listar() {
        List<Oferta> ofertas = repository.listarOfertas();
        boolean hayOfertas = !ofertas.isEmpty();

        String resultado = "No hay ninguna candidatura registrada.";
        if (hayOfertas) {
            resultado = formatearListaOfertas(ofertas);
        }
        return resultado;
    }

    /**
     * Registra una nueva candidatura validando sus parámetros obligatorios.
     * @param titulo título de la oferta
     * @param empresa nombre de la empresa
     * @param estadoStr estado inicial en texto
     * @param fechaStr fecha de postulación en texto
     * @param enlace enlace web de la oferta
     * @param descripcion descripción del puesto
     * @param salario salario ofrecido
     * @return mensaje de éxito o error correspondiente
     */
    @Override
    public String anadir(String titulo, String empresa, String estadoStr, String fechaStr, String enlace, String descripcion, Double salario) {
        boolean parametrosValidos = validarParametrosObligatorios(titulo, empresa, estadoStr, fechaStr);

        if (!parametrosValidos) {
            return "Error: Faltan argumentos obligatorios (--titulo, --empresa, --estado, --fecha).";
        }

        String respuesta;
        try {
            Estado estado = Estado.valueOf(estadoStr.toUpperCase().trim());
            LocalDate fecha = LocalDate.parse(fechaStr.trim(), FORMATO);

            List<Oferta> existentes = repository.listarOfertas();
            int nuevoId = calcularSiguienteId(existentes);

            Oferta nueva = new Oferta(nuevoId, titulo, empresa, estado, fecha, enlace, descripcion, salario);
            repository.guardarOferta(nueva);

            respuesta = "Candidatura añadida con éxito con el ID " + nuevoId;
        } catch (IllegalArgumentException e) {
            respuesta = "Error en los datos introducidos (estado o fecha no válidos): " + e.getMessage();
        } catch (Exception e) {
            respuesta = "Error al añadir la candidatura: " + e.getMessage();
        }
        return respuesta;
    }

    /**
     * Filtra las candidaturas existentes según empresa y/o estado.
     * @param empresaFiltro nombre de la empresa a filtrar
     * @param estadoFiltro estado a filtrar
     * @return lista de ofertas coincidentes formateadas o mensaje de no resultados
     */
    @Override
    public String filtrar(String empresaFiltro, String estadoFiltro) {
        List<Oferta> ofertas = repository.listarOfertas();
        List<Oferta> resultadoFiltrado = new ArrayList<>();

        for (Oferta o : ofertas) {
            boolean coincideEmpresa = (empresaFiltro == null) || o.empresa().equalsIgnoreCase(empresaFiltro);
            boolean coincideEstado = (estadoFiltro == null) || o.estado().name().equalsIgnoreCase(estadoFiltro);

            if (coincideEmpresa && coincideEstado) {
                resultadoFiltrado.add(o);
            }
        }

        boolean hayCoincidencias = !resultadoFiltrado.isEmpty();
        String respuesta = "No se encontraron candidaturas que coincidan con los filtros.";

        if (hayCoincidencias) {
            respuesta = formatearListaOfertas(resultadoFiltrado);
        }

        return respuesta;
    }

    /**
     * Importa candidaturas desde un archivo CSV externo.
     * @param rutaFichero ruta del archivo a importar
     * @return mensaje con el resultado de la importación
     */
    @Override
    public String importar(String rutaFichero) {
        boolean rutaValida = rutaFichero != null && !rutaFichero.isBlank();
        if (!rutaValida) {
            return "Error: Debe indicar la ruta del archivo CSV a importar.";
        }

        File archivo = new File(rutaFichero);
        boolean existeArchivo = archivo.exists();
        if (!existeArchivo) {
            return "Error: El archivo de importación '" + rutaFichero + "' no existe.";
        }

        List<Oferta> nuevasOfertas = repository.importarDesdeFichero(rutaFichero);
        boolean hayNuevasOfertas = !nuevasOfertas.isEmpty();

        if (!hayNuevasOfertas) {
            return "No se ha importado ninguna candidatura o el archivo está vacío.";
        }

        return "Se han importado " + nuevasOfertas.size() + " candidaturas con éxito desde '" + rutaFichero + "'.";
    }

    /**
     * Comprueba que los campos obligatorios no sean nulos.
     * @param titulo título de la oferta
     * @param empresa nombre de la empresa
     * @param estadoStr estado en texto
     * @param fechaStr fecha en texto
     * @return true si están todos presentes, false en caso contrario
     */
    private boolean validarParametrosObligatorios(String titulo, String empresa, String estadoStr,String fechaStr) {
        boolean esValido = titulo != null && empresa != null && estadoStr != null && fechaStr != null;
        return esValido;
    }

    /**
     * Calcula el siguiente identificador único disponible.
     * @param ofertas lista de ofertas existentes
     * @return siguiente ID numérico
     */
    private int calcularSiguienteId(List<Oferta> ofertas) {
        int maxId = 0;
        for (Oferta o : ofertas) {
            if (o.id() > maxId) {
                maxId = o.id();
            }
        }
        int siguienteId = maxId + 1;
        return siguienteId;
    }

    /**
     * Convierte una lista de ofertas en una cadena con saltos de línea.
     * @param ofertas lista de ofertas a formatear
     * @return texto formateado de las ofertas
     */
    private String formatearListaOfertas(List<Oferta> ofertas) {
        String texto = "";
        for (Oferta o : ofertas) {
            texto += o.toString() + "\n";
        }
        String textoFormateado = texto.trim();
        return textoFormateado;
    }
}