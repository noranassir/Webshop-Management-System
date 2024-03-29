package assignment_2.database_table_classes.shoe_related;

public final class Stock {


    private final int shoeId;
    private final int quantity;

    public Stock(int shoeId, int quantity) {
        this.shoeId=shoeId;
        this.quantity=quantity;
    }

    public int getShoeId() {
        return shoeId;
    }

    public int getQuantity() {
        return quantity;
    }

}
