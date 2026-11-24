package com.tracker.controllers;

import com.tracker.business.EmpresaServiceImpl;
import com.tracker.business.EntrevistaServiceImpl;
import com.tracker.business.OfertaServiceImpl;
import com.tracker.common.AppContext;
import com.tracker.common.ContextKeys;
import com.tracker.common.SceneNavegator;
import com.tracker.data.entity.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la vista con el detalle de una candidatura.
 */
public class DetallesController implements Initializable {

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnVolver;

    @FXML
    private ListView<EntrevistaDetalle> listEntrevistas;

    @FXML
    private Label txtDescripcion;

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

    private OfertaServiceImpl oo;

    /**
     * Crea el controlador del detalle de una candidatura.
     */
    public DetallesController() {
        oo = (OfertaServiceImpl) AppContext.getInstance().get(ContextKeys.OFERTA_SERVICE);
    }

    /**
     * Inicializa los componentes de la vista una vez cargada.
     *
     * @param location  ubicación del archivo FXML.
     * @param resources recursos de internacionalización de la vista.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listEntrevistas.setCellFactory(lista -> new ListCell<>() {
            @Override
            protected void updateItem(EntrevistaDetalle item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.fecha() + " | " + item.tipo() + " | " + item.resultado());
                }
            }
        });

        if (AppContext.getInstance().get(ContextKeys.SELECCIONADO) != null) {
            Oferta oferta = (Oferta) AppContext.getInstance().get(ContextKeys.SELECCIONADO);
            EmpresaServiceImpl empresas = (EmpresaServiceImpl) AppContext.getInstance().get(ContextKeys.EMPRESA_SERVICE);

            txtEmpresa.setText(empresas.buscarPorId(oferta.getIdEmpresa()).map(Empresa::getNombre).orElse(""));
            txtFechaPostulacion.setText(oferta.getFechaPostulacion().toString());
            txtID.setText(oferta.getId().toString());
            txtNotas.setText(oferta.getNotas() != null ? oferta.getNotas() : "Sin notas");
            txtSalario.setText(oferta.getSalario() != null ? oferta.getSalario().toString() : "No definido");
            txtTitulo.setText(oferta.getTitulo());
            txtDescripcion.setText(oferta.getDescripcion() != null ? oferta.getDescripcion() : "Ninguna descripción");

            radAceptada.setSelected(false);
            radEntrevista.setSelected(false);
            radPendiennte.setSelected(false);
            radRechazada.setSelected(false);

            listEntrevistas.getItems().addAll(((EntrevistaServiceImpl) AppContext.getInstance()
                    .get(ContextKeys.ENTREVISTA_SERVICE)).listarPorOferta(oferta.getId()));

            Estado estado = oferta.getEstado();
            if (estado != null) {
                switch (estado) {
                    case ACEPTADA -> radAceptada.setSelected(true);
                    case PENDIENTE -> radPendiennte.setSelected(true);
                    case ENTREVISTA -> radEntrevista.setSelected(true);
                    case RECHAZADA -> radRechazada.setSelected(true);
                }
            }
        }
    }

    /**
     * Selecciona el estado aceptado.
     *
     * @param event evento producido al pulsar el botón.
     */
    @FXML
    void onAceptada(ActionEvent event) {
        radAceptada.setSelected(true);
        radPendiennte.setSelected(false);
        radRechazada.setSelected(false);
        radEntrevista.setSelected(false);
    }

    /**
     * Selecciona el estado entrevista.
     *
     * @param event evento producido al pulsar el botón.
     */
    @FXML
    void onEntrevista(ActionEvent event) {
        radAceptada.setSelected(false);
        radPendiennte.setSelected(false);
        radRechazada.setSelected(false);
        radEntrevista.setSelected(true);
    }

    /**
     * Selecciona el estado pendiente.
     *
     * @param event evento producido al pulsar el botón.
     */
    @FXML
    void onPendiente(ActionEvent event) {
        radAceptada.setSelected(false);
        radPendiennte.setSelected(true);
        radRechazada.setSelected(false);
        radEntrevista.setSelected(false);
    }

    /**
     * Selecciona el estado rechazado.
     *
     * @param event evento producido al pulsar el botón.
     */
    @FXML
    void onRechazada(ActionEvent event) {
        radAceptada.setSelected(false);
        radPendiennte.setSelected(false);
        radRechazada.setSelected(true);
        radEntrevista.setSelected(false);
    }

    /**
     * Pide confirmación antes de eliminar la oferta y sus entrevistas.
     *
     * @param event evento producido al pulsar el botón.
     */
    @FXML
    void onClickEliminar(ActionEvent event) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("Eliminar candidatura");
        confirmacion.setContentText("¿Se van a eliminar la oferta y sus entrevistas?");
        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            Oferta seleccionada = (Oferta) AppContext.getInstance().get(ContextKeys.SELECCIONADO);
            if (seleccionada != null) {
                oo.eliminarConDependencias(seleccionada.getId());
            }
            AppContext.getInstance().put(ContextKeys.SELECCIONADO, null);
            SceneNavegator.navigateToInjection("main-view.fxml", "Página Principal");
        }
    }

    /**
     * Aplica el estado seleccionado a la oferta.
     *
     * @param event evento producido al pulsar el botón.
     */
    @FXML
    void onClickModificar(ActionEvent event) {
        if (oo == null) {
            return;
        }
        Oferta seleccionada = (Oferta) AppContext.getInstance().get(ContextKeys.SELECCIONADO);
        if (seleccionada == null) {
            return;
        }
        Estado actual = seleccionada.getEstado();
        if (radPendiennte.isSelected()) {
            if (actual == Estado.PENDIENTE) {
                ningúnCambio();
            } else {
                oo.cambiarEstado(seleccionada.getId(), Estado.PENDIENTE);
                confirmarModificacion();
            }
        } else if (radEntrevista.isSelected()) {
            if (actual == Estado.ENTREVISTA) {
                ningúnCambio();
            } else {
                oo.cambiarEstado(seleccionada.getId(), Estado.ENTREVISTA);
                confirmarModificacion();
            }
        } else if (radAceptada.isSelected()) {
            if (actual == Estado.ACEPTADA) {
                ningúnCambio();
            } else {
                oo.cambiarEstado(seleccionada.getId(), Estado.ACEPTADA);
                confirmarModificacion();
            }
        } else if (radRechazada.isSelected()) {
            if (actual == Estado.RECHAZADA) {
                ningúnCambio();
            } else {
                oo.cambiarEstado(seleccionada.getId(), Estado.RECHAZADA);
                confirmarModificacion();
            }
        }
    }

    /**
     * Informa de que el estado seleccionado es el mismo que ya tenía la oferta.
     */
    private static void ningúnCambio() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText("No se ha realizado ningún cambio");
        alert.showAndWait();
    }

    /**
     * Informa de que el estado de la oferta se ha modificado.
     */
    private static void confirmarModificacion() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText("Se ha modificado el estado");
        alert.showAndWait();
    }

    /**
     * Vuelve al listado de candidaturas.
     *
     * @param event evento producido al pulsar el botón.
     */
    @FXML
    void onClickVolver(ActionEvent event) {
        SceneNavegator.navigateToInjection("main-view.fxml", "Página Principal");
    }
}
