package com.tracker.presentation;
import com.tracker.Controllers.SceneNavegator;
import javafx.application.Application;
import javafx.stage.Stage;

public class Aplicacion extends Application{
    @Override
    public void start(Stage stage) throws Exception {
        SceneNavegator.setStage(stage);
        SceneNavegator.navigateTo("main-view.fxml","init");
        stage.show();
    }
}
