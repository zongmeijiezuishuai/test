package com.example.myapplication.entity;

public class SaleProduct {
    private int id;
    private String name;
    private int originalPrice;
    private int discountedPrice;
    private int imageResourceId;

    public SaleProduct(int id, String name, int originalPrice, int discountedPrice, int imageResourceId) {
        this.id = id;
        this.name = name;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.imageResourceId = imageResourceId;
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

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(int discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
