package com.tracker.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ListadoController implements Initializable {
    @javafx.fxml.FXML
    private Button btnNueva;
    @javafx.fxml.FXML
    private TableColumn cTitulo;
    @javafx.fxml.FXML
    private TableColumn cEmpresa;
    @javafx.fxml.FXML
    private TableColumn cFechaPostulacion;
    @javafx.fxml.FXML
    private Button btnEliminar;
    @javafx.fxml.FXML
    private Button btnFiltrar;
    @javafx.fxml.FXML
    private Button btnQuitarFiltro;
    @javafx.fxml.FXML
    private TableColumn cEstado;
    @javafx.fxml.FXML
    private TableView tCandidaturas;
    @javafx.fxml.FXML
    private TableColumn cSalario;
    @javafx.fxml.FXML
    private TableColumn cId;
    @javafx.fxml.FXML
    private BorderPane fondo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {



    }

    @javafx.fxml.FXML
    public void onClickEliminar(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void onClickFiltrar(ActionEvent actionEvent) {
        SceneNavegator.navigateToInjection("Filtrar-view.fxml","Pagina principal");
    }

    @javafx.fxml.FXML
    public void onClickQuitarFiltro(ActionEvent actionEvent) {

    }

    @javafx.fxml.FXML
    public void onClickNueva(ActionEvent actionEvent) {
        SceneNavegator.navigateToInjection("Añadir-view.fxml","Pagina principal");
    }
}
