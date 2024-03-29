package assignment_2.database_table_classes.shoe_related;

public final class Shoe {

    private final int id;
    private final Brand brand;
    private final String modelName;
    private final Color color;
    private final Size size;
    private final double price;

    public Shoe(int id, Brand brand, String modelName, Color color, Size size, double price) {
        this.id = id;
        this.brand = brand;
        this.modelName = modelName;
        this.color = color;
        this.size = size;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public Brand getBrand() {
        return brand;
    }


    public String getModelName() {
        return modelName;
    }

    public Color getColor() {
        return color;
    }

    public Size getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public String getBrandName() {
        return brand.getName();
    }

    public String getColorName() {
        return color.getName();
    }

    public int getSizeNo() {
        return size.getSize_no();
    }
}
