package assignment_2.database_table_classes.customer_related;

import assignment_2.database_table_classes.customer_related.Customer;

import java.sql.Timestamp;

public final class CustomerOrder {

    private final int id;
    private final Customer customer;
    private final Timestamp timestamp;

    public CustomerOrder(int id, Customer customer, Timestamp timestamp) {
        this.id = id;
        this.customer = customer;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Timestamp getDateTime() {
        return timestamp;
    }

    public int getCustomerId() {
        return customer.getId();
    }
}
