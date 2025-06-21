package com.tp.APP1.models;


public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private String status;
    private boolean validated;

    public Product() {
    }

    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = quantity;
    }
    public Product( String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.stock = quantity;
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

    // Getter/Setter pour status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public int getStock() {
        return stock;
    }

    public void setStock(int quantity) {
        this.stock = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id= " + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + stock +
                '}';
    }
}