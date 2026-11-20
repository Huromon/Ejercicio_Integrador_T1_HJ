package com.tracker.data.entity;

/**
 * Estados de una candidatura.
 */
public enum Estado {
    PENDIENTE, ENTREVISTA, RECHAZADA, ACEPTADA;

    public static Estado desdeTexto(String texto) {
        if (texto == null) return PENDIENTE;
        return switch (texto.toUpperCase().trim()) {
            case "ENTREVISTA" -> ENTREVISTA;
            case "RECHAZADA", "RECHAZADO" -> RECHAZADA;
            case "ACEPTADA", "ACEPTADO" -> ACEPTADA;
            default -> PENDIENTE;
        };
    }
}
