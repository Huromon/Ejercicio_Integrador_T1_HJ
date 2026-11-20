package com.tracker.exception;

/**
 * Error de validación de datos en la capa de negocio.
 * <p>
 * Se lanza cuando falta un campo obligatorio o su valor no es correcto.
 * </p>
 */
public class ValidacionException extends TrackerException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error.
     */
    public ValidacionException(String mensaje) {
        super(mensaje);
    }

    /**
     * Crea la excepción con un mensaje y la causa original.
     *
     * @param mensaje descripción del error.
     * @param causa   excepción que provocó el error.
     */
    public ValidacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
