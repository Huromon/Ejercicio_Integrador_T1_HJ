package com.tracker.controllers;

import com.tracker.common.SceneNavegator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la vista con el detalle de una candidatura.
 */
public class DetallesController implements Initializable {

    /**
     * Inicializa los componentes de la vista una vez cargada.
     *
     * @param location  ubicación del archivo FXML.
     * @param resources recursos de internacionalización de la vista.
     */


    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnVolver;

    @FXML
    private ListView<?> listEntrevistas;

    @FXML
    private TextFlow textFlowDescripcion;

    @FXML
    private Label txtEmpresa;

    @FXML
    private RadioButton radAceptada;

    @FXML
    private RadioButton radEntrevista;

    @FXML
    private RadioButton radPendiennte;

    @FXML
    private RadioButton radRechazada;

    @FXML
    private Label txtFechaPostulacion;

    @FXML
    private Label txtID;

    @FXML
    private Label txtNotas;

    @FXML
    private Label txtSalario;

    @FXML
    private Label txtTitulo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void onAceptada(ActionEvent event) {
        radAceptada.setSelected(true);
        radPendiennte.setSelected(false);
        radRechazada.setSelected(false);
        radEntrevista.setSelected(false);
    }



    @FXML
    void onEntrevista(ActionEvent event) {
        radAceptada.setSelected(false);
        radPendiennte.setSelected(false);
        radRechazada.setSelected(false);
        radEntrevista.setSelected(true);
    }

    @FXML
    void onPendiente(ActionEvent event) {
        radAceptada.setSelected(false);
        radPendiennte.setSelected(true);
        radRechazada.setSelected(false);
        radEntrevista.setSelected(false);
    }

    @FXML
    void onRechazada(ActionEvent event) {
        radAceptada.setSelected(false);
        radPendiennte.setSelected(false);
        radRechazada.setSelected(true);
        radEntrevista.setSelected(false);
    }



    @FXML
    void onClickEliminar(ActionEvent event) {
        SceneNavegator.navigateToInjection("main-view.fxml","Pagina Principal");
    }

    @FXML
    void onClickModificar(ActionEvent event) {
        SceneNavegator.navigateToInjection("main-view.fxml","Pagina Principal");
    }

    @FXML
    void onClickVolver(ActionEvent event) {
        SceneNavegator.navigateToInjection("main-view.fxml","Pagina Principal");
    }
}
