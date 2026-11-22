package com.tracker.controllers;

import com.tracker.business.EmpresaServiceImpl;
import com.tracker.business.OfertaServiceImpl;
import com.tracker.common.AppContext;
import com.tracker.common.ContextKeys;
import com.tracker.common.SceneNavegator;
import com.tracker.data.entity.Empresa;
import com.tracker.data.entity.Estado;
import com.tracker.data.entity.Oferta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
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
    private ComboBox<String> inpEmpresa;

    @FXML
    private TextField inpEnlace;

    @FXML
    private TextField inpNotas;

    @FXML
    private TextField inpSalario;

    @FXML
    private TextField inpTitulo;
    private OfertaServiceImpl oo;
    public AñadirController() {
        this.oo = (OfertaServiceImpl) AppContext.getInstance().get(ContextKeys.OFERTA_SERVICE);
    }

    /**
     * Inicializa los componentes de la vista una vez cargada.
     *
     * @param location  ubicación del archivo FXML.
     * @param resources recursos de internacionalización de la vista.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnAñadir.setDisable(true);
        choiceboxEstado.getItems().addAll(
                "Pendiente",
                "Entrevista",
                "Aceptada",
                "Rechazada"
        );
        EmpresaServiceImpl empresaService = (EmpresaServiceImpl) AppContext.getInstance().get(ContextKeys.EMPRESA_SERVICE);
        inpEmpresa.setEditable(true);
        for(Empresa e : empresaService.listarTodas()) {
            inpEmpresa.getItems().add(e.getNombre());
        }
        choiceboxEstado.getSelectionModel().selectFirst();
        inpEmpresa.getSelectionModel().selectFirst();
        inpEmpresa.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            comprobar();
        });
        dateFechaPostulacion.valueProperty().addListener((observable, oldValue, newValue) -> {
        comprobar();
        });

        inpTitulo.textProperty().addListener((observable, oldValue, newValue) -> {
        comprobar();
        });
        inpSalario.textProperty().addListener((observable, oldValue, newValue) -> {
            if(inpSalario.isFocused()) {
                configurarSoloDecimales(inpSalario);
            }

            comprobar();
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
public void comprobardecimal() {
    if(inpSalario.getText()!=null){
        if(inpSalario.getText().endsWith(".")) {
            inpSalario.setText(inpSalario.getText().concat("00"));
        }else if(inpSalario.getText().endsWith(".?")) {
            inpSalario.setText(inpSalario.getText().concat("0"));
        }if(!inpSalario.getText().endsWith(".??")) {
            inpSalario.setText(inpSalario.getText().concat(".00"));
        }
        BigDecimal salario = new BigDecimal(inpSalario.getText());
        System.out.println(salario);
    }

}
    public void onClickAñadir(ActionEvent actionEvent) {

        OfertaServiceImpl ofertaService = (OfertaServiceImpl) AppContext.getInstance().get(ContextKeys.OFERTA_SERVICE);
        EmpresaServiceImpl empresaService = (EmpresaServiceImpl) AppContext.getInstance().get(ContextKeys.EMPRESA_SERVICE);
        String nombreEmpresa = inpEmpresa.getEditor().getText().trim();
        if (nombreEmpresa.isEmpty()) {
            return;
        }
        Empresa empresa = empresaService.buscarPorNombre(nombreEmpresa).orElseGet(() ->
                empresaService.crear(nombreEmpresa, null, null));
        Integer id_Empresa = empresa.getId();
        if(id_Empresa!=null && ofertaService != null) {
            comprobardecimal();

            Oferta o = new Oferta ();
            o.setIdEmpresa(id_Empresa);
            o.setTitulo(inpTitulo.getText());
            o.setDescripcion((inpDesc.getText().isEmpty()) ? "" : inpDesc.getText());
            o.setEstado(Estado.valueOf(choiceboxEstado.getValue().toUpperCase()));
            o.setFechaPostulacion(dateFechaPostulacion.getValue());
            o.setEnlace( (inpEnlace.getText().isEmpty()) ? "" : inpEnlace.getText());
            o.setSalario((!inpSalario.getText().isEmpty())?(new BigDecimal(inpSalario.getText())):BigDecimal.ZERO);
            o.setNotas((inpNotas.getText().isEmpty())? "" : inpNotas.getText());
            oo.crear(
                    o.getIdEmpresa(),
                    o.getTitulo(),
                    o.getDescripcion(),
                    o.getEstado(),
                    o.getFechaPostulacion(),
                    o.getEnlace(),
                    o.getSalario(),
                    o.getNotas()
            );



            SceneNavegator.navigateToInjection("main-view.fxml","Pagina Principal");
        }


    }

    public void onClickCancelar(ActionEvent actionEvent) {
        SceneNavegator.navigateToInjection("main-view.fxml","Pagina Principal");
    }
    public void comprobar() {
        String nombreEmpresa = inpEmpresa.getEditor().getText().trim();
        if(inpTitulo.getText().isEmpty() || dateFechaPostulacion.getValue() == null || nombreEmpresa.isEmpty()) {
            btnAñadir.setDisable(true);
        }else{
            btnAñadir.setDisable(false);
        }
    }



}
