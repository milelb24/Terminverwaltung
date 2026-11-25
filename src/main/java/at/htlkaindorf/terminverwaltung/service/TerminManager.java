package at.htlkaindorf.terminverwaltung.service;

import at.htlkaindorf.terminverwaltung.model.Termin;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class TerminManager {
    private final List<Termin> termine;

    public TerminManager() {
        this(Collections.<Termin>emptyList());
    }

    public TerminManager(List<Termin> gespeicherteTermine) {
        termine = new ArrayList<>();
        if (gespeicherteTermine != null) {
            termine.addAll(gespeicherteTermine);
        }
    }

    public void erstelleTermin(Termin termin) {
        Objects.requireNonNull(termin, "Der Termin darf nicht null sein.");

        boolean idBereitsVorhanden = termine.stream()
                .anyMatch(t -> t.getId().equals(termin.getId()));
        if (idBereitsVorhanden) {
            throw new IllegalArgumentException("Diese Termin-ID ist bereits vorhanden.");
        }
        termine.add(termin);
    }

    public void aktualisiereTermin(String id, Termin neueVersion) {
        Objects.requireNonNull(id, "Die ID darf nicht null sein.");
        Objects.requireNonNull(neueVersion, "Die neue Version darf nicht null sein.");

        if (!id.equals(neueVersion.getId())) {
            throw new IllegalArgumentException("Die ID der neuen Version stimmt nicht überein.");
        }

        int hoechsteVersion = termine.stream()
                .filter(t -> t.getId().equals(id))
                .mapToInt(Termin::getVersion)
                .max()
                .orElse(0);

        if (hoechsteVersion == 0) {
            throw new IllegalArgumentException("Der ursprüngliche Termin wurde nicht gefunden.");
        }
        if (neueVersion.getVersion() != hoechsteVersion + 1) {
            throw new IllegalArgumentException("Es kann nur eine neue Version auf Basis der letzten Version erstellt werden.");
        }

        termine.add(neueVersion);
    }

    public void loescheTermin(String id, int version) {
        termine.removeIf(t -> t.getId().equals(id) && t.getVersion() == version);
    }

    public List<Termin> getAlleTermine() {
        return Collections.unmodifiableList(new ArrayList<>(termine));
    }

    public List<Termin> sucheTermine(String suchbegriff) {
        if (suchbegriff == null || suchbegriff.trim().isEmpty()) {
            return getAlleTermine();
        }

        String suchtext = suchbegriff.toLowerCase(Locale.ROOT).trim();
        return termine.stream()
                .filter(t -> t.getTitel().toLowerCase(Locale.ROOT).contains(suchtext)
                        || t.getBeschreibung().toLowerCase(Locale.ROOT).contains(suchtext)
                        || t.getOrt().toLowerCase(Locale.ROOT).contains(suchtext))
                .collect(Collectors.toList());
    }

    public List<Termin> filterNachDatum(LocalDate datum) {
        Objects.requireNonNull(datum, "Das Filterdatum darf nicht null sein.");
        return termine.stream()
                .filter(t -> t.getDatum().equals(datum))
                .collect(Collectors.toList());
    }
}