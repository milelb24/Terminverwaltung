package at.htlkaindorf.terminverwaltung.persistence;

import at.htlkaindorf.terminverwaltung.model.Termin;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/** Kümmert sich ausschließlich um das Laden und Speichern der Terminliste. */
public class TerminSpeicher {
    private final Path datei;

    public TerminSpeicher() {
        this(Paths.get("termine_speicher.dat"));
    }

    public TerminSpeicher(Path datei) {
        if (datei == null) {
            throw new IllegalArgumentException("Der Dateipfad darf nicht null sein.");
        }
        this.datei = datei;
    }

    public void speichereTermine(List<Termin> termine) throws IOException {
        Path ordner = datei.toAbsolutePath().getParent();
        if (ordner != null) {
            Files.createDirectories(ordner);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(datei))) {
            oos.writeObject(new ArrayList<>(termine));
        }
    }

    @SuppressWarnings("unchecked")
    public List<Termin> ladeTermine() {
        if (!Files.exists(datei)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(datei))) {
            Object objekt = ois.readObject();
            if (!(objekt instanceof List<?>)) {
                throw new IOException("Die Speicherdatei enthält keine Terminliste.");
            }

            List<?> geladeneObjekte = (List<?>) objekt;
            List<Termin> geladeneTermine = new ArrayList<>();
            for (Object element : geladeneObjekte) {
                if (!(element instanceof Termin)) {
                    throw new IOException("Die Speicherdatei enthält einen ungültigen Eintrag.");
                }
                geladeneTermine.add((Termin) element);
            }
            return geladeneTermine;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Fehler beim Laden der Termine: " + e.getMessage());
            sichereAlteDatei();
            return new ArrayList<>();
        }
    }

    /** Legt bei einem nicht lesbaren Speicherstand eine Sicherungskopie an. */
    private void sichereAlteDatei() {
        try {
            Path backupDatei = datei.resolveSibling(datei.getFileName().toString() + ".backup");
            Files.copy(datei, backupDatei, StandardCopyOption.REPLACE_EXISTING);
            System.err.println("Die ursprüngliche Datei wurde gesichert unter: " + backupDatei);
        } catch (IOException e) {
            System.err.println("Die alte Speicherdatei konnte nicht gesichert werden: " + e.getMessage());
        }
    }

    public File getDatei() {
        return datei.toFile();
    }
}