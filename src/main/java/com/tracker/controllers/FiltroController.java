package com.tracker.controllers;

import com.tracker.business.OfertaServiceImpl;
import com.tracker.common.AppContext;
import com.tracker.common.SceneNavegator;
import com.tracker.data.entity.FiltroOferta;
import com.tracker.data.entity.Oferta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
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
    private ChoiceBox<String> choiceboxEstado;

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
        dateFechaMax.setDisable(true);
        inpSalarioMax.setDisable(true);
        choiceboxEstado.getItems().addAll(
                "Cualquiera",
                "Pendiente",
                "Entrevista",
                "Aceptada",
                "Rechazada"
        );
        choiceboxEstado.getSelectionModel().selectFirst();
      dateFechaMin.valueProperty().addListener((observable, oldValue, newValue) -> {
          if(dateFechaMin.getValue() != null) {
              if(dateFechaMax.getValue() != null) {
                  if(dateFechaMin.getValue().isAfter(dateFechaMax.getValue())){
                      dateFechaMax.setValue(null);
                  }
              }else{
                  dateFechaMax.setDisable(false);
              }


          }else if(dateFechaMin.getValue() == null ) {
              dateFechaMax.setDisable(true);
              dateFechaMax.setValue(null);
          }
      });
        dateFechaMax.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(dateFechaMax.getValue() != null) {
                if(dateFechaMin.getValue().isAfter(dateFechaMax.getValue()) ) {
                    dateFechaMax.setValue(dateFechaMin.getValue());
                }
            }
        });
        inpSalarioMin.textProperty().addListener((observable, oldValue, newValue) -> {
        configurarSoloDecimales(inpSalarioMax);
        if(inpSalarioMin.getText() != null && !inpSalarioMin.getText().isEmpty()) {
            if(inpSalarioMax.getText() == null || inpSalarioMax.getText().isEmpty()) {
                inpSalarioMax.setDisable(false);
            }else{
             Double comp = Double.parseDouble(inpSalarioMin.getText());
             Double comp2 = Double.parseDouble(inpSalarioMax.getText());
             if(comp > comp2) {
                 inpSalarioMax.clear();
             }
            }

        }else{
            inpSalarioMax.setDisable(true);
            inpSalarioMax.clear();
        }
        });
        inpSalarioMax.textProperty().addListener((observable, oldValue, newValue) -> {
            if(inpSalarioMax.isFocused()) {
                configurarSoloDecimales(inpSalarioMax);
            }

        });
        inpSalarioMax.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue ) {
                if(inpSalarioMax.getText() != null && !inpSalarioMax.getText().isEmpty()) {
                    Double comp = Double.parseDouble(inpSalarioMin.getText());
                    Double comp2 = Double.parseDouble(inpSalarioMax.getText());
                    if(comp > comp2) {
                        inpSalarioMax.clear();
                    }
                }

            }
        });



    }
    private void configurarSoloDecimales(TextField textField) {
        textField.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
            String nuevoTexto = change.getControlNewText();

            if (nuevoTexto.matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));
    }
    public void onClickFiltrar(ActionEvent actionEvent) {
        String estadoSeleccionado = choiceboxEstado.getValue();
        AppContext.getInstance().put("filtra", true);
        OfertaServiceImpl ofertaService = (OfertaServiceImpl) AppContext.getInstance().get("ofertasevices");
        if(estadoSeleccionado.equals("Cualquiera")) {

        }




     //   List<Oferta> filtrada = ofertaService.filtrar();
        SceneNavegator.navigateToInjection("main-view.fxml","Pagina Principal");
      //  AppContext.getInstance().put("filtrado", AppContext.getInstance().get("ofertase"));
    }

    public void onClickCancelar(ActionEvent actionEvent) {
        AppContext.getInstance().put("filtra", false);

        SceneNavegator.navigateToInjection("main-view.fxml","Pagina Principal");
    }
}
