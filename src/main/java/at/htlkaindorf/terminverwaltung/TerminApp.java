package at.htlkaindorf.terminverwaltung;

import at.htlkaindorf.terminverwaltung.export.TerminExporter;
import at.htlkaindorf.terminverwaltung.model.Termin;
import at.htlkaindorf.terminverwaltung.persistence.TerminSpeicher;
import at.htlkaindorf.terminverwaltung.service.TerminManager;
import at.htlkaindorf.terminverwaltung.ui.TerminController;
import at.htlkaindorf.terminverwaltung.ui.TerminView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TerminApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        TerminSpeicher speicher = new TerminSpeicher();
        List<Termin> gespeicherteTermine = speicher.ladeTermine();

        TerminManager manager = new TerminManager(gespeicherteTermine);

        if (manager.getAlleTermine().isEmpty()) {
            manager.erstelleTermin(new Termin(
                    "Mathe Schularbeit",
                    LocalDate.now().plusDays(2),
                    LocalTime.of(8, 50),
                    LocalTime.of(10, 25),
                    "Schule",
                    "Vorbereitung Kapitel 4"
            ));
            manager.erstelleTermin(new Termin(
                    "Fußballtraining",
                    LocalDate.now().plusDays(4),
                    LocalTime.of(17, 0),
                    LocalTime.of(19, 0),
                    "Sportplatz",
                    "Neue Schuhe mitnehmen"
            ));
            manager.erstelleTermin(new Termin(
                    "Projektbesprechung",
                    LocalDate.now().plusDays(1),
                    LocalTime.of(14, 0),
                    LocalTime.of(15, 0),
                    "Online",
                    "JavaFX Oberfläche besprechen"
            ));

            try {
                speicher.speichereTermine(manager.getAlleTermine());
            } catch (IOException e) {
                System.err.println("Beispieldaten konnten nicht gespeichert werden: " + e.getMessage());
            }
        }

        TerminView view = new TerminView();
        TerminExporter exporter = new TerminExporter();
        new TerminController(manager, speicher, exporter, view, primaryStage);

        primaryStage.setTitle("Terminverwaltung (Immutable Pattern)");
        primaryStage.setScene(new Scene(view, 1200, 750));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}