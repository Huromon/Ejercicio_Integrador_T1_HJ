package com.tracker.presentation;

import com.tracker.business.TrackerServiceFactory;
import com.tracker.common.AppContext;
import com.tracker.common.SceneNavegator;
import com.tracker.data.config.HibernateConfig;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Punto de entrada JavaFX de la aplicación de seguimiento de candidaturas.
 */
public class Aplicacion extends Application{

    /**
     * Configura la ventana principal y muestra la vista inicial del listado.
     *
     * @param stage ventana principal creada por JavaFX.
     * @throws Exception si no se puede cargar la vista inicial.
     */
    @Override
    public void start(Stage stage) throws Exception {

        TrackerServiceFactory.inicializar();
        AppContext.getInstance().put("filtra",false);
        SceneNavegator.setStage(stage);
        SceneNavegator.navigateTo("main-view.fxml","Pagina Principal");
        stage.show();
    }

    /**
     * Cierra el {@link org.hibernate.SessionFactory} cuando se cierra la ventana.
     */
    @Override
    public void stop() {
        HibernateConfig.cerrar();
    }
}
