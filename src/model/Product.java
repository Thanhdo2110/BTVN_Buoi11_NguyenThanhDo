package model;

public class Product {
    private int id;
    private String productName;
    private int price;
    private String brand;
    private int stock;

    public Product() {
    }

    public Product(int id, String productName, int price, String brand, int stock) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.brand = brand;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
