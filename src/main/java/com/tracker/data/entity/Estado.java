package com.tracker.data.entity;

/**
 * Estados posibles de una candidatura.
 */
public enum Estado {
    PENDIENTE, ENTREVISTA, RECHAZADA, ACEPTADA;

    /**
     * Convierte un texto al estado correspondiente, sin distinguir mayúsculas.
     *
     * @param texto texto a convertir; si es {@code null} se devuelve {@link #PENDIENTE}.
     * @return el estado correspondiente al texto.
     */
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
