/**
 * Módulo de la aplicación de seguimiento de candidaturas laborales v2.0.
 */
module EjercicioIntegrador {
    requires java.logging;
    requires java.sql;
    requires java.naming;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires mysql.connector.j;
    requires static lombok;

    opens com.tracker.presentation;
    exports com.tracker.presentation to javafx.graphics;

    opens com.tracker.controllers;
    exports com.tracker.controllers to javafx.fxml;
    exports com.tracker.common to javafx.fxml;
    opens com.tracker.common;
}
