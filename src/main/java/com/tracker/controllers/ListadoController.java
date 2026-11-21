package com.tracker.controllers;

import com.tracker.business.EmpresaServiceImpl;
import com.tracker.business.OfertaServiceImpl;
import com.tracker.common.AppContext;
import com.tracker.common.SceneNavegator;
import com.tracker.data.entity.Oferta;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador de la vista principal con el listado de candidaturas.
 */
public class ListadoController implements Initializable {
    @FXML
    private Button btnNueva;
    @FXML
    private TableColumn<Oferta,String> cId;
    @FXML
    private TableColumn<Oferta,String> cTitulo;
    @FXML
    private TableColumn<Oferta,String> cEmpresa;
    @FXML
    private TableColumn<Oferta,String> cEstado;
    @FXML
    private TableColumn<Oferta, String> cFechaPostulacion;
    @FXML
    private TableColumn<Oferta,String> cSalario;
    @FXML
    private TableView<Oferta> tCandidaturas;

    private Button btnFiltrar;
    @FXML
    private Button btnQuitarFiltro;

    @FXML
    private BorderPane fondo;
    private OfertaServiceImpl oo;
    private EmpresaServiceImpl oo2;
public ListadoController(){
    Boolean filt =(Boolean) AppContext.getInstance().get("filtra");
if(filt){
    this.oo = (OfertaServiceImpl) AppContext.getInstance().get("filtrado");
}else {
    this.oo = (OfertaServiceImpl) AppContext.getInstance().get("ofertasevices");
}
oo2 = (EmpresaServiceImpl) AppContext.getInstance().get("empresaService");

}
    /**
     * Registra un listener que guarda en el contexto la candidatura seleccionada en la tabla.
     *
     * @param location  ubicación del archivo FXML.
     * @param resources recursos de internacionalización de la vista.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<Oferta> listaofert = oo.listarTodas();


        configurarColumnas();

        fondo.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            Node nodoPulsado = (Node) event.getTarget();
            boolean clicDentroDeTabla = false;

            while (nodoPulsado != null) {
                if (nodoPulsado == tCandidaturas) {
                    clicDentroDeTabla = true;
                    break;
                }
                nodoPulsado = nodoPulsado.getParent();
            }

            if (!clicDentroDeTabla) {
                AppContext.getInstance().put("selecionado", null);
                tCandidaturas.getSelectionModel().clearSelection();
            }
        });
        cargarOfertasEnTabla(listaofert);
        tCandidaturas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Oferta seleccionada = tCandidaturas.getSelectionModel().getSelectedItem();
                if (seleccionada != null) {
                    AppContext.getInstance().put("selecionado", seleccionada);

                    SceneNavegator.navigateToInjection("Candidatura-view.fxml", "Detalle de Oferta");
                }
            }
        });


    }
    private void configurarColumnas() {
        cId.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getId() != null ? String.valueOf(cell.getValue().getId()) : ""
        ));

        cTitulo.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getTitulo() != null ? cell.getValue().getTitulo() : ""
        ));

        cEmpresa.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getIdEmpresa() != null ? String.valueOf(cell.getValue().getIdEmpresa()) : ""
        ));

        cEstado.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getEstado() != null ? cell.getValue().getEstado().name() : ""
        ));

        cFechaPostulacion.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getFechaPostulacion() != null ? cell.getValue().getFechaPostulacion().toString() : ""
        ));

        cSalario.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getSalario() != null ? cell.getValue().getSalario().toPlainString() : ""
        ));
    }

    public void cargarOfertasEnTabla(List<Oferta> ofertas) {
        tCandidaturas.getItems().setAll(ofertas);
    }






    /**
     * Abre la vista de filtrado de candidaturas.
     *
     * @param actionEvent evento producido al pulsar el botón.
     */
    @javafx.fxml.FXML
    public void onClickFiltrar(ActionEvent actionEvent) {
        SceneNavegator.navigateToInjection("filtro-view.fxml","Filtrar");
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
        SceneNavegator.navigateToInjection("nuevo-view.fxml","Añadir");
    }

}
