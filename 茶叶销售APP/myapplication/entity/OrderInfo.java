package com.example.myapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class OrderInfo implements Serializable {
    private int order_id;
    private String username;
    private int product_img;
    private String product_title;
    private int product_price;
    private int product_count;
    private String address;
    private String mobile;
    private String orderStatus; // 订单状态
    private String orderNumber; // 订单号
    private Date orderTime; // 下单时间
    private Date deliveryTime; // 收货时间
    private transient String cachedOrderTimeFormatted;

    public OrderInfo(int order_id, String username, int product_img, String product_title, int product_price, int product_count, String address, String mobile,String orderStatus, String orderNumber, Date orderTime, Date deliveryTime) {
        this.order_id = order_id;
        this.username = username;
        this.product_img = product_img;
        this.product_title = product_title;
        this.product_price = product_price;
        this.product_count = product_count;
        this.address = address;
        this.mobile = mobile;
        this.orderStatus = orderStatus;
        this.orderNumber = orderNumber;
        this.orderTime = orderTime;
        this.deliveryTime = deliveryTime;
        generateOrderNumber();
        setOrderTime(new Date()); // 设置当前时间为下单时间
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    private void generateOrderNumber() {
        // 使用 ThreadLocalRandom 生成一个11位的随机数
        long randomNumber = ThreadLocalRandom.current().nextLong(10_000_000_000L, 100_000_000_000L);
        this.orderNumber = String.format("%011d", randomNumber);
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderTimeFormatted() {
        if (cachedOrderTimeFormatted == null && orderTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            cachedOrderTimeFormatted = sdf.format(orderTime);
        }
        return cachedOrderTimeFormatted;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryTimeFormatted() {
        if (deliveryTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(deliveryTime);
        }
        return null;
    }


    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getProduct_img() {
        return product_img;
    }

    public void setProduct_img(int product_img) {
        this.product_img = product_img;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public int getProduct_count() {
        return product_count;
    }

    public void setProduct_count(int product_count) {
        this.product_count = product_count;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
