package com.tracker.controllers;

import com.tracker.common.SceneNavegator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la vista para añadir una nueva candidatura.
 */
public class AñadirController implements Initializable {
    @FXML
    private Button btnAñadir;

    @FXML
    private Button btnCancelar;

    @FXML
    private ChoiceBox<String> choiceboxEstado;

    @FXML
    private DatePicker dateFechaPostulacion;

    @FXML
    private TextArea inpDesc;

    @FXML
    private TextField inpEmpresa;

    @FXML
    private TextField inpEnlace;

    @FXML
    private TextField inpNotas;

    @FXML
    private TextField inpSalario;

    @FXML
    private TextField inpTitulo;

    /**
     * Inicializa los componentes de la vista una vez cargada.
     *
     * @param location  ubicación del archivo FXML.
     * @param resources recursos de internacionalización de la vista.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceboxEstado.getItems().addAll(
                "Pendiente",
                "Entrevista",
                "Aceptada",
                "Rechazada"
        );
        choiceboxEstado.getSelectionModel().selectFirst();
    }



    public void onClickAñadir(ActionEvent actionEvent) {
        SceneNavegator.navigateToInjection("main-view.fxml","Pagina Principal");
    }

    public void onClickCancelar(ActionEvent actionEvent) {
        SceneNavegator.navigateToInjection("main-view.fxml","Pagina Principal");
    }
}
