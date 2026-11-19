module EjercicioIntegrador {
    requires java.logging;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.naming;
    requires java.sql;

    opens com.tracker.presentation;
    exports com.tracker.presentation to fxml;
    opens com.tracker.Controllers;
    exports com.tracker.Controllers to fmxl;


}