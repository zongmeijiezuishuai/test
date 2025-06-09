package com.example.idea_test.mysql;

public class TempData {
    private int id;
    private String time;
    private float tempValue;

    public TempData(int id, String time, float tempValue) {
        this.id = id;
        this.time = time;
        this.tempValue = tempValue;
    }


    public float getValue() {
        return tempValue;
    }

    public String getTime() {
        return time;

    }
}