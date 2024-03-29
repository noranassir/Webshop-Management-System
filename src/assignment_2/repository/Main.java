package assignment_2.repository;

import assignment_2.database_table_classes.customer_related.Customer;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
public class Main {
        public static void main(String[] args) throws SQLException, IOException {
               customerOrder();
               getReports();
        }

        private static void customerOrder() throws SQLException {
                Repository repository = new Repository();

        while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter your email address to login:");
                String email = scanner.nextLine();
                System.out.println("Enter your password:");
                String password = scanner.nextLine();

                Map<Integer, Customer> loggedInCustomerMap = repository.loggedInCustomer(email, password);
                if (loggedInCustomerMap == null) {
                        continue;
                }
                int customerId = repository.getCustomerId(email, password);


                System.out.println("\nSelect a shoe");
                repository.getShoesInStock();
                int shoeId = scanner.nextInt();
                System.out.println("You have selected shoe " + shoeId);
                repository.addToCart(customerId, -1, shoeId);
                break;
        }}
        private static void getReports() throws IOException {
                Repository repository = new Repository();
                Scanner scanner = new Scanner(System.in);
                boolean exit = false;

                while (!exit) {
                        System.out.println("Choose report");
                        System.out.println("1. Search customer by attribute");
                        System.out.println("2. Count orders per customer");
                        System.out.println("3. Order value by city");
                        System.out.println("4. Exit");

                        int choice = scanner.nextInt();
                        scanner.nextLine();

                        switch (choice) {
                                case 1:
                                        System.out.println("Input shoe attribute (color, brand, size):");
                                        String attribute = scanner.nextLine();
                                        System.out.println("Enter value:");
                                        String value = scanner.nextLine();
                                        repository.searchCustomer.apply(value).apply(attribute);
                                        break;
                                case 2:
                                        repository.countOrdersPerCustomer();
                                        break;
                                case 3:
                                        repository.orderValueCity();
                                        break;
                                case 4:
                                        exit = true;
                                        break;
                                default:
                                        System.out.println("Invalid choice!");
                        }
                }
        }
}


