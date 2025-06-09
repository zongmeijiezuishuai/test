package com.example.idea_test.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtils {
    private static final String TAG = "mysql-party-JDBCUtils";

    private static String driver = "com.mysql.jdbc.Driver";// MySql驱动

    private static String dbName = "onenet";// 数据库名称

    private static String user = "root";// 用户名

    private static String password = "Dzm6339415";// 密码

    public static Connection getConn(){

        Connection connection = null;
        try{
            Class.forName(driver);// 动态加载类
            String ip = "192.168.228.1";// 写成本机地址，不能写成localhost，同时手机和电脑连接的网络必须是同一个

            // 尝试建立到给定数据库URL的连接
            //此时，我连到的是liuliu_5G
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.228.1:3306/onenet?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true","root","Dzm6339415");

        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

}
