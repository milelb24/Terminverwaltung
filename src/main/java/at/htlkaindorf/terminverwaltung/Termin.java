package at.htlkaindorf.terminverwaltung;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public final class Termin implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String titel;
    private final LocalDate datum;
    private final LocalTime beginn;
    private final LocalTime ende;
    private final String ort;
    private final String beschreibung;
    private final int version;

    public Termin(String titel, LocalDate datum, LocalTime beginn, LocalTime ende, String ort, String beschreibung) {
        this.id = UUID.randomUUID().toString();
        this.titel = titel;
        this.datum = datum;
        this.beginn = beginn;
        this.ende = ende;
        this.ort = ort;
        this.beschreibung = beschreibung;
        this.version = 1;
    }

    private Termin(String id, String titel, LocalDate datum, LocalTime beginn, LocalTime ende, String ort, String beschreibung, int version) {
        this.id = id;
        this.titel = titel;
        this.datum = datum;
        this.beginn = beginn;
        this.ende = ende;
        this.ort = ort;
        this.beschreibung = beschreibung;
        this.version = version;
    }

    public Termin updateTermin(String titel, LocalDate datum, LocalTime beginn, LocalTime ende, String ort, String beschreibung) {
        return new Termin(this.id, titel, datum, beginn, ende, ort, beschreibung, this.version + 1);
    }

    public String getId() { return id; }
    public String getTitel() { return titel; }
    public LocalDate getDatum() { return datum; }
    public LocalTime getBeginn() { return beginn; }
    public LocalTime getEnde() { return ende; }
    public String getOrt() { return ort; }
    public String getBeschreibung() { return beschreibung; }
    public int getVersion() { return version; }

    @Override
    public String toString() {
        return String.format("[%s | v%d] %s am %s (%s - %s) @ %s | %s",
                id, version, titel, datum, beginn, ende, ort, beschreibung);
    }
}