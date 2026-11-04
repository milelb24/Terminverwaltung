package at.htlkaindorf.terminverwaltung;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TerminManager {
    private List<Termin> termine;
    private final String DATEI_NAME = "termine_speicher.dat";

    public TerminManager() {
        this.termine = new ArrayList<>();
        ladeTermine();
    }

    public void erstelleTermin(Termin termin) {
        termine.add(termin);
    }

    public void aktualisiereTermin(String id, Termin neueVersion) {
        termine.add(neueVersion);
    }

    public void loescheTermin(String id, int version) {
        termine.removeIf(t -> t.getId().equals(id) && t.getVersion() == version);
    }

    public List<Termin> getAlleTermine() {
        return termine;
    }

    public List<Termin> sucheTermine(String suchbegriff) {
        if (suchbegriff == null || suchbegriff.trim().isEmpty()) {
            return getAlleTermine();
        }
        String term = suchbegriff.toLowerCase().trim();
        return termine.stream()
                .filter(t -> t.getTitel().toLowerCase().contains(term) ||
                        t.getBeschreibung().toLowerCase().contains(term) ||
                        t.getOrt().toLowerCase().contains(term))
                .collect(Collectors.toList());
    }

    public List<Termin> filterNachDatum(LocalDate datum) {
        return termine.stream()
                .filter(t -> t.getDatum().equals(datum))
                .collect(Collectors.toList());
    }

    public void speichereTermine() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(DATEI_NAME)))) {
            oos.writeObject(termine);
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern: " + e.getMessage());
        }
    }

    public void exportiereAlsCsv(File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8))) {
            writer.println("ID;Titel;Datum;Beginn;Ende;Ort;Beschreibung;Version");
            for (Termin t : termine) {
                writer.printf("%s;%s;%s;%s;%s;%s;%s;v%d%n",
                        t.getId(), t.getTitel(), t.getDatum(), t.getBeginn(),
                        t.getEnde(), t.getOrt(), t.getBeschreibung(), t.getVersion());
            }
        }
    }

    public void exportiereAlsTxt(File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8))) {
            writer.println("=== TERMINÜBERSICHT EXPORT ===");
            writer.println("Anzahl Termine: " + termine.size());
            writer.println("=========================================\n");
            for (Termin t : termine) {
                writer.println(t.toString());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void ladeTermine() {
        File datei = new File(DATEI_NAME);
        if (datei.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(datei.toPath()))) {
                termine = (List<Termin>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Fehler beim Laden: " + e.getMessage());
            }
        }
    }
}