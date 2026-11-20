package com.tracker.controllers;

import com.tracker.presentation.Aplicacion;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneNavegator {
    private static Stage stage;


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
    public static void setStage(Stage stage) {
        SceneNavegator.stage = stage;
    }
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
