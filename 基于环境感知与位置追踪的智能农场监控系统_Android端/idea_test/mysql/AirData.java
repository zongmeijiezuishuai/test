package com.example.idea_test.mysql;

public class AirData {
    private int id;
    private String time;
    private float airValue;

    public AirData(int id, String time, float airValue) {
        this.id = id;
        this.time = time;
        this.airValue = airValue;
    }


    public float getValue() {
        return airValue;
    }

    public String getTime() {
        return time;

    }
}
