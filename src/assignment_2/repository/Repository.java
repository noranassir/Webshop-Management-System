package assignment_2.repository;

import assignment_2.database_table_classes.customer_related.City;
import assignment_2.database_table_classes.customer_related.Customer;
import assignment_2.database_table_classes.customer_related.CustomerOrder;
import assignment_2.database_table_classes.customer_related.CustomerOrderDetails;
import assignment_2.database_table_classes.shoe_related.Brand;
import assignment_2.database_table_classes.shoe_related.Color;
import assignment_2.database_table_classes.shoe_related.Shoe;
import assignment_2.database_table_classes.shoe_related.Size;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Repository {

    private final Properties properties = new Properties();

    public Repository() {

        try {
            properties.load(new FileInputStream("src/assignment_2/repository/Settings.properties"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCustomerId(String email, String password) {
        int customerId = -1;

        try (Connection connect = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));

             PreparedStatement statement = connect.prepareStatement("SELECT id" +
                     " FROM customer" +
                     " WHERE email = ? AND password = ?")) {

            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                customerId = rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customerId;
    }

    public Map loggedInCustomer(String email, String password) {
        Map<Integer, Customer> loggedInCustomerMap = new HashMap<>();

        try (Connection connect = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));

             PreparedStatement statement = connect.prepareStatement("SELECT " +
                     "customer.id, customer.first_name, customer.last_name, customer.address, customer.postal_code," +
                     " city.id, city.name" +
                     " FROM customer " +
                     "JOIN city ON customer.cityid = city.id " +
                     "WHERE email = ? AND password = ? ")

        ) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("customer.id");
                String fName = rs.getString("first_name");
                String lName = rs.getString("last_name");
                String address = rs.getString("address");
                String postalCode = rs.getString("postal_code");
                int cityId = rs.getInt("city.id");
                String cityName = rs.getString("city.name");

                City city = new City(cityId, cityName);
                Customer customer = new Customer(id, fName, lName, address, postalCode, city);
                loggedInCustomerMap.put(id, customer);

            }
            if (loggedInCustomerMap.isEmpty()) {
                System.out.println("Invalid email or password");
                return null;
            } else {
                loggedInCustomerMap.values().forEach(c -> System.out.println("Welcome " + c.getFirstName() + " " + c.getLastName()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return loggedInCustomerMap;
    }

    public List getAllColors() throws SQLException {
        List<Color> allColors = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));

             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM color");
        ) {
            while (rs.next()) {

                int id = rs.getInt("id");
                String name = rs.getString("name");
                Color color = new Color(id, name);

                allColors.add(color);
            }

        }
        return allColors;
    }

    public List getShoeSizes() throws SQLException {
        List<Size> shoeSizes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));

             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM size");
        ) {
            while (rs.next()) {

                int id = rs.getInt("id");
                int size_no = rs.getInt("size_no");

                Size size = new Size(id, size_no);
                shoeSizes.add(size);
            }
        }
        return shoeSizes;
    }

    public List getBrands() throws SQLException {
        List<Brand> allBrands = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));

             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM brand");
        ) {
            while (rs.next()) {

                int id = rs.getInt("id");
                String brandName = rs.getString("name");
                Brand brand = new Brand(id, brandName);

                allBrands.add( brand);
            }
        }

        return allBrands;
    }

    public List<Shoe> getShoesInStock() throws SQLException {
        List<Shoe> shoesInStock = new ArrayList<>();

        try (Connection connect = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));
             Statement statement = connect.createStatement();
             ResultSet rs = statement.executeQuery("SELECT shoe.id, " +
                     "brand.id, brand.name, " +
                     "shoe.model_name, " +
                     "color.id, color.name, " +
                     "size.id, size.size_no, " +
                     "shoe.price, " +
                     "stock.quantity " +
                     "FROM shoe " +
                     "JOIN brand ON shoe.brandid = brand.id " +
                     "JOIN color ON shoe.colorid = color.id " +
                     "JOIN size ON shoe.sizeid = size.id " +
                     "JOIN stock ON shoe.id = stock.shoeid " +
                     "WHERE stock.quantity > 0")) {


            while (rs.next()) {

                int shoeId = rs.getInt("shoe.id");
                int brandId = rs.getInt("brand.id");
                String brandName = rs.getString("brand.name");
                Brand brand = new Brand(brandId, brandName);
                String modelName = rs.getString("shoe.model_name");
                int colorId = rs.getInt("color.id");
                String colorName = rs.getString("color.name");
                Color color = new Color(colorId, colorName);
                int sizeId = rs.getInt("size.id");
                int sizeNo = rs.getInt("size.size_no");
                Size size = new Size(sizeId, sizeNo);
                double price = rs.getDouble("shoe.price");

                Shoe shoe = new Shoe(shoeId, brand, modelName, color, size, price);
                shoesInStock.add(shoe);
            }
        }

        AtomicInteger index = new AtomicInteger(1);

        shoesInStock.forEach(s -> System.out.println(index.getAndIncrement() + "." + s.getBrand().getName() + " " + s.getModelName() + " " + s.getColor().getName() + " " + s.getSize().getSize_no()));


        return shoesInStock;
    }


    public List<Shoe> getAllShoes() throws SQLException {
        List<Shoe> shoeList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM shoe");
        ) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int brandId = rs.getInt("brandId");
                String modelName = rs.getString("model_name");
                int colorId = rs.getInt("colorId");
                int sizeId = rs.getInt("sizeId");
                double price = rs.getDouble("price");


                Brand brand = getBrandById(brandId);
                Color color = getColorById(colorId);
                Size size = getSizeById(sizeId);

                Shoe shoe = new Shoe(id, brand, modelName, color, size, price);
                shoeList.add(shoe);
            }
        }
        return shoeList;
    }
    private Brand getBrandById(int brandId) throws SQLException {
        Brand brand = null;

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM brand WHERE id = ?");
        ) {
            statement.setInt(1, brandId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                brand = new Brand(id, name);
            }
        }
        return brand;
    }
    private Size getSizeById(int sizeId) throws SQLException {
        Size size = null;

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM size WHERE id = ?");
        ) {
            statement.setInt(1, sizeId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                int id = resultSet.getInt("id");
                int sizeNo = resultSet.getInt("size_no");
                size = new Size(id, sizeNo);
            }
        }
        return size;
    }

    private Color getColorById(int colorId) throws SQLException {
        Color color = null;

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM color WHERE id = ?");
        ) {
            statement.setInt(1, colorId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                color = new Color(id, name);
            }
        }
        return color;
    }

    public void addToCart(int customerId, int orderId, int shoeId) throws SQLException {

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));
             CallableStatement statement = connection.prepareCall("call addToCart(?,?,?,?)");
        ) {
            statement.setInt(1, customerId);
            statement.setInt(2, orderId);
            statement.setInt(3, shoeId);
            statement.registerOutParameter(4, Types.VARCHAR);
            statement.executeQuery();

            System.out.println(statement.getString(4));
        }
    }

    public Map<Customer, String> searchCustomer(String shoeAttribute, String type) throws IOException, SQLException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/assignment_2/repository/Settings.properties"));
        String sqlQuery = "SELECT " +
                "customer.id, " +
                "customer.first_name, " +
                "customer.last_name, " +
                "customer.address, " +
                "customer.postal_code, " +
                "customer.cityid, " +
                "city.name " +
                "FROM " +
                "customer " +
                "JOIN city ON customer.cityid = city.id " +
                "JOIN customer_order ON customer.id = customer_order.customerid " +
                "JOIN customer_order_details ON customer_order.id = customer_order_details.orderid " +
                "JOIN shoe ON customer_order_details.shoeid = shoe.id " +
                "JOIN brand ON shoe.brandid = brand.id " +
                "JOIN color ON shoe.colorid = color.id " +
                "JOIN size ON shoe.sizeid = size.id ";

        switch (type.toLowerCase()) {
            case "color":
                sqlQuery += "WHERE color.name = ?";
                break;
            case "brand":
                sqlQuery += "WHERE brand.name = ?";
                break;
            case "size":
                sqlQuery += "WHERE size.size_no = ?";
                break;
            default:
                throw new IllegalArgumentException("Invalid type");
        }

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));

             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, shoeAttribute);
            ResultSet rs = preparedStatement.executeQuery();

            Map<Customer, String> customerReport = new HashMap<>();
            while (rs.next()) {
                int customerId = rs.getInt("customer.id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String address = rs.getString("address");
                String postalCode = rs.getString("postal_code");
                int cityId = rs.getInt("cityid");
                String cityName = rs.getString("city.name");

                City city = new City(cityId, cityName);
                Customer customer = new Customer(customerId, firstName, lastName, address, postalCode, city);
                customerReport.put(customer, cityName);
            }
            customerReport.keySet().forEach(customer ->
                    System.out.println(customer.getFirstName() + " " + customer.getLastName() + " " + customer.getAddress())
            );

            return customerReport;
        }
    }


    public static Function<String, Function<String, Map<Integer, Customer>>> searchCustomer = shoeAttribute -> type -> {

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/assignment_2/repository/Settings.properties"));

            String sqlQuery = "SELECT " +
                    "customer.id, " +
                    "customer.first_name, " +
                    "customer.last_name, " +
                    "customer.address, " +
                    "customer.postal_code, " +
                    "customer.cityid, " +
                    "city.name " +
                    "FROM " +
                    "customer " +
                    "JOIN city ON customer.cityid = city.id " +
                    "JOIN customer_order ON customer.id = customer_order.customerid " +
                    "JOIN customer_order_details ON customer_order.id = customer_order_details.orderid " +
                    "JOIN shoe ON customer_order_details.shoeid = shoe.id " +
                    "JOIN brand ON shoe.brandid = brand.id " +
                    "JOIN color ON shoe.colorid = color.id " +
                    "JOIN size ON shoe.sizeid = size.id ";

            switch (type.toLowerCase()) {
                case "color":
                    sqlQuery += "WHERE color.name = ?";
                    break;
                case "brand":
                    sqlQuery += "WHERE brand.name = ?";
                    break;
                case "size":
                    sqlQuery += "WHERE size.size_no = ?";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid type");
            }

            try (Connection connection = DriverManager.getConnection(
                    properties.getProperty("connectionString"),
                    properties.getProperty("name"),
                    properties.getProperty("password"));

                 PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

                preparedStatement.setString(1, shoeAttribute);
                ResultSet rs = preparedStatement.executeQuery();

                Map<Integer, Customer> customerReport = new HashMap<>();
                while (rs.next()) {
                    int customerId = rs.getInt("customer.id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String address = rs.getString("address");
                    String postalCode = rs.getString("postal_code");
                    int cityId = rs.getInt("cityid");
                    String cityName = rs.getString("city.name");

                    City city = new City(cityId, cityName);
                    Customer customer = new Customer(customerId, firstName, lastName, address, postalCode, city);
                    customerReport.put(customerId, customer);
                }
                if (customerReport.isEmpty()) {
                    System.out.println("No customers found");
                } else {
                    customerReport.values().forEach(customer ->
                            System.out.println(customer.getFirstName() + " " + customer.getLastName() + ", " + customer.getAddress())
                    );
                }

                return customerReport;
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    };

    public static void countOrdersPerCustomer() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/assignment_2/repository/Settings.properties"));

        try (Connection connect = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));
             Statement statement = connect.createStatement();
             ResultSet rs = statement.executeQuery("select" +
                     " customer.id, first_name, last_name, address, postal_code, cityid, city.name, " +
                     "customer_order.id as order_id, customer_order.order_date " +
                     "from customer " +
                     "join city on customer.cityid = city.id " +
                     "left join customer_order on customer.id = customer_order.customerid");

        ) {
            List<Customer> customerList = new ArrayList<>();
            List<CustomerOrder> customerOrderList = new ArrayList<>();

            Set<Customer> customersWithOrders = new HashSet<>();

            while (rs.next()) {
                int customerId = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String address = rs.getString("address");
                String postalCode = rs.getString("postal_code");
                int cityId = rs.getInt("cityid");
                String cityName = rs.getString("city.name");
                int orderId = rs.getInt("order_id");
                Timestamp timestamp = rs.getTimestamp("order_date");

                boolean found = customerList.stream().anyMatch(c -> c.getId() == customerId);
                if (!found) {
                    City city = new City(cityId, cityName);
                    Customer customer = new Customer(customerId, firstName, lastName, address, postalCode, city);
                    customerList.add(customer);
                }

                boolean orderFound = customerOrderList.stream().anyMatch(o -> o.getId() == orderId);
                if (!orderFound) {
                    Customer customer = customerList.stream().filter(c -> c.getId() == customerId).findFirst().orElse(null);
                    if (customer != null) {
                        CustomerOrder customerOrder = new CustomerOrder(orderId, customer, timestamp);
                        customerOrderList.add(customerOrder);
                        customersWithOrders.add(customer);
                    }
                }
            }

            customerList.stream()
                    .filter(c -> !customersWithOrders.contains(c))
                    .forEach(c -> System.out.println(c.getFirstName() + " " + c.getLastName() + ", Orders: 0"));


            Map<Customer, Long> ordersPerCustomer = customerOrderList.stream()
                    .collect(Collectors.groupingBy(CustomerOrder::getCustomer, Collectors.counting()));


            ordersPerCustomer.forEach((customer, orderCount) -> System.out.println(customer.getFirstName() + " " + customer.getLastName() + ", Orders: " + orderCount));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Map<String, Double> orderValueCity() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/assignment_2/repository/Settings.properties"));
        List<CustomerOrderDetails> customerOrders = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(
                     "SELECT " +
                             "customer.id, " +
                             "customer.first_name, " +
                             "customer.last_name, " +
                             "customer.address, " +
                             "customer.postal_code, " +
                             "customer.cityid, " +
                             "city.id, " +
                             "city.name, " +
                             "customer_order.id, " +
                             "customer_order.order_date, " +
                             "customer_order_details.orderid, " +
                             "customer_order_details.shoeid, " +
                             "customer_order_details.quantity, " +
                             "shoe.id, " +
                             "shoe.model_name, " +
                             "shoe.brandid, " +
                             "shoe.colorid, " +
                             "shoe.sizeid, " +
                             "shoe.price, " +
                             "brand.id, " +
                             "brand.name, " +
                             "color.id, " +
                             "color.name, " +
                             "size.id, " +
                             "size.size_no " +
                             "FROM " +
                             "customer " +
                             "JOIN city ON customer.cityid = city.id " +
                             "JOIN customer_order ON customer.id = customer_order.customerid " +
                             "JOIN customer_order_details ON customer_order.id = customer_order_details.orderid " +
                             "JOIN shoe ON customer_order_details.shoeid = shoe.id " +
                             "JOIN brand ON shoe.brandid = brand.id " +
                             "JOIN color ON shoe.colorid = color.id " +
                             "JOIN size ON shoe.sizeid = size.id");) {

            while (rs.next()) {
                int orderId = rs.getInt("customer_order.id");
                Timestamp timestamp = rs.getTimestamp("order_date");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                int customerId = rs.getInt("customer.id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String address = rs.getString("address");
                String postalCode = rs.getString("postal_code");
                int cityId = rs.getInt("city.id");
                String cityName = rs.getString("city.name");

                int brandId = rs.getInt("brand.id");
                String brandName = rs.getString("brand.name");
                Brand brand = new Brand(brandId, brandName);
                int colorId = rs.getInt("color.id");
                String colorName = rs.getString("color.name");
                Color color = new Color(colorId, colorName);
                int sizeId = rs.getInt("size.id");
                int sizeNo = rs.getInt("size.size_no");
                Size size = new Size(sizeId, sizeNo);

                int shoeId = rs.getInt("shoe.id");
                String modelName = rs.getString("shoe.model_name");


                City city = new City(cityId, cityName);

                Customer customer = new Customer(customerId, firstName, lastName, address, postalCode, city);
                CustomerOrder customerOrder = new CustomerOrder(orderId, customer, timestamp);
                Shoe shoe = new Shoe(shoeId, brand, modelName, color, size, price);
                CustomerOrderDetails customerOrderDetails = new CustomerOrderDetails(customerOrder, shoe, quantity);
                customerOrders.add(customerOrderDetails);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        Map<String, List<CustomerOrderDetails>> ordersByCityName = customerOrders.stream()
                .collect(Collectors.groupingBy(order -> order.getOrder().getCustomer().getCity().getName()));

        //Räkna ut beställningsvärde utefter namn på ort
        Map<String, Double> orderValuePerCityName = new HashMap<>();
        ordersByCityName.forEach((cityName, orderDetailsList) -> {
            double totalOrderValue = orderDetailsList.stream()
                    .mapToDouble(order -> order.getQuantity() * order.getShoe().getPrice())
                    .sum();
            orderValuePerCityName.put(cityName, totalOrderValue);
        });


        orderValuePerCityName.forEach((cityName, totalValue) -> {
            System.out.println(cityName + ": " + totalValue);
        });

        return Collections.unmodifiableMap(orderValuePerCityName);
    }

}
