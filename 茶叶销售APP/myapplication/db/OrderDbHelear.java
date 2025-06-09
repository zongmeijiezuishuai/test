package com.example.myapplication.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.adapter.OrderWithComments;
import com.example.myapplication.entity.CarInfo;
import com.example.myapplication.entity.CommentInfo;
import com.example.myapplication.entity.OrderInfo;

import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class OrderDbHelear extends SQLiteOpenHelper {
    private static OrderDbHelear sHelper;
    private static final String DB_NAME = "order.db";   //数据库名
    private static final int VERSION = 3;    //版本号

    //必须实现其中一个构方法
    public OrderDbHelear(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public OrderDbHelear(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public synchronized static OrderDbHelear getInstance(Context context) {
        if (null == sHelper) {
            sHelper = new OrderDbHelear(context, DB_NAME, null, VERSION);
        }
        return sHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建订单表
        db.execSQL("CREATE TABLE IF NOT EXISTS order_table (" +
                "order_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +       // 用户名
                "product_img INTEGER, " +  // 商品图片资源ID
                "product_title TEXT, " +
                "product_price REAL, " +  // 更正为REAL类型以适应价格的小数部分
                "product_count INTEGER, " +
                "address TEXT, " +
                "mobile TEXT," +
                "order_status TEXT, " +                // 订单状态
                "order_number TEXT, " +                // 订单号
                "order_time DATETIME, " +              // 下单时间
                "delivery_time DATETIME"  +            // 收货时间
                ")");

        // 创建评论表
        db.execSQL("CREATE TABLE IF NOT EXISTS comments_table (" +
                "comment_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_id INTEGER, " +     // 关联到订单表的外键
                "user_name TEXT, " +       // 发布评论的用户名
                "content TEXT, " +         // 评论内容
                "rating REAL, " +          // 评分
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " + // 评论创建时间
                "FOREIGN KEY(order_id) REFERENCES order_table(order_id)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 当版本号增加时调用此方法，可以在这里处理数据库升级逻辑
        // 在数据库版本更新时执行的操作
        if (oldVersion < 3) {
            // 删除旧表
            db.execSQL("DROP TABLE IF EXISTS order_table");
            // 重新创建表
            onCreate(db);
        }
    }

    /**
     * 批量插入数据
     */

    public void insertByAll(List<CarInfo> list, String address, String mobile) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (CarInfo carInfo : list) {
                ContentValues values = new ContentValues();
                values.put("username", carInfo.getUsername());
                values.put("product_img", carInfo.getProduct_img());
                values.put("product_title", carInfo.getProduct_title());
                values.put("product_price", (int)(carInfo.getProduct_price()));
                values.put("product_count", carInfo.getProduct_count());
                values.put("address", address);
                values.put("mobile", mobile);
                values.put("order_status", "待发货"); // 默认订单状态
                values.put("order_number", generateOrderNumber()); // 生成订单号
                values.put("order_time", getCurrentDateTime()); // 获取当前时间
                // 可选: 设置 delivery_time 为 null 或者一个默认值
                db.insert("order_table", null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    private String generateOrderNumber() {
        // 实现生成唯一订单号的逻辑
        return UUID.randomUUID().toString(); // 示例实现
    }

    /**
     * 获取所有注册用户
     */
    @SuppressLint("Range")
    public List<OrderInfo> queryOrderListData(String username) {
        // 获取SQLiteDatabase实例
        SQLiteDatabase db = getReadableDatabase();
        List<OrderInfo> list = new ArrayList<>();

        // 修改查询语句以包含所有必要的字段
        String sql = "SELECT order_id, username, product_img, product_title, product_price, product_count, address, mobile, order_status, order_number, order_time, delivery_time FROM order_table WHERE username = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        while (cursor.moveToNext()) {
            OrderInfo orderInfo;
            orderInfo = new OrderInfo(
                    cursor.getInt(cursor.getColumnIndex("order_id")),
                    cursor.getString(cursor.getColumnIndex("username")),
                    cursor.getInt(cursor.getColumnIndex("product_img")),
                    cursor.getString(cursor.getColumnIndex("product_title")),
                    cursor.getInt(cursor.getColumnIndex("product_price")),
                    cursor.getInt(cursor.getColumnIndex("product_count")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getString(cursor.getColumnIndex("mobile")),
                    cursor.getString(cursor.getColumnIndex("order_status")),
                    cursor.getString(cursor.getColumnIndex("order_number")),
                    stringToDate(cursor.getString(cursor.getColumnIndex("order_time"))),
                    stringToDate(cursor.getString(cursor.getColumnIndex("delivery_time")))
            );
            list.add(orderInfo);
        }

        cursor.close();
        db.close();
        return list;
    }

    private Date stringToDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return dateString == null ? null : dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除订单商品
     */

    public int delete(String order_id) {
        //获取SQLiteDatabase实例
        SQLiteDatabase db = getWritableDatabase();
        // 执行SQL
        int delete = db.delete("order_table", " order_id=?", new String[]{order_id});
        // 关闭数据库连接
        db.close();
        return delete;
    }

    /**
     * 关联评论查询
     */
    public List<OrderWithComments> fetchOrdersWithComments() {
        SQLiteDatabase db = getReadableDatabase();
        List<OrderWithComments> ordersWithComments = new ArrayList<>();

        // 使用左连接（LEFT JOIN）以确保即使没有评论的订单也会被列出
        String query = "SELECT o.order_id, o.username, o.product_img, o.product_title, o.product_price, o.product_count, o.address, o.mobile, o.order_status, o.order_number, o.order_time, o.delivery_time, " +
                "c.comment_id, c.user_name, c.content, c.rating, c.created_at " +
                "FROM order_table o " +
                "LEFT JOIN comments_table c ON o.order_id = c.order_id";

        Cursor cursor = db.rawQuery(query, null);

        // 用于缓存已经处理过的订单，避免重复创建 OrderInfo 对象
        Map<Integer, OrderWithComments> orderCache = new HashMap<>();

        try {
            while (cursor.moveToNext()) {
                int orderId = cursor.getInt(cursor.getColumnIndexOrThrow("order_id"));
                OrderWithComments orderWithComments = orderCache.get(orderId);

                if (orderWithComments == null) {
                    // 如果是第一次遇到这个订单，则创建一个新的 OrderInfo 对象
                    orderWithComments = new OrderWithComments(
                            new OrderInfo(
                                    cursor.getInt(cursor.getColumnIndexOrThrow("order_id")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("username")),
                                    cursor.getInt(cursor.getColumnIndexOrThrow("product_img")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("product_title")),
                                    cursor.getInt(cursor.getColumnIndexOrThrow("product_price")),
                                    cursor.getInt(cursor.getColumnIndexOrThrow("product_count")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("address")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("mobile")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("order_status")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("order_number")),
                                    stringToDate(cursor.getString(cursor.getColumnIndexOrThrow("order_time"))),
                                    stringToDate(cursor.getString(cursor.getColumnIndexOrThrow("delivery_time")))
                            ),
                            new ArrayList<>()
                    );
                    orderCache.put(orderId, orderWithComments);
                    ordersWithComments.add(orderWithComments);
                }

                // 检查是否有评论数据
                if (!cursor.isNull(cursor.getColumnIndexOrThrow("comment_id"))) {
                    // 如果有评论，则创建 CommentInfo 对象并添加到订单的评论列表中
                    orderWithComments.getComments().add(new CommentInfo(
                            cursor.getInt(cursor.getColumnIndexOrThrow("comment_id")),
                            orderId,
                            cursor.getString(cursor.getColumnIndexOrThrow("user_name")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("rating")),
                            cursor.getString(cursor.getColumnIndexOrThrow("content")),
                            cursor.getString(cursor.getColumnIndexOrThrow("created_at"))
                    ));
                }
            }
        } finally {
            cursor.close();
            db.close();
        }

        return ordersWithComments;
    }

    /**
     * 获取所有订单
     */

    @SuppressLint("Range")
    public List<OrderInfo> queryAllOrderListData() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<OrderInfo> orderInfos = new ArrayList<>();

        String[] projection = {
                "order_id", "username", "product_img", "product_title", "product_price", "product_count",
                "address", "mobile", "order_status", "order_number", "order_time", "delivery_time"
        };

        Cursor cursor = db.query(
                "order_table",  // 表名
                projection,     // 列 - 指定要查询的列
                null,           // WHERE 子句 - null 表示没有过滤条件
                null,           // WHERE 参数
                null,           // GROUP BY
                null,           // HAVING
                null            // ORDER BY
        );

        while (cursor.moveToNext()) {
            OrderInfo orderInfo = new OrderInfo(
                    cursor.getInt(cursor.getColumnIndex("order_id")),
                    cursor.getString(cursor.getColumnIndex("username")),
                    cursor.getInt(cursor.getColumnIndex("product_img")),
                    cursor.getString(cursor.getColumnIndex("product_title")),
                    cursor.getInt(cursor.getColumnIndex("product_price")), // 使用 getInt
                    cursor.getInt(cursor.getColumnIndex("product_count")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getString(cursor.getColumnIndex("mobile")),
                    cursor.getString(cursor.getColumnIndex("order_status")),
                    cursor.getString(cursor.getColumnIndex("order_number")),
                    stringToDate(cursor.getString(cursor.getColumnIndex("order_time"))),
                    stringToDate(cursor.getString(cursor.getColumnIndex("delivery_time")))
            );
            orderInfos.add(orderInfo);
        }

        cursor.close();
        db.close();
        return orderInfos;
    }


    private static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    /**
     * 更新订单状态
     */

    public boolean updateOrderStatus(int orderId, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order_status", status);

        // Updating row
        return db.update("order_table", values, "order_id = ?", new String[]{String.valueOf(orderId)}) > 0;
    }

    /**
     * 收货时间
     */
//    private void updateOrderStatusInDatabase(String deliveryDate) {
//        // 获取可写数据库
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        // 创建一个 ContentValues 对象，用于存储要更新的内容
//        ContentValues values = new ContentValues();
//        values.put("delivery_time", deliveryDate);  // 更新收货时间
//        values.put("order_status", "已收货");      // 更新订单状态为“已收货”
//
//        // 使用 order_id 更新数据库中的订单信息
//        int rowsAffected = db.update("order_table", values, "order_id = ?", new String[]{String.valueOf(orderId)});
//
//        if (rowsAffected > 0) {
//            Log.d("Database", "订单状态更新成功");
//        } else {
//            Log.d("Database", "订单状态更新失败");
//        }
//    }


//    public void updateOrderStatusInDatabase(String deliveryDate) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put("delivery_time", deliveryDate);  // 更新收货时间
//        values.put("order_status", "已收货");      // 更新订单状态为已收货
//
//        // 使用 "order_id" 字段进行查询和更新
//        int rowsAffected = db.update("order_table", values, "order_id = ?", new String[]{String.valueOf(orderId)});
//
//        if (rowsAffected > 0) {
//            Log.d("Database", "订单状态更新成功");
//        } else {
//            Log.d("Database", "订单状态更新失败");
//        }
//    }

    public void insertOrder(OrderInfo orderInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("username", orderInfo.getUsername());
        values.put("product_img", orderInfo.getProduct_img());
        values.put("product_title", orderInfo.getProduct_title());
        values.put("product_price", orderInfo.getProduct_price());
        values.put("product_count", orderInfo.getProduct_count());
        values.put("address", orderInfo.getAddress());
        values.put("mobile", orderInfo.getMobile());
        values.put("order_status", orderInfo.getOrderStatus());
        values.put("order_number", orderInfo.getOrderNumber());
        values.put("order_time", orderInfo.getOrderTime().toString()); // 转为字符串
        values.put("delivery_time", orderInfo.getDeliveryTime().toString()); // 转为字符串

        long orderId = db.insert("order_table", null, values);
        if (orderId != -1) {
            Log.d("Database", "订单插入成功，订单 ID: " + orderId);
        } else {
            Log.d("Database", "订单插入失败");
        }
    }



}
