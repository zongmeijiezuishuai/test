package com.example.myapplication.entity;

import java.io.Serializable;

public class GoodsInfo implements Serializable {
    private int productId;
    private String productImg;
    private String productTitle;
    private String productDetails;
    private int productPrice;
    private int productCount;
    private String type;

    // 无参构造函数
    public GoodsInfo() {
    }

    // 带参数的构造函数
    public GoodsInfo(int productId, String productImg, String productTitle, String productDetails, int productPrice, int productCount, String type) {
        this.productId = productId;
        this.productImg = productImg;
        this.productTitle = productTitle;
        this.productDetails = productDetails;
        this.productPrice = productPrice;
        this.productCount = productCount;
        this.type = type;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}