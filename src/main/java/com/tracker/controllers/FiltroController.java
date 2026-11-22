package com.tracker.controllers;

import com.tracker.business.OfertaService;
import com.tracker.common.AppContext;
import com.tracker.common.ContextKeys;
import com.tracker.common.SceneNavegator;
import com.tracker.data.entity.Estado;
import com.tracker.data.entity.FiltroOferta;
import com.tracker.data.entity.Oferta;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
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
            if (dateFechaMin.getValue() != null) {
                if (dateFechaMax.getValue() != null
                        && dateFechaMin.getValue().isAfter(dateFechaMax.getValue())) {
                    dateFechaMax.setValue(null);
                } else {
                    dateFechaMax.setDisable(false);
                }
            } else {
                dateFechaMax.setDisable(true);
                dateFechaMax.setValue(null);
            }
        });
        dateFechaMax.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (dateFechaMax.getValue() != null && dateFechaMin.getValue() != null
                    && dateFechaMin.getValue().isAfter(dateFechaMax.getValue())) {
                dateFechaMax.setValue(dateFechaMin.getValue());
            }
        });
        inpSalarioMin.textProperty().addListener((observable, oldValue, newValue) -> {
            configurarSoloDecimales(inpSalarioMin);
            if (inpSalarioMin.getText() != null && !inpSalarioMin.getText().isEmpty()) {
                if (inpSalarioMax.getText() == null || inpSalarioMax.getText().isEmpty()) {
                    inpSalarioMax.setDisable(false);
                } else {
                    Double comp = Double.parseDouble(inpSalarioMin.getText());
                    Double comp2 = Double.parseDouble(inpSalarioMax.getText());
                    if (comp > comp2) {
                        inpSalarioMax.clear();
                    }
                }
            } else {
                inpSalarioMax.setDisable(true);
                inpSalarioMax.clear();
            }
        });
        inpSalarioMax.textProperty().addListener((observable, oldValue, newValue) -> {
            if (inpSalarioMax.isFocused()) {
                configurarSoloDecimales(inpSalarioMax);
            }
        });
        inpSalarioMax.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && inpSalarioMax.getText() != null && !inpSalarioMax.getText().isEmpty()
                    && inpSalarioMin.getText() != null && !inpSalarioMin.getText().isEmpty()) {
                Double comp = Double.parseDouble(inpSalarioMin.getText());
                Double comp2 = Double.parseDouble(inpSalarioMax.getText());
                if (comp > comp2) {
                    inpSalarioMax.clear();
                }
            }
        });
    }

    /**
     * Configura un campo de texto para admitir solo decimales con dos cifras.
     *
     * @param textField campo que se va a configurar.
     */
    private void configurarSoloDecimales(TextField textField) {
        textField.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
            String nuevoTexto = change.getControlNewText();
            if (nuevoTexto.matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));
    }

    /**
     * Aplica los criterios de la vista, guarda el resultado y vuelve al listado.
     *
     * @param actionEvent evento producido al pulsar el botón.
     */
    @FXML
    public void onClickFiltrar(ActionEvent actionEvent) {
        String empresa = text(field(textfieldEmpresa));
        Estado estado = choiceboxEstado.getValue() == null
                || choiceboxEstado.getValue().equals("Cualquiera")
                ? null : Estado.desdeTexto(choiceboxEstado.getValue());
        BigDecimal sMin = decimal(inpSalarioMin.getText());
        BigDecimal sMax = decimal(inpSalarioMax.getText());

        FiltroOferta filtro = new FiltroOferta(
                empresa, estado, sMin, sMax,
                dateFechaMin.getValue(), dateFechaMax.getValue(),
                text(field(textfieldTitulo)));

        OfertaService service = (OfertaService) AppContext.getInstance().get(ContextKeys.OFERTA_SERVICE);
        List<Oferta> resultados = service.filtrar(filtro);

        AppContext.getInstance().put(ContextKeys.OFERTAS_FILTRADAS, resultados);
        AppContext.getInstance().put(ContextKeys.FILTRA, true);
        SceneNavegator.navigateToInjection("main-view.fxml", "Página Principal");
    }

    /**
     * Cancela el filtrado, limpia los resultados y vuelve al listado.
     *
     * @param actionEvent evento producido al pulsar el botón.
     */
    @FXML
    public void onClickCancelar(ActionEvent actionEvent) {
        AppContext.getInstance().put(ContextKeys.FILTRA, false);
        AppContext.getInstance().remove(ContextKeys.OFERTAS_FILTRADAS);
        SceneNavegator.navigateToInjection("main-view.fxml", "Página Principal");
    }

    /**
     * Devuelve el texto de un campo, o {@code null} si no hay campo.
     *
     * @param campo campo del que se quiere leer el texto.
     * @return el texto del campo, o {@code null} si el campo es nulo.
     */
    private String field(TextField campo) {
        return campo == null ? null : campo.getText();
    }

    /**
     * Normaliza un texto de filtro devolviendo {@code null} cuando está vacío.
     *
     * @param valor texto a normalizar.
     * @return el texto recortado, o {@code null} si está vacío.
     */
    private String text(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    /**
     * Convierte un texto a decimal, devolviendo {@code null} si no es válido.
     *
     * @param valor texto a convertir.
     * @return el número decimal, o {@code null} si está vacío o no es válido.
     */
    private BigDecimal decimal(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
