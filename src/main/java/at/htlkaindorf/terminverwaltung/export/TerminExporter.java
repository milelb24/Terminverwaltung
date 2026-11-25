package at.htlkaindorf.terminverwaltung.export;

import at.htlkaindorf.terminverwaltung.model.Termin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/** Exportiert Termine in CSV- oder TXT-Dateien. */
public class TerminExporter {

    public void exportiereAlsCsv(File datei, List<Termin> termine) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(datei.toPath(), StandardCharsets.UTF_8)) {
            writer.write("ID;Titel;Datum;Beginn;Ende;Ort;Beschreibung;Version");
            writer.newLine();

            for (Termin termin : termine) {
                writer.write(csvWert(termin.getId()) + ";"
                        + csvWert(termin.getTitel()) + ";"
                        + csvWert(termin.getDatum().toString()) + ";"
                        + csvWert(termin.getBeginn().toString()) + ";"
                        + csvWert(termin.getEnde().toString()) + ";"
                        + csvWert(termin.getOrt()) + ";"
                        + csvWert(termin.getBeschreibung()) + ";"
                        + csvWert("v" + termin.getVersion()));
                writer.newLine();
            }
        }
    }

    public void exportiereAlsTxt(File datei, List<Termin> termine) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(datei.toPath(), StandardCharsets.UTF_8)) {
            writer.write("=== TERMINÜBERSICHT EXPORT ===");
            writer.newLine();
            writer.write("Anzahl Terminversionen: " + termine.size());
            writer.newLine();
            writer.write("=========================================");
            writer.newLine();
            writer.newLine();

            for (Termin termin : termine) {
                writer.write(termin.toString());
                writer.newLine();
            }
        }
    }

    /** Schützt CSV-Felder vor Problemen mit Semikolons, Anführungszeichen und Zeilenumbrüchen. */
    private String csvWert(String wert) {
        return "\"" + wert.replace("\"", "\"\"") + "\"";
    }
}
