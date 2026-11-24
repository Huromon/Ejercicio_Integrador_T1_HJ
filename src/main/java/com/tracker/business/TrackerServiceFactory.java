package com.tracker.business;

import com.tracker.data.DataBaseInitializer;
import com.tracker.data.TransaccionManager;
import com.tracker.data.repository.*;

/**
 * Punto único de composición de los servicios de la aplicación v2.0.
 * <p>
 * Crea una sola vez los repositorios JDBC y los servicios de negocio,
 * los conecta entre sí y los expone a la interfaz JavaFX del compañero.
 * </p>
 */
public final class TrackerServiceFactory {

    private static EmpresaService empresaService;
    private static OfertaService ofertaService;
    private static EntrevistaService entrevistaService;

    /**
     * Constructor privado para impedir la creación de instancias de la factoría.
     */
    private TrackerServiceFactory() {
    }

    /**
     * Inicializa la base de datos y construye los servicios una sola vez.
     */
    public static void inicializar() {
        if (empresaService != null) {
            return;
        }
        DataBaseInitializer.inicializar();

        EmpresaRepository empresaRepository = new EmpresaJdbcRepository();
        OfertaRepository ofertaRepository = new OfertaJdbcRepository();
        EntrevistaRepository entrevistaRepository = new EntrevistaJdbcRepository();
        TransaccionManager transaccionManager = new TransaccionManager();

        empresaService = new EmpresaServiceImpl(empresaRepository);
        ofertaService = new OfertaServiceImpl(ofertaRepository, empresaRepository, entrevistaRepository, transaccionManager);
        entrevistaService = new EntrevistaServiceImpl(entrevistaRepository, ofertaRepository, transaccionManager);
    }

    /**
     * @return el servicio de empresas ya inicializado.
     * @throws IllegalStateException si no se llamó a {@link #inicializar()}.
     */
    public static EmpresaService getEmpresaService() {
        if (empresaService == null) {
            throw new IllegalStateException("Debe llamar a TrackerServiceFactory.inicializar() primero.");
        }
        return empresaService;
    }

    /**
     * @return el servicio de ofertas ya inicializado.
     * @throws IllegalStateException si no se llamó a {@link #inicializar()}.
     */
    public static OfertaService getOfertaService() {
        if (ofertaService == null) {
            throw new IllegalStateException("Debe llamar a TrackerServiceFactory.inicializar() primero.");
        }
        return ofertaService;
    }

    /**
     * @return el servicio de entrevistas ya inicializado.
     * @throws IllegalStateException si no se llamó a {@link #inicializar()}.
     */
    public static EntrevistaService getEntrevistaService() {
        if (entrevistaService == null) {
            throw new IllegalStateException("Debe llamar a TrackerServiceFactory.inicializar() primero.");
        }
        return entrevistaService;
    }
}
