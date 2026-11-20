package com.tracker.exception;

/**
 * Error producido en la capa de acceso a datos.
 * <p>
 * Los repositorios capturan {@link java.sql.SQLException}, registran el error
 * con el Logger y lo encapsulan en esta excepción.
 * </p>
 */
public class AccesoDatosException extends TrackerException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error.
     */
    public AccesoDatosException(String mensaje) {
        super(mensaje);
    }

    /**
     * Crea la excepción con un mensaje y la causa original.
     *
     * @param mensaje descripción del error.
     * @param causa   excepción SQL original.
     */
    public AccesoDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
