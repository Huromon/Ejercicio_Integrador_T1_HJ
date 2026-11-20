package com.tracker.exception;

/**
 * Error en la configuración de la conexión con la base de datos.
 * <p>
 * Se lanza cuando faltan las variables de entorno con las credenciales.
 * </p>
 */
public class ConfiguracionException extends TrackerException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error.
     */
    public ConfiguracionException(String mensaje) {
        super(mensaje);
    }
}
