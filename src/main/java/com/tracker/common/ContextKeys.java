package com.tracker.common;

/**
 * Claves utilizadas para guardar y recuperar datos compartidos en {@link AppContext}.
 */
public final class ContextKeys {

    /** Clave del servicio de ofertas. */
    public static final String OFERTA_SERVICE = "ofertaService";

    /** Clave del servicio de empresas. */
    public static final String EMPRESA_SERVICE = "empresaService";

    /** Clave del servicio de entrevistas. */
    public static final String ENTREVISTA_SERVICE = "entrevistaService";

    /** Clave que indica si el listado muestra resultados filtrados. */
    public static final String FILTRA = "filtra";

    /** Clave donde se guardan las ofertas resultantes del último filtro. */
    public static final String OFERTAS_FILTRADAS = "ofertasFiltradas";

    /** Clave de la oferta seleccionada en el listado. */
    public static final String SELECCIONADO = "seleccionado";

    /**
     * Constructor privado para impedir la creación de instancias.
     */
    private ContextKeys() {
    }
}
