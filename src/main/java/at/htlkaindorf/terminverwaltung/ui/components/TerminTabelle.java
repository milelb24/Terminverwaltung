package at.htlkaindorf.terminverwaltung.ui.components;

import at.htlkaindorf.terminverwaltung.model.Termin;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TerminTabelle extends TableView<Termin> {
    private final TableColumn<Termin, String> colId;

    public TerminTabelle() {
        colId = new TableColumn<>("ID");
        colId.setCellValueFactory(zelle ->
                new ReadOnlyStringWrapper(zelle.getValue().getId()));
        colId.setPrefWidth(220);

        TableColumn<Termin, String> colTitel = new TableColumn<>("Titel");
        colTitel.setCellValueFactory(zelle ->
                new ReadOnlyStringWrapper(zelle.getValue().getTitel()));
        colTitel.setPrefWidth(150);

        TableColumn<Termin, LocalDate> colDatum = new TableColumn<>("Datum");
        colDatum.setCellValueFactory(zelle ->
                new ReadOnlyObjectWrapper<>(zelle.getValue().getDatum()));

        TableColumn<Termin, LocalTime> colBeginn = new TableColumn<>("Beginn");
        colBeginn.setCellValueFactory(zelle ->
                new ReadOnlyObjectWrapper<>(zelle.getValue().getBeginn()));

        TableColumn<Termin, LocalTime> colEnde = new TableColumn<>("Ende");
        colEnde.setCellValueFactory(zelle ->
                new ReadOnlyObjectWrapper<>(zelle.getValue().getEnde()));

        TableColumn<Termin, String> colOrt = new TableColumn<>("Ort");
        colOrt.setCellValueFactory(zelle ->
                new ReadOnlyStringWrapper(zelle.getValue().getOrt()));

        TableColumn<Termin, String> colBeschreibung = new TableColumn<>("Beschreibung");
        colBeschreibung.setCellValueFactory(zelle ->
                new ReadOnlyStringWrapper(zelle.getValue().getBeschreibung()));
        colBeschreibung.setPrefWidth(200);

        TableColumn<Termin, Integer> colVersion = new TableColumn<>("Version");
        colVersion.setCellValueFactory(zelle ->
                new ReadOnlyObjectWrapper<>(zelle.getValue().getVersion()));

        getColumns().addAll(colId, colTitel, colDatum, colBeginn, colEnde,
                colOrt, colBeschreibung, colVersion);
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void setTermine(List<Termin> termine) {
        setItems(FXCollections.observableArrayList(termine));
    }

    public Termin getAusgewaehlterTermin() {
        return getSelectionModel().getSelectedItem();
    }

    public void setIdSichtbar(boolean sichtbar) {
        colId.setVisible(sichtbar);
    }
}