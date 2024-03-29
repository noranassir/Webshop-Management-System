package assignment_2.database_table_classes.shoe_related;

public final class Brand {

    private final int id;
    private final String name;

    public Brand(int id, String name) {
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
