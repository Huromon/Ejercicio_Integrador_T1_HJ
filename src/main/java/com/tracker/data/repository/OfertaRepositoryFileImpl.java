package com.tracker.data.repository;

import com.tracker.data.entity.Estado;
import com.tracker.data.entity.Oferta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OfertaRepositoryFileImpl implements OfertaRepository {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String RUTA_FICHERO = "candidaturas.csv";

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
        return null;
    }

    @Override
    public void guardarOferta(Oferta oferta) {

    }

    @Override
    public boolean eliminarPorId(Integer id) {
        return false;
    }

    @Override
    public void guardarTodas(List<Oferta> ofertas) {

    }
}
