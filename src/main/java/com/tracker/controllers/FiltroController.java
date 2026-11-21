package com.tracker.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la vista de filtrado de candidaturas.
 */
public class FiltroController implements Initializable {
    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnFiltrar;

    @FXML
    private ChoiceBox<?> choiceboxEstado;

    @FXML
    private DatePicker dateFechaMax;

    @FXML
    private DatePicker dateFechaMin;

    @FXML
    private TextField inpSalarioMax;

    @FXML
    private TextField inpSalarioMin;

    @FXML
    private TextField textfieldEmpresa;

    @FXML
    private TextField textfieldTitulo;

    /**
     * Inicializa los componentes de la vista una vez cargada.
     *
     * @param location  ubicación del archivo FXML.
     * @param resources recursos de internacionalización de la vista.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onClickFiltrar(ActionEvent actionEvent) {
        SceneNavegator.navigateToInjection("main-view.fxml","Pagina Principal");
    }

    public void onClickCancelar(ActionEvent actionEvent) {
        SceneNavegator.navigateToInjection("main-view.fxml","Pagina Principal");
    }
}
