package com.tracker.data.repository;

import com.tracker.data.entity.Estado;
import com.tracker.data.entity.Oferta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class OfertaRepositoryFileImpl implements OfertaRepository {
    private static Logger logger = Logger.getLogger("Repositorio");

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String RUTA_FICHERO = "candidaturas.csv";
    private static final String CABECERA = "id;titulo;empresa;estado;fechaPostulacion;enlace;descripcion;salario";

    /**
     * Lista todas las ofertas almacenadas en el fichero de datos.
     * @return lista de ofertas leídas
     */
    @Override
    public List<Oferta> listarOfertas() {
        List<Oferta> ofertas = new ArrayList<>();
        File archivo = new File(RUTA_FICHERO);
        boolean existeArchivo = archivo.exists();

        if (existeArchivo) {
            try (BufferedReader bfr = new BufferedReader(new FileReader(archivo))) {
                bfr.readLine();
                String linea;

                while ((linea = bfr.readLine()) != null) {
                    boolean lineaValida = !linea.isBlank();
                    if (lineaValida) {
                        Oferta oferta = parsearLineaOferta(linea);
                        ofertas.add(oferta);
                    }
                }
            } catch (IOException e) {
                logger.warning("Error de entrada/salida al leer el fichero: " + e.getMessage());
            } catch (Exception e) {
                logger.warning("Error al parsear datos del CSV: " + e.getMessage());
            }
        }

        return ofertas;
    }

    /**
     * Busca una oferta específica por su identificador único.
     * @param id identificador de la oferta
     * @return objeto Oferta encontrado o nulo si no existe
     */
    @Override
    public Oferta buscarPorId(Integer id) {
        Oferta ofertaEncontrada = null;
        List<Oferta> ofertas = listarOfertas();

        for (Oferta oferta : ofertas) {
            boolean coincideId = id.equals(oferta.id());
            if (coincideId) {
                ofertaEncontrada = oferta;
            }
        }
        return ofertaEncontrada;
    }

    /**
     * Guarda una nueva oferta añadiéndola al conjunto existente.
     * @param oferta oferta a guardar
     */
    @Override
    public void guardarOferta(Oferta oferta) {
        List<Oferta> ofertas = listarOfertas();
        ofertas.add(oferta);
        guardarTodas(ofertas);
    }

    /**
     * Elimina una oferta del almacenamiento según su identificador.
     * @param id identificador de la oferta a eliminar
     * @return true si se eliminó con éxito, false si no se encontró
     */
    @Override
    public boolean eliminarPorId(Integer id) {
        List<Oferta> ofertas = listarOfertas();
        boolean encontrada = ofertas.removeIf(oferta -> oferta.id().equals(id));

        if (encontrada) {
            guardarTodas(ofertas);
        }
        return encontrada;
    }

    /**
     * Guarda una lista completa de ofertas sobrescribiendo el archivo.
     * @param ofertas lista de ofertas a guardar
     */
    @Override
    public void guardarTodas(List<Oferta> ofertas) {
        File archivo = new File(RUTA_FICHERO);

        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(archivo, false))) {
            bfw.write(CABECERA);
            bfw.newLine();

            for (Oferta oferta : ofertas) {
                String linea = construirLineaCsv(oferta);
                bfw.write(linea);
                bfw.newLine();
            }
        } catch (IOException e) {
            logger.warning("Error de entrada/salida al guardar todas las ofertas: " + e.getMessage());
        }
    }

    /**
     * Importa nuevas ofertas desde un archivo CSV externo asignándoles IDs consecutivos.
     * @param rutaFichero ruta del archivo CSV a importar
     * @return lista de ofertas nuevas importadas
     */
    @Override
    public List<Oferta> importarDesdeFichero(String rutaFichero) {
        List<Oferta> nuevasOfertas = new ArrayList<>();
        File archivo = new File(rutaFichero);
        boolean existeArchivo = archivo.exists();

        if (!existeArchivo) {
            return nuevasOfertas;
        }

        int ultimoId = obtenerMaximoIdActual();

        try (BufferedReader bfr = new BufferedReader(new FileReader(archivo))) {
            bfr.readLine();
            String linea;

            while ((linea = bfr.readLine()) != null) {
                boolean lineaValida = !linea.isBlank();
                if (lineaValida) {
                    ultimoId++;
                    Oferta nueva = parsearLineaImportada(linea, ultimoId);
                    nuevasOfertas.add(nueva);
                }
            }

            if (!nuevasOfertas.isEmpty()) {
                List<Oferta> existentes = listarOfertas();
                existentes.addAll(nuevasOfertas);
                guardarTodas(existentes);
            }

        } catch (Exception e) {
            logger.warning("Error al leer el fichero: " + e.getMessage());
        }

        return nuevasOfertas;
    }

    /**
     * Parsea una línea del fichero de almacenamiento y crea un objeto Oferta.
     * @param linea línea de texto del CSV
     * @return objeto Oferta parseado
     */
    private Oferta parsearLineaOferta(String linea) {
        String[] datos = linea.split(";", -1);

        Integer id = Integer.parseInt(datos[0].trim());
        String titulo = datos[1].trim();
        String empresa = datos[2].trim();
        Estado estado = Estado.valueOf(datos[3].trim().toUpperCase());
        LocalDate fechaPostulacion = LocalDate.parse(datos[4].trim(), FORMATO);

        String enlace = extraerEnlace(datos);
        String descripcion = extraerDescripcion(datos);
        Double salario = extraerSalario(datos);

        Oferta oferta = new Oferta(id, titulo, empresa, estado, fechaPostulacion, enlace, descripcion, salario);
        return oferta;
    }

    /**
     * Parsea una línea de un fichero externo asignándole un ID específico.
     * @param linea línea de texto del archivo importado
     * @param idAsignado identificador único asignado
     * @return objeto Oferta parseado
     */
    private Oferta parsearLineaImportada(String linea, int idAsignado) {
        String[] datos = linea.split(";", -1);

        String titulo = datos[1].trim();
        String empresa = datos[2].trim();
        Estado estado = Estado.valueOf(datos[3].trim().toUpperCase());
        LocalDate fechaPostulacion = LocalDate.parse(datos[4].trim(), FORMATO);

        String enlace = extraerEnlace(datos);
        String descripcion = extraerDescripcion(datos);
        Double salario = extraerSalario(datos);

        Oferta oferta = new Oferta(idAsignado, titulo, empresa, estado, fechaPostulacion, enlace, descripcion, salario);
        return oferta;
    }

    /**
     * Extrae el campo de enlace de los datos divididos de la línea.
     * @param datos array de campos de la línea CSV
     * @return enlace de la oferta o cadena vacía
     */
    private String extraerEnlace(String[] datos) {
        String enlace = datos.length > 5 ? datos[5].trim() : "";
        return enlace;
    }

    /**
     * Extrae el campo de descripción de los datos divididos de la línea.
     * @param datos array de campos de la línea CSV
     * @return descripción de la oferta o cadena vacía
     */
    private String extraerDescripcion(String[] datos) {
        String descripcion = datos.length > 6 ? datos[6].trim() : "";
        return descripcion;
    }

    /**
     * Extrae el campo de salario de los datos divididos de la línea.
     * @param datos array de campos de la línea CSV
     * @return salario en formato Double o 0.0 por defecto
     */
    private Double extraerSalario(String[] datos) {
        Double salario = 0.0;
        boolean tieneSalario = datos.length > 7 && !datos[7].trim().isEmpty();
        if (tieneSalario) {
            salario = Double.parseDouble(datos[7].trim());
        }
        return salario;
    }

    /**
     * Obtiene el valor máximo de ID actualmente registrado en el almacenamiento.
     * @return valor entero del ID más alto
     */
    private int obtenerMaximoIdActual() {
        List<Oferta> existentes = listarOfertas();
        int maxId = 0;
        for (Oferta o : existentes) {
            if (o.id() > maxId) {
                maxId = o.id();
            }
        }
        return maxId;
    }

    /**
     * Construye una cadena de texto en formato CSV a partir de un objeto Oferta.
     * @param oferta oferta a serializar
     * @return línea formateada para el fichero
     */
    private String construirLineaCsv(Oferta oferta) {
        String enlaceStr = oferta.enlace() != null ? oferta.enlace() : "";
        String descStr = oferta.descripcion() != null ? oferta.descripcion() : "";
        String salarioStr = (oferta.salario() != null && oferta.salario() != 0.0) ? String.valueOf(oferta.salario()) : "";

        String linea = oferta.id() + ";" +
                oferta.titulo() + ";" +
                oferta.empresa() + ";" +
                oferta.estado() + ";" +
                oferta.fechaPostulacion().format(FORMATO) + ";" +
                enlaceStr + ";" +
                descStr + ";" +
                salarioStr;
        return linea;
    }
}