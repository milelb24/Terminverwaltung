package at.htlkaindorf.terminverwaltung.ui;

import at.htlkaindorf.terminverwaltung.export.TerminExporter;
import at.htlkaindorf.terminverwaltung.model.Termin;
import at.htlkaindorf.terminverwaltung.persistence.TerminSpeicher;
import at.htlkaindorf.terminverwaltung.service.TerminManager;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TerminController {
    private final TerminManager manager;
    private final TerminSpeicher speicher;
    private final TerminExporter exporter;
    private final TerminView view;
    private final Stage stage;

    public TerminController(TerminManager manager, TerminSpeicher speicher,
                            TerminExporter exporter, TerminView view, Stage stage) {
        this.manager = manager;
        this.speicher = speicher;
        this.exporter = exporter;
        this.view = view;
        this.stage = stage;

        registriereAktionen();
        aktualisiereTabelle(manager.getAlleTermine());
    }

    private void registriereAktionen() {
        view.getBtnErstellen().setOnAction(event -> erstelleTermin());
        view.getBtnAendern().setOnAction(event -> aktualisiereTermin());
        view.getBtnLoeschen().setOnAction(event -> loescheTermin());
        view.getBtnLeeren().setOnAction(event -> felderLeeren());
        view.getBtnSuchen().setOnAction(event -> sucheTermine());
        view.getBtnFilter().setOnAction(event -> filterNachDatum());
        view.getBtnAlle().setOnAction(event -> zeigeAlleTermine());
        view.getBtnExport().setOnAction(event -> speichereUndExportiere());

        view.getCbZeigeId().setOnAction(event ->
                view.getTabelle().setIdSichtbar(view.getCbZeigeId().isSelected()));

        view.getTabelle().getSelectionModel().selectedItemProperty().addListener(
                (observable, alterTermin, neuerTermin) -> {
                    if (neuerTermin != null) {
                        view.getFormular().fuelleMit(neuerTermin);
                    }
                });
    }

    private void erstelleTermin() {
        try {
            Termin termin = view.getFormular().erstelleTermin();
            manager.erstelleTermin(termin);
            aktualisiereTabelle(manager.getAlleTermine());
            felderLeeren();
        } catch (RuntimeException e) {
            zeigeMeldung("Fehler", "Bitte überprüfe deine Eingaben.\n"
                    + "Titel und Datum müssen angegeben werden.\n"
                    + "Zeitformat: HH:MM. Das Ende muss nach dem Beginn liegen.");
        }
    }

    private void aktualisiereTermin() {
        Termin ausgewaehlterTermin = view.getTabelle().getAusgewaehlterTermin();
        if (ausgewaehlterTermin == null) {
            zeigeMeldung("Achtung", "Bitte zuerst einen Termin aus der Tabelle auswählen.");
            return;
        }

        try {
            Termin neueVersion = view.getFormular().erstelleNeueVersion(ausgewaehlterTermin);
            manager.aktualisiereTermin(ausgewaehlterTermin.getId(), neueVersion);
            aktualisiereTabelle(manager.getAlleTermine());
            view.getTabelle().getSelectionModel().select(neueVersion);
        } catch (RuntimeException e) {
            zeigeMeldung("Fehler", "Der Termin konnte nicht geändert werden.\n"
                    + "Bitte überprüfe das Zeitformat und wähle die letzte Version des Termins aus.");
        }
    }

    private void loescheTermin() {
        Termin ausgewaehlterTermin = view.getTabelle().getAusgewaehlterTermin();
        if (ausgewaehlterTermin == null) {
            zeigeMeldung("Achtung", "Bitte zuerst einen Termin zum Löschen auswählen.");
            return;
        }

        manager.loescheTermin(ausgewaehlterTermin.getId(), ausgewaehlterTermin.getVersion());
        aktualisiereTabelle(manager.getAlleTermine());
        felderLeeren();
    }

    private void sucheTermine() {
        aktualisiereTabelle(manager.sucheTermine(view.getTfSuche().getText()));
    }

    private void filterNachDatum() {
        try {
            LocalDate datum = LocalDate.parse(view.getTfFilterDatum().getText().trim());
            aktualisiereTabelle(manager.filterNachDatum(datum));
        } catch (RuntimeException e) {
            zeigeMeldung("Fehler", "Ungültiges Datumsformat. Bitte YYYY-MM-DD verwenden.");
        }
    }

    private void zeigeAlleTermine() {
        view.getTfSuche().clear();
        view.getTfFilterDatum().clear();
        aktualisiereTabelle(manager.getAlleTermine());
    }

    private void speichereUndExportiere() {
        try {
            // Die interne Speicherdatei wird auch dann aktualisiert, wenn der Export abgebrochen wird.
            speicher.speichereTermine(manager.getAlleTermine());
        } catch (IOException e) {
            zeigeMeldung("Fehler", "Die Termine konnten nicht gespeichert werden: " + e.getMessage());
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Termine exportieren");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV-Datei (*.csv)", "*.csv"),
                new FileChooser.ExtensionFilter("Textdatei (*.txt)", "*.txt")
        );
        fileChooser.setInitialFileName("termine.csv");

        File datei = fileChooser.showSaveDialog(stage);
        if (datei == null) {
            return;
        }

        try {
            String dateiname = datei.getName().toLowerCase();
            if (dateiname.endsWith(".txt")) {
                exporter.exportiereAlsTxt(datei, manager.getAlleTermine());
            } else {
                if (!dateiname.endsWith(".csv")) {
                    datei = new File(datei.getAbsolutePath() + ".csv");
                }
                exporter.exportiereAlsCsv(datei, manager.getAlleTermine());
            }

            String zeit = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            view.getLblStatus().setText(String.format("Zuletzt exportiert um %s (%d Terminversionen)",
                    zeit, manager.getAlleTermine().size()));
            zeigeMeldung("Erfolg", "Erfolgreich exportiert nach:\n" + datei.getAbsolutePath());
        } catch (IOException e) {
            zeigeMeldung("Fehler", "Export fehlgeschlagen: " + e.getMessage());
        }
    }

    private void aktualisiereTabelle(java.util.List<Termin> termine) {
        view.getTabelle().setTermine(termine);
    }

    private void felderLeeren() {
        view.getTabelle().getSelectionModel().clearSelection();
        view.getFormular().leeren();
    }

    private void zeigeMeldung(String titel, String nachricht) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(nachricht);
        alert.showAndWait();
    }
}