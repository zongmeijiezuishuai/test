package com.example.myapplication.entity;

import java.util.List;

public class OrderWithComments {
    private OrderInfo orderInfo;
    private List<CommentInfo> comments;

    public OrderWithComments(OrderInfo orderInfo, List<CommentInfo> comments) {
        this.orderInfo = orderInfo;
        this.comments = comments;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public List<CommentInfo> getComments() {
        return comments;
    }

    public void setComments(List<CommentInfo> comments) {
        this.comments = comments;
    }
}
