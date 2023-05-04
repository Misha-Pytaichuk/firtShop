package Entity;

public class Product {
    private int id;
    private String name;
    private double price;
    private double count;
    private int category;

    public Product(int id, String name, double price, double count, int category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.category = category;
    }

    public Product(int id, String name, double count, double price){
        this.id = id;
        this.name = name;
        this.count = count;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
