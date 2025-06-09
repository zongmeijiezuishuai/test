package com.example.idea_test.mysql;

public class HumiData {
    private int id;
    private String time;
    private float humiValue;

    public HumiData(int id, String time, float humiValue) {
        this.id = id;
        this.time = time;
        this.humiValue = humiValue;
    }


    public float getValue() {
        return humiValue;
    }

    public String getTime() {
        return time;

    }
}
