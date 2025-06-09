package com.example.idea_test.Handler;

import com.example.idea_test.mysql.*;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MySQLDataFetcher {
    private static final String URL = "jdbc:mysql://192.168.228.1:3306/onenet?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "Dzm6339415";
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<TempData> getAllTempData() {
        List<TempData> dataList = new ArrayList<>();
        String sql = "SELECT * FROM temp ORDER BY time ASC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String time = rs.getString("time");
                float value = rs.getFloat("temp_value");
                dataList.add(new TempData(id, time, value));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    // 其他数据表的查询方法类似...
    public static List<HumiData> getAllHumiData() {
        List<HumiData> dataList = new ArrayList<>();
        String sql = "SELECT * FROM humi ORDER BY time ASC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String time = rs.getString("time");
                float value = rs.getFloat("humi_value");
                dataList.add(new HumiData(id, time, value));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }
    public static List<AirData> getAllAirData() {
        List<AirData> dataList = new ArrayList<>();
        String sql = "SELECT * FROM air ORDER BY time ASC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String time = rs.getString("time");
                float value = rs.getFloat("air_value");
                dataList.add(new AirData(id, time, value));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }
    public static List<FlameData> getAllFlameData() {
        List<FlameData> dataList = new ArrayList<>();
        String sql = "SELECT * FROM flame ORDER BY time ASC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String time = rs.getString("time");
                float value = rs.getFloat("flame_value");
                dataList.add(new FlameData(id, time, value));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }
    public static List<LightData> getAllLightData() {
        List<LightData> dataList = new ArrayList<>();
        String sql = "SELECT * FROM light ORDER BY time ASC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String time = rs.getString("time");
                float value = rs.getFloat("light_value");
                dataList.add(new LightData(id, time, value));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }
    public static List<SoilData> getAllSoilData() {
        List<SoilData> dataList = new ArrayList<>();
        String sql = "SELECT * FROM soil ORDER BY time ASC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String time = rs.getString("time");
                float value = rs.getFloat("soil_value");
                dataList.add(new SoilData(id, time, value));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }
//
}