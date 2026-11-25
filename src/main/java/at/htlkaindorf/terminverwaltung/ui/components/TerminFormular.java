package at.htlkaindorf.terminverwaltung.ui.components;

import at.htlkaindorf.terminverwaltung.model.Termin;
import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.LocalTime;

public class TerminFormular extends GridPane {
    private final TextField tfTitel;
    private final DatePicker dpDatum;
    private final TextField tfBeginn;
    private final TextField tfEnde;
    private final TextField tfOrt;
    private final TextField tfBeschreibung;

    public TerminFormular() {
        setHgap(10);
        setVgap(6);
        setPadding(new Insets(10));

        tfTitel = new TextField();
        tfTitel.setPromptText("Titel eingeben");

        dpDatum = new DatePicker(LocalDate.now());

        tfBeginn = new TextField();
        tfBeginn.setPromptText("HH:MM");

        tfEnde = new TextField();
        tfEnde.setPromptText("HH:MM");

        tfOrt = new TextField();
        tfOrt.setPromptText("Ort eingeben");

        tfBeschreibung = new TextField();
        tfBeschreibung.setPromptText("Beschreibung eingeben");

        add(new Label("Titel:"), 0, 0);
        add(tfTitel, 1, 0);
        add(new Label("Datum:"), 0, 1);
        add(dpDatum, 1, 1);
        add(new Label("Beginn (HH:MM):"), 0, 2);
        add(tfBeginn, 1, 2);
        add(new Label("Ende (HH:MM):"), 0, 3);
        add(tfEnde, 1, 3);
        add(new Label("Ort:"), 0, 4);
        add(tfOrt, 1, 4);
        add(new Label("Beschreibung:"), 0, 5);
        add(tfBeschreibung, 1, 5);

        tfTitel.setMaxWidth(Double.MAX_VALUE);
        tfBeginn.setMaxWidth(Double.MAX_VALUE);
        tfEnde.setMaxWidth(Double.MAX_VALUE);
        tfOrt.setMaxWidth(Double.MAX_VALUE);
        tfBeschreibung.setMaxWidth(Double.MAX_VALUE);
    }

    public String getTitel() {
        return tfTitel.getText().trim();
    }

    public LocalDate getDatum() {
        return dpDatum.getValue();
    }

    public String getBeginn() {
        return tfBeginn.getText().trim();
    }

    public String getEnde() {
        return tfEnde.getText().trim();
    }

    public String getOrt() {
        return tfOrt.getText().trim();
    }

    public String getBeschreibung() {
        return tfBeschreibung.getText().trim();
    }

    public void fuelleMit(Termin termin) {
        tfTitel.setText(termin.getTitel());
        dpDatum.setValue(termin.getDatum());
        tfBeginn.setText(termin.getBeginn().toString());
        tfEnde.setText(termin.getEnde().toString());
        tfOrt.setText(termin.getOrt());
        tfBeschreibung.setText(termin.getBeschreibung());
    }

    public void leeren() {
        tfTitel.clear();
        dpDatum.setValue(LocalDate.now());
        tfBeginn.clear();
        tfEnde.clear();
        tfOrt.clear();
        tfBeschreibung.clear();
    }


    public Termin erstelleTermin() {
        return new Termin(
                getTitel(),
                getDatum(),
                LocalTime.parse(getBeginn()),
                LocalTime.parse(getEnde()),
                getOrt(),
                getBeschreibung()
        );
    }

    public Termin erstelleNeueVersion(Termin alterTermin) {
        String titel = getTitel().isEmpty() ? alterTermin.getTitel() : getTitel();
        LocalDate datum = getDatum() == null ? alterTermin.getDatum() : getDatum();
        LocalTime beginn = getBeginn().isEmpty()
                ? alterTermin.getBeginn() : LocalTime.parse(getBeginn());
        LocalTime ende = getEnde().isEmpty()
                ? alterTermin.getEnde() : LocalTime.parse(getEnde());
        String ort = getOrt().isEmpty() ? alterTermin.getOrt() : getOrt();
        String beschreibung = getBeschreibung().isEmpty()
                ? alterTermin.getBeschreibung() : getBeschreibung();

        return alterTermin.updateTermin(titel, datum, beginn, ende, ort, beschreibung);
    }
}