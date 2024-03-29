package assignment_2.database_table_classes.customer_related;

public final class Customer {

    private final int id;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String postalCode;
    private final City city;

    public Customer(int id, String firstName, String lastName, String address, String postalCode, City city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;

    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }


    public String getPostalCode() {
        return postalCode;
    }

    public City getCity() {
        return city;
    }

}
