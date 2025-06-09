package com.example.idea_test.info;

public class DeviceData {
    private double temperature;
    private double humidity;
    private double flame;
    private double airQuality;
    private String timestamp;

    // 构造函数、getter和setter方法
    public DeviceData(double temperature, double humidity, double flame, double airQuality, String timestamp) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.flame = flame;
        this.airQuality = airQuality;
        this.timestamp = timestamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getFlame() {
        return flame;
    }

    public void setFlame(double flame) {
        this.flame = flame;
    }

    public double getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(double airQuality) {
        this.airQuality = airQuality;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}