package com.example.myapplication;

public class FeedbackItem {

    private int id;
    private String name;
    private String email;
    private String message;

    public FeedbackItem(int id, String name, String email, String message) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.message = message;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMessage() { return message; }

}
