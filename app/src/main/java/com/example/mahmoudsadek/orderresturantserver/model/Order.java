package com.example.mahmoudsadek.orderresturantserver.model;

/**
 * Created by Mahmoud Sadek on 6/17/2018.
 */

public class Order {
    private String ProductId, ProductName, Quantity, Price, Dicount;

    public Order() {
    }

    public Order(String productId, String productName, String quantity, String price, String dicount) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Dicount = dicount;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDicount() {
        return Dicount;
    }

    public void setDicount(String dicount) {
        Dicount = dicount;
    }
}
