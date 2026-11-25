package at.htlkaindorf.terminverwaltung;

import at.htlkaindorf.terminverwaltung.model.Termin;
import at.htlkaindorf.terminverwaltung.service.TerminManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TerminApp extends Application {
    private TerminManager manager;
    private TableView<Termin> table;
    private ObservableList<Termin> tableData;

    private TextField tfTitel, tfBeginn, tfEnde, tfOrt, tfBeschreibung;
    private DatePicker dpDatum;

    private TextField tfSuche, tfFilterDatum;
    private Label lblStatus;
    private CheckBox cbZeigeId;

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) {
        manager = new TerminManager();

        if (manager.getAlleTermine().isEmpty()) {
            manager.erstelleTermin(new Termin("Mathe Schularbeit", LocalDate.now().plusDays(2), LocalTime.of(8, 50), LocalTime.of(10, 25), "Schule", "Vorbereitung Kapitel 4"));
            manager.erstelleTermin(new Termin("Fußballtraining", LocalDate.now().plusDays(4), LocalTime.of(17, 0), LocalTime.of(19, 0), "Sportplatz", "Neue Schuhe mitnehmen"));
            manager.erstelleTermin(new Termin("Projektbesprechung", LocalDate.now().plusDays(1), LocalTime.of(14, 0), LocalTime.of(15, 0), "Online", "JavaFX Oberfläche besprechen"));
            manager.speichereTermine();
        }

        tableData = FXCollections.observableArrayList(manager.getAlleTermine());

        primaryStage.setTitle("Terminverwaltung (JavaFX - Immutable Pattern)");

        tfTitel = new TextField();
        dpDatum = new DatePicker();
        dpDatum.setValue(LocalDate.now());
        tfBeginn = new TextField();
        tfEnde = new TextField();
        tfOrt = new TextField();
        tfBeschreibung = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));

        grid.add(new Label("Titel:"), 0, 0); grid.add(tfTitel, 1, 0);
        grid.add(new Label("Datum:"), 0, 1); grid.add(dpDatum, 1, 1);
        grid.add(new Label("Beginn (HH:MM):"), 0, 2); grid.add(tfBeginn, 1, 2);
        grid.add(new Label("Ende (HH:MM):"), 0, 3); grid.add(tfEnde, 1, 3);
        grid.add(new Label("Ort:"), 0, 4); grid.add(tfOrt, 1, 4);
        grid.add(new Label("Beschreibung:"), 0, 5); grid.add(tfBeschreibung, 1, 5);

        Button btnErstellen = new Button("Neuen Termin erstellen");
        Button btnUpdate = new Button("Ändern (Neue Version)");
        Button btnLoeschen = new Button("Löschen");
        Button btnLeeren = new Button("Felder leeren");

        HBox actionBox = new HBox(10, btnErstellen, btnUpdate, btnLoeschen, btnLeeren);
        VBox topBox = new VBox(10, grid, actionBox);
        topBox.setPadding(new Insets(10));

        table = new TableView<>();
        table.setItems(tableData);

        TableColumn<Termin, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(220);

        TableColumn<Termin, String> colTitel = new TableColumn<>("Titel");
        colTitel.setCellValueFactory(new PropertyValueFactory<>("titel"));

        TableColumn<Termin, LocalDate> colDatum = new TableColumn<>("Datum");
        colDatum.setCellValueFactory(new PropertyValueFactory<>("datum"));

        TableColumn<Termin, LocalTime> colBeginn = new TableColumn<>("Beginn");
        colBeginn.setCellValueFactory(new PropertyValueFactory<>("beginn"));

        TableColumn<Termin, LocalTime> colEnde = new TableColumn<>("Ende");
        colEnde.setCellValueFactory(new PropertyValueFactory<>("ende"));

        TableColumn<Termin, String> colOrt = new TableColumn<>("Ort");
        colOrt.setCellValueFactory(new PropertyValueFactory<>("ort"));

        TableColumn<Termin, String> colBeschreibung = new TableColumn<>("Beschreibung");
        colBeschreibung.setCellValueFactory(new PropertyValueFactory<>("beschreibung"));

        TableColumn<Termin, Integer> colVersion = new TableColumn<>("Version");
        colVersion.setCellValueFactory(new PropertyValueFactory<>("version"));

        table.getColumns().addAll(colId, colTitel, colDatum, colBeginn, colEnde, colOrt, colBeschreibung, colVersion);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                tfTitel.setText(newVal.getTitel());
                dpDatum.setValue(newVal.getDatum());
                tfBeginn.setText(newVal.getBeginn().toString());
                tfEnde.setText(newVal.getEnde().toString());
                tfOrt.setText(newVal.getOrt());
                tfBeschreibung.setText(newVal.getBeschreibung());
            }
        });

        tfSuche = new TextField();
        tfSuche.setPromptText("Suchbegriff");
        Button btnSuchen = new Button("Suchen");
        btnSuchen.setOnAction(e -> aktTabelle(manager.sucheTermine(tfSuche.getText())));

        tfFilterDatum = new TextField();
        tfFilterDatum.setPromptText("YYYY-MM-DD");
        Button btnFilter = new Button("Datum filtern");
        btnFilter.setOnAction(e -> {
            try {
                LocalDate d = LocalDate.parse(tfFilterDatum.getText().trim());
                aktTabelle(manager.filterNachDatum(d));
            } catch (Exception ex) {
                showAlert("Fehler", "Ungültiges Datumsformat! Bitte YYYY-MM-DD.");
            }
        });

        Button btnAlle = new Button("Alle anzeigen");
        btnAlle.setOnAction(e -> {
            tfSuche.clear();
            tfFilterDatum.clear();
            aktTabelle(manager.getAlleTermine());
        });

        Button btnExport = new Button("Speichern & Exportieren");
        btnExport.setOnAction(e -> speichereUndExportiere(primaryStage));

        cbZeigeId = new CheckBox("ID anzeigen");
        cbZeigeId.setSelected(true);
        cbZeigeId.setOnAction(e -> colId.setVisible(cbZeigeId.isSelected()));

        lblStatus = new Label("Status: Noch nicht exportiert");

        HBox bottomBox = new HBox(10, new Label("Suche:"), tfSuche, btnSuchen, new Label("Datum:"), tfFilterDatum, btnFilter, btnAlle, btnExport, cbZeigeId);
        bottomBox.setPadding(new Insets(10));

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBox);
        mainLayout.setCenter(table);

        VBox bottomContainer = new VBox(5, bottomBox, lblStatus);
        bottomContainer.setPadding(new Insets(5));
        mainLayout.setBottom(bottomContainer);

        btnLeeren.setOnAction(e -> felderLeeren());

        btnErstellen.setOnAction(e -> {
            try {
                Termin t = new Termin(
                        tfTitel.getText().trim(),
                        dpDatum.getValue(),
                        LocalTime.parse(tfBeginn.getText().trim()),
                        LocalTime.parse(tfEnde.getText().trim()),
                        tfOrt.getText().trim(),
                        tfBeschreibung.getText().trim()
                );
                manager.erstelleTermin(t);
                aktTabelle(manager.getAlleTermine());
                felderLeeren();
            } catch (Exception ex) {
                showAlert("Fehler", "Bitte Eingaben prüfen (Zeitformat: HH:MM, Datum ausgewählt?).");
            }
        });

        btnUpdate.setOnAction(e -> {
            Termin selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Achtung", "Bitte einen Termin aus der Tabelle auswählen!");
                return;
            }
            try {
                Termin neu = selected.updateTermin(
                        tfTitel.getText().isEmpty() ? selected.getTitel() : tfTitel.getText().trim(),
                        dpDatum.getValue() != null ? dpDatum.getValue() : selected.getDatum(),
                        tfBeginn.getText().isEmpty() ? selected.getBeginn() : LocalTime.parse(tfBeginn.getText().trim()),
                        tfEnde.getText().isEmpty() ? selected.getEnde() : LocalTime.parse(tfEnde.getText().trim()),
                        tfOrt.getText().isEmpty() ? selected.getOrt() : tfOrt.getText().trim(),
                        tfBeschreibung.getText().isEmpty() ? selected.getBeschreibung() : tfBeschreibung.getText().trim()
                );
                manager.aktualisiereTermin(selected.getId(), neu);
                aktTabelle(manager.getAlleTermine());
            } catch (Exception ex) {
                showAlert("Fehler", "Fehler bei der Aktualisierung.");
            }
        });

        btnLoeschen.setOnAction(e -> {
            Termin selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Achtung", "Bitte einen Termin zum Löschen auswählen!");
                return;
            }
            manager.loescheTermin(selected.getId(), selected.getVersion());
            aktTabelle(manager.getAlleTermine());
            felderLeeren();
        });

        Scene scene = new Scene(mainLayout, 1050, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void aktTabelle(java.util.List<Termin> liste) {
        tableData.setAll(liste);
    }

    private void felderLeeren() {
        table.getSelectionModel().clearSelection();
        tfTitel.clear();
        dpDatum.setValue(LocalDate.now());
        tfBeginn.clear();
        tfEnde.clear();
        tfOrt.clear();
        tfBeschreibung.clear();
    }

    private void speichereUndExportiere(Stage stage) {
        manager.speichereTermine();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Termine exportieren");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV-Datei (*.csv)", "*.csv"),
                new FileChooser.ExtensionFilter("Textdatei (*.txt)", "*.txt")
        );

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                if (file.getName().endsWith(".txt")) {
                    manager.exportiereAlsTxt(file);
                } else {
                    if (!file.getName().endsWith(".csv")) {
                        file = new File(file.getAbsolutePath() + ".csv");
                    }
                    manager.exportiereAlsCsv(file);
                }
                String zeit = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                lblStatus.setText(String.format("Zuletzt exportiert um %s (%d Termine)", zeit, manager.getAlleTermine().size()));
                showAlert("Erfolg", "Erfolgreich exportiert nach:\n" + file.getAbsolutePath());
            } catch (Exception ex) {
                showAlert("Fehler", "Export fehlgeschlagen: " + ex.getMessage());
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}