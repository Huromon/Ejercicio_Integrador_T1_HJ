package com.tracker.exception;

/**
 * Error producido cuando se busca una entidad que no existe.
 */
public class NoEncontradoException extends TrackerException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error.
     */
    public NoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
