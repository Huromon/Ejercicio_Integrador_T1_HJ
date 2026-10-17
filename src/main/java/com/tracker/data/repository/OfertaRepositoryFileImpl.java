package com.tracker.data.repository;

import com.tracker.data.entity.Estado;
import com.tracker.data.entity.Oferta;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class OfertaRepositoryFileImpl implements OfertaRepository {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String RUTA_FICHERO = "candidaturas.csv";
    private static final String CABECERA = "id;titulo;empresa;estado;fechaPostulacion;enlace;descripcion;salario";

    @Override
    public List<Oferta> listarOfertas() {
        List<Oferta> ofertas = new ArrayList<>();
        File archivo = new File(RUTA_FICHERO);

        if (!archivo.exists()) {
            return ofertas;
        }

        try (BufferedReader bfr = new BufferedReader(new FileReader(archivo))) {

            String cabecera = bfr.readLine();
            String linea;

            while ((linea = bfr.readLine()) != null) {

                if (!linea.isBlank()) {
                    String[] datos = linea.split(";", -1);

                    Integer id = Integer.parseInt(datos[0].trim());
                    String titulo = datos[1].trim();
                    String empresa = datos.length > 2 ? datos[2].trim() : "";
                    Estado estado = Estado.valueOf(datos[3].trim().toUpperCase());
                    LocalDate fechaPostulacion = LocalDate.parse(datos[4].trim(), FORMATO);

                    String enlace = datos.length > 5 ? datos[5].trim() : "";
                    String descripcion = datos.length > 6 ? datos[6].trim() : "";

                    Double salario = (datos.length > 7 && !datos[7].trim().isEmpty()) ? Double.parseDouble(datos[7].trim()) : null;
                    Oferta oferta = new Oferta(id, titulo, empresa, estado, fechaPostulacion, enlace, descripcion, salario);
                    ofertas.add(oferta);
                }
            }

        } catch (IOException e) {
            System.out.println("Error de entrada/salida al leer el fichero: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error al parsear algún dato (revisa el formato de fecha o el estado): " + e.getMessage());
        }

        return ofertas;
    }

    @Override
    public Oferta buscarPorId(Integer id) {
        List<Oferta> ofertas = listarOfertas();

        for (Oferta oferta : ofertas) {
            if (id.equals(oferta.id())) {
                return oferta;
            }
        }
        return null;
    }

    @Override
    public void guardarOferta(Oferta oferta) {
        File archivo = new File(RUTA_FICHERO);

        String nuevaOferta = convertirAFormatoCsv(oferta);

        try (BufferedWriter bfr = new BufferedWriter(new FileWriter(archivo, true))) {
            bfr.write(nuevaOferta);
            System.out.println("Oferta guardada correctamente en el fichero.");
        } catch (IOException e) {
            System.out.println("Error al guardar la oferta: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminarPorId(Integer id) {
        List<Oferta> ofertas = listarOfertas();
        boolean encontrada = ofertas.removeIf(oferta -> oferta.id().equals(id));

        if (!encontrada) {
            return false;
        }

        File archivo = new File(RUTA_FICHERO);

        try (BufferedWriter bfr = new BufferedWriter(new FileWriter(archivo, false))) {

            bfr.write(CABECERA + "\n");
            for (Oferta oferta : ofertas) {
                String linea = convertirAFormatoCsv(oferta);
                bfr.write(linea);
            }

            return true;

        } catch (IOException e) {
            System.out.println("Error al eliminar la oferta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void guardarTodas(List<Oferta> ofertas) {
        File archivo = new File(RUTA_FICHERO);

        try (BufferedWriter bfr = new BufferedWriter(new FileWriter(archivo, false))) {
            bfr.write(CABECERA + "\n");

            for (Oferta oferta : ofertas) {
                bfr.write(convertirAFormatoCsv(oferta));
            }

            System.out.println("Se han guardado todas las ofertas correctamente.");

        } catch (IOException e) {
            System.out.println("Error de entrada/salida al guardar todas las ofertas: " + e.getMessage());
        }
    }

    /* HELPERS */

    private static String convertirAFormatoCsv(Oferta oferta) {
        String linea = oferta.id() + ";" +
                oferta.titulo() + ";" +
                oferta.empresa() + ";" +
                oferta.estado() + ";" +
                oferta.fechaPostulacion() + ";" +
                (oferta.enlace() != null ? oferta.enlace() : "") + ";" +
                (oferta.descripcion() != null ? oferta.descripcion() : "") + ";" +
                (oferta.salario() != null ? oferta.salario() : "") + "\n";
        return linea;
    }
}
