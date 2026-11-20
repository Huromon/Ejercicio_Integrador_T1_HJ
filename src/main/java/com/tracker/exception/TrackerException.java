package com.tracker.exception;

/**
 * Excepción base de la aplicación.
 * <p>
 * Todas las excepciones propias del proyecto heredan de esta clase, de forma
 * que la interfaz pueda capturarlas de una sola vez si lo necesita.
 * </p>
 */
public class TrackerException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error.
     */
    public TrackerException(String mensaje) {
        super(mensaje);
    }

    /**
     * Crea la excepción con un mensaje y la causa original.
     *
     * @param mensaje descripción del error.
     * @param causa   excepción que provocó el error.
     */
    public TrackerException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
