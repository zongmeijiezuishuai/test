package com.example.myapplication.entity;

public class SystemNotification {
    private int id;
    private String publisher;
    private String timestamp;
    private String content;

    public SystemNotification(int id, String publisher, String timestamp, String content) {
        this.id = id;
        this.publisher = publisher;
        this.timestamp = timestamp;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
