package com.example.idea_test.mysql;

public class FlameData {
    private int id;
    private String time;
    private float flameValue;

    public FlameData(int id, String time, float flameValue) {
        this.id = id;
        this.time = time;
        this.flameValue = flameValue;
    }


    public float getValue() {
        return flameValue;
    }

    public String getTime() {
        return time;

    }
}
