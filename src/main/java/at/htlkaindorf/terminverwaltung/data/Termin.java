package at.htlkaindorf.terminverwaltung.data;

public class Termin {
    private final int id;
    private final String name;
    private final boolean active;

    public Termin(int id, String name) {
        this.id = id;
        this.name = name;
        this.active = true;
    }
}
