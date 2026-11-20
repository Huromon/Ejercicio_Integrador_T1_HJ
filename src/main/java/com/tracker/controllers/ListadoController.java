package com.tracker.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la vista principal con el listado de candidaturas.
 */
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

    /**
     * Registra un listener que guarda en el contexto la candidatura seleccionada en la tabla.
     *
     * @param location  ubicación del archivo FXML.
     * @param resources recursos de internacionalización de la vista.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tCandidaturas.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection)->{
            AppContext.getInstance().put("equipo",newSelection);
        });
    }

    /**
     * Elimina la candidatura seleccionada en la tabla.
     *
     * @param actionEvent evento producido al pulsar el botón.
     */
    @javafx.fxml.FXML
    public void onClickEliminar(ActionEvent actionEvent) {
    }

    /**
     * Abre la vista de filtrado de candidaturas.
     *
     * @param actionEvent evento producido al pulsar el botón.
     */
    @javafx.fxml.FXML
    public void onClickFiltrar(ActionEvent actionEvent) {
        SceneNavegator.navigateToInjection("filtro-view.fxml","Pagina principal");
    }

    /**
     * Quita los filtros aplicados al listado de candidaturas.
     *
     * @param actionEvent evento producido al pulsar el botón.
     */
    @javafx.fxml.FXML
    public void onClickQuitarFiltro(ActionEvent actionEvent) {

    }

    /**
     * Abre la vista para añadir una nueva candidatura.
     *
     * @param actionEvent evento producido al pulsar el botón.
     */
    @javafx.fxml.FXML
    public void onClickNueva(ActionEvent actionEvent) {
        SceneNavegator.navigateToInjection("nuevo-view.fxml","Pagina principal");
    }
}
