package com.tracker.controllers;

import com.tracker.business.EntrevistaServiceImpl;
import com.tracker.business.OfertaService;
import com.tracker.business.OfertaServiceImpl;
import com.tracker.common.AppContext;
import com.tracker.common.SceneNavegator;
import com.tracker.data.entity.Entrevista;
import com.tracker.data.entity.EntrevistaDetalle;
import com.tracker.data.entity.Estado;
import com.tracker.data.entity.Oferta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    if (AppContext.getInstance().get("selecionado") != null) {
        Oferta oferta = (Oferta) AppContext.getInstance().get("selecionado");
        txtEmpresa.setText(oferta.getIdEmpresa().toString());
        txtFechaPostulacion.setText(oferta.getFechaPostulacion().toString());
        txtID.setText(oferta.getId().toString());
        txtNotas.setText((oferta.getNotas()!=null)?oferta.getNotas().toString():"sin notas");
        txtSalario.setText((oferta.getSalario()!=null)?oferta.getSalario().toString() : "No Definido" );
        txtTitulo.setText(oferta.getTitulo());
        txtDescripcion.setText((oferta.getDescripcion()!=null)?oferta.getDescripcion(): "Ninguna descripcion");
        radEntrevista.setSelected(false);
        radPendiennte.setSelected(false);
        radRechazada.setSelected(false);
        radAceptada.setSelected(false);
        AppContext.getInstance().get("entrevistaService");
        listEntrevistas.getItems().addAll(((EntrevistaServiceImpl)AppContext.getInstance().get("entrevistaService")).listarPorOferta(oferta.getId()));
        switch (oferta.getEstado().toString()) {
            case "ACEPTADA":
                radAceptada.setSelected(true);

                radEntrevista.setSelected(false);
                radPendiennte.setSelected(false);
                radRechazada.setSelected(false);

                break;
                case "PENDIENTE":
                    radPendiennte.setSelected(true);
                    break;
                    case "ENTREVISTA":
                        radEntrevista.setSelected(true);
                        break;
                        case "RECHAZADA":
                            radRechazada.setSelected(true);
                            break;

        }



    }
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
        OfertaServiceImpl ofertaService = (OfertaServiceImpl) AppContext.getInstance().get("ofertasevices");
        if(ofertaService != null) {
        String estado =((Oferta) AppContext.getInstance().get("selecionado")).getEstado().toString();
        if(radPendiennte.isSelected()) {
            if((estado.equalsIgnoreCase("PENDIENTE"))){
                ningunCambio();
            }else{
                ofertaService.cambiarEstado(((Oferta) AppContext.getInstance().get("selecionado") ).getId(),Estado.PENDIENTE);
                confirmarModificacion();
            }
        }else if(radEntrevista.isSelected()) {
            if((estado.equalsIgnoreCase("ENTREVISTA"))) {
                ningunCambio();
            }else{
                ofertaService.cambiarEstado(((Oferta) AppContext.getInstance().get("selecionado") ).getId(),Estado.ENTREVISTA);
                confirmarModificacion();
            }
        }else if(radAceptada.isSelected()) {
            if((estado.equalsIgnoreCase("ACEPTADA")) ) {
                ningunCambio();
            }else{
                ofertaService.cambiarEstado(((Oferta) AppContext.getInstance().get("selecionado") ).getId(),Estado.ACEPTADA);
                confirmarModificacion();
            }
        }else if(radRechazada.isSelected()) {
            if ((estado.equalsIgnoreCase("RECHAZADA"))) {
                ningunCambio();
            } else {
                ofertaService.cambiarEstado(((Oferta) AppContext.getInstance().get("selecionado")).getId(), Estado.RECHAZADA);
                confirmarModificacion();
            }
        }
        }


    }

    private static void ningunCambio() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText("Alert");
        alert.setContentText("No se ha realizado ningun cambio");
        alert.showAndWait();
    }

    private static void confirmarModificacion() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("aviso");
        alert.setHeaderText(null);
        alert.setContentText("se a modificar el estado");
        alert.showAndWait();
    }

    @FXML
    void onClickVolver(ActionEvent event) {
        SceneNavegator.navigateToInjection("main-view.fxml","Pagina Principal");
    }
}
