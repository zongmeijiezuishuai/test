package com.example.myapplication.adapter;

import com.example.myapplication.entity.CommentInfo;
import com.example.myapplication.entity.OrderInfo;

import java.util.List;

// 新增类 OrderWithComments 来封装订单和对应的评论集合
public class OrderWithComments {
    private OrderInfo orderInfo;
    private List<CommentInfo> comments;
    private int order_id;

    public OrderWithComments(OrderInfo orderInfo, List<CommentInfo> comments) {
        this.orderInfo = orderInfo;
        this.comments = comments;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public List<CommentInfo> getComments() {
        return comments;
    }

    public int getOrderId() {
        return 0;
    }

    public void setComments(List<CommentInfo> comments) {
    }
}
