package com.tracker.presentation;
import com.tracker.controllers.SceneNavegator;
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
        SceneNavegator.setStage(stage);
        SceneNavegator.navigateTo("main-view.fxml","init");
        stage.show();
    }
}
