package assignment_2.database_table_classes.shoe_related;

public final class Color {

    private final int id;
    private final String name;

    public Color(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
