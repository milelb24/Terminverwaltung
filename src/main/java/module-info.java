module at.htlkaindorf.terminverwaltung {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.htlkaindorf.terminverwaltung to javafx.fxml;
    exports at.htlkaindorf.terminverwaltung;
}