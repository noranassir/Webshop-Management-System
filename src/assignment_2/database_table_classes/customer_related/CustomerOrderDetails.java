package assignment_2.database_table_classes.customer_related;


import assignment_2.database_table_classes.shoe_related.Shoe;

public final class CustomerOrderDetails {

    private final CustomerOrder order;
    private final Shoe shoe;
    private final int quantity;

    public CustomerOrderDetails(CustomerOrder order, Shoe shoe, int quantity) {
        this.order = order;
        this.shoe = shoe;
        this.quantity = quantity;
    }

    public Shoe getShoe() {
        return shoe;
    }

    public CustomerOrder getOrder(){
        return order;
    }
    public int getQuantity() {
        return quantity;
    }

}
