package com.example.idea_test.mysql;

public class SoilData {
    private int id;
    private String time;
    private float soilValue;

    public SoilData(int id, String time, float soilValue) {
        this.id = id;
        this.time = time;
        this.soilValue = soilValue;
    }


    public float getValue() {
        return soilValue;
    }

    public String getTime() {
        return time;

    }
}
