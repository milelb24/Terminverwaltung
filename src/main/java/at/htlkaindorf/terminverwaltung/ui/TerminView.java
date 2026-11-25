package at.htlkaindorf.terminverwaltung.ui;

import at.htlkaindorf.terminverwaltung.ui.components.TerminFormular;
import at.htlkaindorf.terminverwaltung.ui.components.TerminTabelle;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TerminView extends BorderPane {
    private final TerminFormular formular;
    private final TerminTabelle tabelle;

    private final Button btnErstellen;
    private final Button btnAendern;
    private final Button btnLoeschen;
    private final Button btnLeeren;
    private final Button btnSuchen;
    private final Button btnFilter;
    private final Button btnAlle;
    private final Button btnExport;

    private final TextField tfSuche;
    private final TextField tfFilterDatum;
    private final CheckBox cbZeigeId;
    private final Label lblStatus;

    public TerminView() {
        formular = new TerminFormular();
        tabelle = new TerminTabelle();

        btnErstellen = new Button("Neuen Termin erstellen");
        btnAendern = new Button("Ändern (Neue Version)");
        btnLoeschen = new Button("Löschen");
        btnLeeren = new Button("Felder leeren");

        HBox aktionsBox = new HBox(10, btnErstellen, btnAendern, btnLoeschen, btnLeeren);
        VBox oben = new VBox(8, formular, aktionsBox);
        oben.setPadding(new Insets(10));
        setTop(oben);

        setCenter(tabelle);

        tfSuche = new TextField();
        tfSuche.setPromptText("Suchbegriff");
        btnSuchen = new Button("Suchen");

        tfFilterDatum = new TextField();
        tfFilterDatum.setPromptText("YYYY-MM-DD");
        tfFilterDatum.setPrefWidth(115);
        btnFilter = new Button("Datum filtern");

        btnAlle = new Button("Alle anzeigen");
        btnExport = new Button("Speichern & Exportieren");
        cbZeigeId = new CheckBox("ID anzeigen");
        cbZeigeId.setSelected(true);

        HBox unten = new HBox(8,
                new Label("Suche:"), tfSuche, btnSuchen,
                new Label("Datum:"), tfFilterDatum, btnFilter,
                btnAlle, btnExport, cbZeigeId);
        unten.setPadding(new Insets(10));
        HBox.setHgrow(tfSuche, Priority.ALWAYS);

        lblStatus = new Label("Status: Noch nicht exportiert");
        VBox fussbereich = new VBox(5, unten, lblStatus);
        fussbereich.setPadding(new Insets(5));
        setBottom(fussbereich);
    }

    public TerminFormular getFormular() { return formular; }
    public TerminTabelle getTabelle() { return tabelle; }
    public Button getBtnErstellen() { return btnErstellen; }
    public Button getBtnAendern() { return btnAendern; }
    public Button getBtnLoeschen() { return btnLoeschen; }
    public Button getBtnLeeren() { return btnLeeren; }
    public Button getBtnSuchen() { return btnSuchen; }
    public Button getBtnFilter() { return btnFilter; }
    public Button getBtnAlle() { return btnAlle; }
    public Button getBtnExport() { return btnExport; }
    public TextField getTfSuche() { return tfSuche; }
    public TextField getTfFilterDatum() { return tfFilterDatum; }
    public CheckBox getCbZeigeId() { return cbZeigeId; }
    public Label getLblStatus() { return lblStatus; }
}