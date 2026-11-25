module at.htlkaindorf.terminverwaltung {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.htlkaindorf.terminverwaltung to javafx.fxml;
    exports at.htlkaindorf.terminverwaltung;
    exports at.htlkaindorf.terminverwaltung.model;
    opens at.htlkaindorf.terminverwaltung.model to javafx.fxml;
    exports at.htlkaindorf.terminverwaltung.service;
    opens at.htlkaindorf.terminverwaltung.service to javafx.fxml;
}