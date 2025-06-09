package com.example.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.example.myapplication.entity.GoodsInfo;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GoodsDbHelear extends SQLiteOpenHelper {

    private static GoodsDbHelear sHelper;
    private static final String DB_NAME = "goods_info.db";   //数据库名
    private static final int VERSION = 2;    //版本号

    private static final String COLUMN_PRODUCT_ID = "productId";
    private static final String COLUMN_PRODUCT_IMG = "productImg";
    private static final String COLUMN_PRODUCT_TITLE = "productTitle";
    private static final String COLUMN_PRODUCT_DETAILS = "productDetails";
    private static final String COLUMN_PRODUCT_PRICE = "productPrice";
    private static final String COLUMN_PRODUCT_COUNT = "productCount";
    private static final String COLUMN_TYPE = "type";


    private static volatile GoodsDbHelear instance;

//    public static GoodsDbHelear getInstance(Context context) {
//        if (instance == null) {
//            synchronized (GoodsDbHelear.class) {
//                if (instance == null) {
//                    instance = new GoodsDbHelear(context.getApplicationContext());
//                }
//            }
//        }
//        return instance;
//    }



    //必须实现其中一个构方法
    public GoodsDbHelear(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //创建单例，供使用调用该类里面的的增删改查的方法
    public synchronized static GoodsDbHelear getInstance(Context context) {
        if (null == sHelper) {
            sHelper = new GoodsDbHelear(context, DB_NAME, null, VERSION);
        }
        return sHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建goods_table表
        db.execSQL("CREATE TABLE goods_table (" +
                "productId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "productImg TEXT, " +
                "productTitle TEXT, " +
                "productDetails TEXT, " +
                "productPrice INTEGER, " +
                "productCount INTEGER, " +
                "type TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // 在数据库版本更新时执行的操作
        if (oldVersion < 2) {
            // 删除旧表
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS goods_table");
            // 重新创建表
            onCreate(sqLiteDatabase);
        }
    }

    /**
     * 添加商品或更新商品数量
     */
    public int addProduct(String productImg, String productTitle, String productDetails, int productPrice, int productCount, String type) {
        SQLiteDatabase db = getWritableDatabase();
        int result = 0;

        // 查询是否已有相同的商品（基于商品名称或者其它唯一标识符）
        Cursor cursor = db.query("goods_table", null, "productTitle = ?", new String[]{productTitle}, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                // 商品已存在，更新商品数量
                int productCountIndex = cursor.getColumnIndex("productCount");
                if (productCountIndex != -1) {
                    // 获取现有的商品数量并加上新传入的商品数量
                    int newProductCount = cursor.getInt(productCountIndex) + productCount;

                    ContentValues values = new ContentValues();
                    values.put("productCount", newProductCount);

                    // 执行更新操作
                    int rowsAffected = db.update("goods_table", values, "productTitle = ?", new String[]{productTitle});
                    result = rowsAffected;  // 返回更新的行数
                } else {
                    result = -1;  // 未找到productCount列，返回错误
                }
            } else {
                // 商品不存在，插入新的商品记录
                ContentValues values = new ContentValues();
                values.put("productImg", productImg);
                values.put("productTitle", productTitle);
                values.put("productDetails", productDetails);
                values.put("productPrice", productPrice);
                values.put("productCount", productCount);
                values.put("type", type);

                // 执行插入操作
                long insert = db.insert("goods_table", null, values);
                if (insert != -1) {
                    result = 1;  // 插入成功
                } else {
                    result = -2;  // 插入失败
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return result;
    }

    /**
     * 根据商品名模糊查询
     */
    public Cursor searchProductsByName(String productTitle) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            // 使用 LIKE 进行模糊查询，%表示任意字符
            String query = "SELECT * FROM goods_table WHERE productTitle LIKE ?";
            // 将商品名包装为模糊查询的条件，前后加上 % 表示任意字符
            String[] selectionArgs = new String[] { "%" + productTitle + "%" };

            // 执行查询
            cursor = db.rawQuery(query, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace(); // 捕获异常，便于调试
        }

        return cursor; // 返回查询结果的 Cursor
    }

    /**
     * 根据type来查询商品
     */

    public List<GoodsInfo> getAllGoodsInfoByType(String type) {
        List<GoodsInfo> goodsInfoList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM goods_table WHERE type = ?";
        Cursor cursor = db.rawQuery(query, new String[]{type});

        while (cursor.moveToNext()) {
            GoodsInfo goodsInfo = new GoodsInfo();
            goodsInfo.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow("productId")));
            goodsInfo.setProductImg(cursor.getString(cursor.getColumnIndexOrThrow("productImg")));
            goodsInfo.setProductTitle(cursor.getString(cursor.getColumnIndexOrThrow("productTitle")));
            goodsInfo.setProductDetails(cursor.getString(cursor.getColumnIndexOrThrow("productDetails")));
            goodsInfo.setProductPrice(cursor.getInt(cursor.getColumnIndexOrThrow("productPrice"))); // 确保数据库中有这个字段
            goodsInfo.setProductCount(cursor.getInt(cursor.getColumnIndexOrThrow("productCount")));
            goodsInfo.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
            goodsInfoList.add(goodsInfo);
        }
        cursor.close();
        return goodsInfoList;
    }

    /**
     *更新修改商品
     */

//    public int updateProduct(int productId, String productImg, String productTitle, String productDetails, int productPrice, int productCount, String type) {
//        SQLiteDatabase db = getWritableDatabase();
//        int result = 0;
//
//        ContentValues values = new ContentValues();
//        values.put("productImg", productImg);
//        values.put("productTitle", productTitle);
//        values.put("productDetails", productDetails);
//        values.put("productPrice", productPrice);
//        values.put("productCount", productCount);
//        values.put("type", type);
//
//        // 执行更新操作
//        int rowsAffected = db.update("goods_table", values, "productId = ?", new String[]{String.valueOf(productId)});
//        if (rowsAffected > 0) {
//            result = rowsAffected;  // 更新成功
//        } else {
//            result = 0;  // 更新失败
//        }
//
//        db.close();
//        return result;
//    }


    public int updateProduct(int productId, String productImg, String productTitle, String productDetails, int productPrice, int productCount, String type) {
        SQLiteDatabase db = getWritableDatabase();
        int result = 0;

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_IMG, productImg);
        values.put(COLUMN_PRODUCT_TITLE, productTitle);
        values.put(COLUMN_PRODUCT_DETAILS, productDetails);
        values.put(COLUMN_PRODUCT_PRICE, productPrice);
        values.put(COLUMN_PRODUCT_COUNT, productCount);
        values.put(COLUMN_TYPE, type);

        // 执行更新操作
        int rowsAffected = db.update("goods_table", values, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
        if (rowsAffected > 0) {
            result = rowsAffected;  // 更新成功
        } else {
            result = 0;  // 更新失败
        }

        db.close();
        return result;
    }

    /**
     * 删除商品
     */
//    public boolean delete(String productId) {
//        if (TextUtils.isEmpty(productId)) {
//            Log.e("GoodsDbHelear", "Invalid product ID provided for deletion.");
//            return false;
//        }
//
//        SQLiteDatabase db = null;
//        try {
//            db = getWritableDatabase();
//            int affectedRows = db.delete("goods_table", "productId=?", new String[]{productId});
//            return affectedRows > 0;
//        } catch (SQLException e) {
//            Log.e("GoodsDbHelear", "Error deleting product with ID: " + productId, e);
//            return false;
//        } finally {
//            if (db != null && db.isOpen()) {
//                // 只有当数据库确实处于打开状态时才关闭它
//                db.close();
//            }
//        }
//    }
    public int delete(String productId) {
        // 获取可写的SQLiteDatabase实例
        SQLiteDatabase db = getWritableDatabase();

        // 执行删除操作，删除指定 productId 的商品
        int delete = db.delete("goods_table", "productId=?", new String[]{productId});

        // 关闭数据库连接
        db.close();

        // 返回受影响的行数
        return delete;
    }


}
