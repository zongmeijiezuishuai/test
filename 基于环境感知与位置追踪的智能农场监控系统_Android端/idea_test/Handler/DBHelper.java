package com.example.idea_test.Handler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.idea_test.mysql.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    private static final String URL = "jdbc:mysql://192.168.228.1:3306/onenet?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "Dzm6339415";

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 插入温度数据
    public static void insertTempData(float value) throws SQLException {
        String sql = "INSERT INTO temp (temp_value, time) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setFloat(1, value);
            pstmt.setString(2, getCurrentTime());
            pstmt.executeUpdate();
        }
    }
    // 插入湿度数据
    public static void insertHumiData(float value) throws SQLException {
        String sql = "INSERT INTO humi (humi_value, time) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setFloat(1, value);
            pstmt.setString(2, getCurrentTime());
            pstmt.executeUpdate();
        }
    }
    //插入空气质量的值
    public static void insertAirData(float value) throws SQLException {
        String sql = "INSERT INTO air (air_value, time) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setFloat(1, value);
            pstmt.setString(2, getCurrentTime());
            pstmt.executeUpdate();
        }
    }
    //插入火焰值
    public static void insertFlameData(float value) throws SQLException {
        String sql = "INSERT INTO flame (flame_value, time) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setFloat(1, value);
            pstmt.setString(2, getCurrentTime());
            pstmt.executeUpdate();
        }
    }
    //插入光照值
    public static void insertHightData(float value) throws SQLException {
        String sql = "INSERT INTO lihgt (light_value, time) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setFloat(1, value);
            pstmt.setString(2, getCurrentTime());
            pstmt.executeUpdate();
        }
    }
    //插入土壤湿度的值
    public static void insertSoilData(float value) throws SQLException {
        String sql = "INSERT INTO soil (soil_value, time) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setFloat(1, value);
            pstmt.setString(2, getCurrentTime());
            pstmt.executeUpdate();
        }
    }

    public static void insertData(String tableName, float value) throws SQLException {
        String columnName = tableName + "_value"; // 例如：air -> air_value
        String sql = "INSERT INTO " + tableName + " (" + columnName + ", time) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setFloat(1, value);
            pstmt.setString(2, getCurrentTime());
            pstmt.executeUpdate();
        }
    }

    // 获取当前精确时间
    public static String getCurrentTime() {
        return LocalDateTime.now().format(TIME_FORMATTER);
    }

}