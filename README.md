# Webshop Management System

A console program that reads from a mock database created in MySQL, which generates reports and allows changes to existing orders in the database.

## Features

### AddToCart Program (Basic Requirement)

- **User Authentication**: Users are prompted to enter their email address and password to log in.
- **Product Selection**: Users can select products from the available stock.
- **Feedback**: Users receive feedback on whether the product was successfully added to their order.
- **User-Friendly Interface**: The program ensures that users do not see internal database IDs but receive user-friendly prompts and information.

### Sales Support Program (Advanced Requirement - Pass with distinction)

- **Customer Reports**: Generate reports listing customers who have purchased items of a specific size, color, or brand.
- **Order Count per Customer**: Generate a report listing each customer's name and the total number of orders they have placed.
- **Total Order Value per Customer**: Generate a report listing each customer's name and the total amount they have spent on orders.
- **Order Value per City**: Generate a report listing the total order value for each city.
- **Top Selling Products**: Generate a report listing the most sold products, including the model name and the quantity sold.

## Requirements for Pass with distinction

- **Functional Programming**: The code emphasizes functional programming with immutable variables and lambda expressions.
- **Object-Oriented Approach**: The Java program uses object-oriented principles to represent database entities as Java classes.
- **Lambda Expressions**: Lambda expressions are utilized for processing data and generating reports.
- **Custom Higher-Order Functions**: At least one custom higher-order function is implemented.
- **No Aggregating SQL Methods**: Data processing is performed using Java lambdas instead of SQL aggregation functions like SUM() or COUNT().
- **Proper Data Modeling**: Java classes are created to represent database tables, and relationships are maintained properly.
- **Demonstration**: The program is demonstrated to showcase its functionality, including order placement and report generation.

