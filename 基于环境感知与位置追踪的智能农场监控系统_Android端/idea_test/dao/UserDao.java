package com.example.idea_test.dao;

import com.example.idea_test.entity.User;
import com.example.idea_test.utils.JDBCUtils;
import android.util.Log;

import java.sql.*;
import java.util.HashMap;

public class UserDao {
    private static final String TAG = "mysql-party-UserDao";

    public int login(String userAccount, String userPassword) {
        HashMap<String, Object> map = new HashMap<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int msg = 0;

        try {
            connection = JDBCUtils.getConn();
            String sql = "select * from user where userAccount = ?";
            if (connection != null) {
                ps = connection.prepareStatement(sql);
                ps.setString(1, userAccount);
                rs = ps.executeQuery();

                int count = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    for (int i = 1; i <= count; i++) {
                        String field = rs.getMetaData().getColumnName(i);
                        map.put(field, rs.getString(field));
                    }
                }

                if (map.size() != 0) {
                    for (String key : map.keySet()) {
                        if (key.equals("userPassword")) {
                            if (userPassword.equals(map.get(key))) {
                                msg = 1; // 密码正确
                            } else {
                                msg = 2; // 密码错误
                            }
                            break;
                        }
                    }
                } else {
                    Log.e(TAG, "查询结果为空");
                    msg = 3; // 账号不存在
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "异常login：" + e.getMessage());
            msg = 0;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                Log.e(TAG, "资源关闭异常：" + e.getMessage());
            }
        }
        return msg;
    }

    public int register(User user) {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            // 1. 获取数据库连接
            connection = JDBCUtils.getConn();
            if (connection == null) {
                Log.e(TAG, "数据库连接失败");
                return 0;
            }

            // 2. 检查账号是否已存在
            if (findUser(user.getUserAccount()) != null) {
                Log.w(TAG, "账号已存在: " + user.getUserAccount());
                return 2;
            }

            // 3. 准备SQL语句
            String sql = "INSERT INTO user(userAccount, userPassword, userName, userType, userState, userDel) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            // 4. 设置参数
            ps = connection.prepareStatement(sql);
            ps.setString(1, user.getUserAccount());
            ps.setString(2, user.getUserPassword());
            ps.setString(3, user.getUserName());
            ps.setInt(4, user.getUserType());
            ps.setInt(5, user.getUserState());
            ps.setInt(6, user.getUserDel());

            // 5. 执行插入
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                Log.i(TAG, "注册成功，账号: " + user.getUserAccount());
                return 1;
            } else {
                Log.e(TAG, "注册失败，影响行数: " + affectedRows);
                return 0;
            }

        } catch (SQLException e) {
            Log.e(TAG, "SQL异常: " + e.getMessage());
            return 0;
        } finally {
            // 关闭资源
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                Log.e(TAG, "关闭连接异常: " + e.getMessage());
            }
        }
    }

    public User findUser(String userAccount) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            connection = JDBCUtils.getConn();
            String sql = "select * from user where userAccount = ?";
            if (connection != null) {
                ps = connection.prepareStatement(sql);
                ps.setString(1, userAccount);
                rs = ps.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt(1);
                    String userAccount1 = rs.getString(2);
                    String userPassword = rs.getString(3);
                    String userName = rs.getString(4);
                    int userType = rs.getInt(5);
                    int userState = rs.getInt(6);
                    int userDel = rs.getInt(7);
                    user = new User(id, userAccount1, userPassword, userName, userType, userState, userDel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "异常findUser：" + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                Log.e(TAG, "资源关闭异常：" + e.getMessage());
            }
        }
        return user;
    }

    /**
     * 发送验证码
     */
    public boolean sendVerificationCode(String userAccount) {
        // 这里应该实现发送验证码到用户邮箱或手机的逻辑
        // 由于你没有提供用户邮箱或手机字段，这里简化处理
        // 实际项目中应该调用短信或邮件服务发送验证码
        //////////////////////////////////////////后面再添加/////////////////
        return findUser(userAccount) != null; // 如果用户存在则返回true
    }

    /**
     * 验证验证码
     */
    public boolean verifyCode(String userAccount, String code) {
        // 这里应该实现验证码验证逻辑
        // 简化处理，假设验证码是"123456"
        return "123456".equals(code);
    }

    /**
     * 重置密码
     */
    public int resetPassword(String userAccount, String newPassword) {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = JDBCUtils.getConn();
            String sql = "UPDATE user SET userPassword = ? WHERE userAccount = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setString(2, userAccount);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0 ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "异常resetPassword：" + e.getMessage());
            return 0;
        } finally {
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                Log.e(TAG, "资源关闭异常：" + e.getMessage());
            }
        }
    }
}