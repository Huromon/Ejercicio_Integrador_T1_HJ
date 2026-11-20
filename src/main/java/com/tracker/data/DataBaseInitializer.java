package com.tracker.data;

import com.tracker.data.config.DataSourceFactory;
import com.tracker.exception.AccesoDatosException;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Inicializador de la base de datos v2.0.
 * <p>
 * Crea el esquema y carga datos si la base de datos está vacía.
 * </p>
 */
public final class DataBaseInitializer {

    private static final Logger LOGGER = Logger.getLogger(DataBaseInitializer.class.getName());

    /**
     * Constructor privado para impedir la creación de instancias del inicializador.
     */
    private DataBaseInitializer() {
    }

    /**
     * Inicializa la base de datos ejecutando scripts SQL.
     */
    public static void inicializar() {
        ejecutarScript("/schema.sql", DataSourceFactory.getDataSourceServidor());
        
        if (estaVacia()) {
            ejecutarScript("/data.sql", DataSourceFactory.getDataSource());
            LOGGER.info("Base de datos inicializada con datos iniciales.");
        }
    }

    /**
     * Ejecuta todas las sentencias separadas por punto y coma de un script SQL.
     *
     * @param nombreArchivo recurso que contiene el script.
     * @param dataSource    origen de datos sobre el que se ejecuta.
     */
    private static void ejecutarScript(String nombreArchivo, DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             InputStream is = DataBaseInitializer.class.getResourceAsStream(nombreArchivo);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is));
             Statement stmt = conn.createStatement()) {

            String script = reader.lines().collect(Collectors.joining("\n"));
            for (String query : script.split(";")) {
                if (!query.trim().isEmpty()) {
                    stmt.execute(query);
                }
            }
        } catch (Exception e) {
            throw new AccesoDatosException("Error al ejecutar script: " + nombreArchivo, e);
        }
    }

    /**
     * Comprueba si la tabla de empresas no contiene registros.
     *
     * @return {@code true} si la tabla está vacía o no se puede consultar.
     */
    private static boolean estaVacia() {
        try (Connection conn = DataSourceFactory.getDataSource().getConnection();
             Statement stmt = conn.createStatement()) {
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM EMPRESA");
            return rs.next() && rs.getInt(1) == 0;
        } catch (Exception e) {
            return true;
        }
    }
}
