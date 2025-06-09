package com.example.idea_test.mysql;

public class LightData {
    private int id;
    private String time;
    private float lightValue;

    public LightData(int id, String time, float lightValue) {
        this.id = id;
        this.time = time;
        this.lightValue = lightValue;
    }


    public float getValue() {
        return lightValue;
    }

    public String getTime() {
        return time;

    }
}
