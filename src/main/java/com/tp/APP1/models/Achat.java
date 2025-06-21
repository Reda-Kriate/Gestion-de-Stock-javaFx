package com.tp.APP1.models;

public class Achat {
    private int id;
    private String clientName;
    private String productName;
    private int quantity;
    private String status;

    public Achat(int id, String clientName, String productName, int quantity, String status) {
        this.id = id;
        this.clientName = clientName;
        this.productName = productName;
        this.quantity = quantity;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
