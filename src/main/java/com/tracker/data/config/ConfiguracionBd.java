package com.tracker.data.config;

import com.tracker.exception.ConfiguracionException;

/**
 * Configuración de la conexión con MySQL.
 * <p>
 * La URL está fijada en el código y las credenciales se leen de las variables
 * de entorno {@code DB_USER} y {@code DB_PASSWORD}.
 * </p>
 */
public final class ConfiguracionBd {

    private static final String URL_BASE_DATOS = "jdbc:mysql://localhost:3307/Candidaturas";
    private static final String URL_SERVIDOR = "jdbc:mysql://localhost:3307";

    private static final String VARIABLE_USUARIO = "DB_USER";
    private static final String VARIABLE_PASSWORD = "DB_PASSWORD";

    private final String usuario;
    private final String password;

    private ConfiguracionBd(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }

    /**
     * Lee las credenciales de las variables de entorno.
     *
     * @return la configuración lista para usar.
     * @throws ConfiguracionException si falta alguna variable obligatoria.
     */
    public static ConfiguracionBd desdeEntorno() {
        String usuario = System.getenv(VARIABLE_USUARIO);
        String password = System.getenv(VARIABLE_PASSWORD);

        if (usuario == null || usuario.isBlank()) {
            throw new ConfiguracionException(
                    "Falta la variable de entorno " + VARIABLE_USUARIO + ".");
        }
        if (password == null) {
            throw new ConfiguracionException(
                    "Falta la variable de entorno " + VARIABLE_PASSWORD + ".");
        }

        return new ConfiguracionBd(usuario, password);
    }

    /**
     * URL de conexión a la base de datos de la aplicación.
     *
     * @return la URL de {@code Candidaturas}.
     */
    public static String getUrlBaseDatos() {
        return URL_BASE_DATOS;
    }

    /**
     * URL de conexión al servidor, sin base de datos seleccionada.
     *
     * @return la URL del servidor MySQL.
     */
    public static String getUrlServidor() {
        return URL_SERVIDOR;
    }

    /**
     * Usuario de MySQL.
     *
     * @return el usuario configurado.
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Contraseña de MySQL.
     *
     * @return la contraseña configurada.
     */
    public String getPassword() {
        return password;
    }
}
