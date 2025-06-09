package com.example.myapplication.entity;

public class CommentInfo {
    private int comment_id;
    private int order_id;
    private String user_name;
    private String content;
    private double rating; // 使用 double 或 float 来存储评分值
    private String created_at; // 保存评论创建时间

    public CommentInfo(int comment_id, int order_id, String user_name, double rating, String content, String created_at) {
        this.comment_id = comment_id;
        this.order_id = order_id;
        this.user_name = user_name;
        this.rating = rating;
        this.content = content;
        this.created_at = created_at;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
