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

    public static EmpresaService getEmpresaService() {
        return empresaService;
    }

    public static OfertaService getOfertaService() {
        return ofertaService;
    }

    public static EntrevistaService getEntrevistaService() {
        return entrevistaService;
    }
}
