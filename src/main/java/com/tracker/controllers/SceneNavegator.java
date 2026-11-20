package com.tracker.controllers;

import com.tracker.presentation.Aplicacion;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utilidad para cambiar la escena mostrada en la ventana principal de la aplicación.
 */
public class SceneNavegator {

    /** Escena principal sobre la que se realizan las navegaciones. */
    private static Stage stage;

    /**
     * Carga una vista FXML y la muestra en la ventana principal con el título indicado.
     *
     * @param fxml  nombre del archivo FXML a cargar.
     * @param title título a mostrar en la ventana.
     * @throws IOException si no se puede cargar la vista.
     */
    public static void navigateTo(String fxml,String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplicacion.class.getResource("/com/tracker/views/" +fxml));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader. load());
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println("lol");
        }
    }

    /**
     * Establece la ventana principal sobre la que se navegará.
     *
     * @param stage ventana principal de la aplicación.
     */
    public static void setStage(Stage stage) {
        SceneNavegator.stage = stage;
    }

    /**
     * Carga una vista FXML asignando explícitamente su controlador según el archivo solicitado.
     *
     * @param fxml  nombre del archivo FXML a cargar.
     * @param title título a mostrar en la ventana.
     */
    public static void navigateToInjection(String fxml, String title)  {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplicacion.class.getResource("/com/tracker/views/" + fxml));


        fxmlLoader.setControllerFactory(type -> {
            switch (fxml) {
                case "nuevo-view.fxml":   return new AñadirController();
                case "main-view.fxml":    return new ListadoController();
                case "filtro-view.fxml": return new FiltroController();
                case "Candidarura-view.fxml": return new DetallesController();
            }
            throw new IllegalArgumentException("Vista no soportada: " + fxml);
        });
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(title);
            stage.setScene(scene);
        }catch (IOException e){
            System.out.println("lol");
        }
    }
}
