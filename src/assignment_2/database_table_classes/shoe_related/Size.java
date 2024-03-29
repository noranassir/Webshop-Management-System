package assignment_2.database_table_classes.shoe_related;

public final class Size {

    private final int id;
    private final int size_no;

    public Size(int id, int size) {
        this.id = id;
        this.size_no = size;
    }
    public int getId() {
        return id;
    }

    public int getSize_no() {
        return size_no;
    }

}
