package at.htlkaindorf.terminverwaltung.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
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

    public Termin(String titel, LocalDate datum, LocalTime beginn,
                  LocalTime ende, String ort, String beschreibung) {
        this(UUID.randomUUID().toString(), titel, datum, beginn, ende, ort, beschreibung, 1);
    }

    private Termin(String id, String titel, LocalDate datum, LocalTime beginn,
                   LocalTime ende, String ort, String beschreibung, int version) {
        this.id = Objects.requireNonNull(id, "Die ID darf nicht null sein.");
        this.titel = pruefeText(titel, "Der Titel darf nicht leer sein.");
        this.datum = Objects.requireNonNull(datum, "Das Datum muss angegeben werden.");
        this.beginn = Objects.requireNonNull(beginn, "Die Beginnzeit muss angegeben werden.");
        this.ende = Objects.requireNonNull(ende, "Die Endzeit muss angegeben werden.");
        this.ort = ort == null ? "" : ort.trim();
        this.beschreibung = beschreibung == null ? "" : beschreibung.trim();

        if (!ende.isAfter(beginn)) {
            throw new IllegalArgumentException("Die Endzeit muss nach der Beginnzeit liegen.");
        }
        if (version < 1) {
            throw new IllegalArgumentException("Die Version muss mindestens 1 sein.");
        }
        this.version = version;
    }

    private static String pruefeText(String wert, String fehlermeldung) {
        if (wert == null || wert.trim().isEmpty()) {
            throw new IllegalArgumentException(fehlermeldung);
        }
        return wert.trim();
    }

    public Termin updateTermin(String titel, LocalDate datum, LocalTime beginn,
                               LocalTime ende, String ort, String beschreibung) {
        return new Termin(id, titel, datum, beginn, ende, ort, beschreibung, version + 1);
    }

    public String getId() {
        return id;
    }

    public String getTitel() {
        return titel;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public LocalTime getBeginn() {
        return beginn;
    }

    public LocalTime getEnde() {
        return ende;
    }

    public String getOrt() {
        return ort;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return String.format("[%s | v%d] %s am %s (%s - %s) @ %s | %s",
                id, version, titel, datum, beginn, ende, ort, beschreibung);
    }
}