package com.tracker.common;

import com.tracker.controllers.AñadirController;
import com.tracker.controllers.DetallesController;
import com.tracker.controllers.FiltroController;
import com.tracker.controllers.ListadoController;
import com.tracker.presentation.Aplicacion;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
     */
    public static void navigateTo(String fxml, String title) {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplicacion.class.getResource("/com/tracker/views/" + fxml));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            mostrarError(fxml, e);
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
    public static void navigateToInjection(String fxml, String title) {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplicacion.class.getResource("/com/tracker/views/" + fxml));
        fxmlLoader.setControllerFactory(type -> {
            switch (fxml) {
                case "nuevo-view.fxml":   return new AñadirController();
                case "main-view.fxml":    return new ListadoController();
                case "filtro-view.fxml": return new FiltroController();
                case "Candidatura-view.fxml": return new DetallesController();
            }
            throw new IllegalArgumentException("Vista no soportada: " + fxml);
        });
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            mostrarError(fxml, e);
        }
    }

    /**
     * Muestra un diálogo de error cuando no se puede cargar una vista.
     *
     * @param fxml nombre del archivo FXML que no se pudo cargar.
     * @param e    excepción producida al cargar la vista.
     */
    private static void mostrarError(String fxml, IOException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No se pudo cargar la vista");
        alert.setContentText(fxml + "\n" + e.getMessage());
        alert.showAndWait();
    }
}
