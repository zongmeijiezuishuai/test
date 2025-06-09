package com.example.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessageDbHelear extends SQLiteOpenHelper {
    private static CarDbHelear sHelper;
    private static final String DB_NAME = "message.db";   //数据库名
    private static final int VERSION = 1;    //版本号

    // 表名
    public  static final String TABLE_FEEDBACK = "feedback";

    // 列名
    public  static final String COLUMN_ID = "id";
    public  static final String COLUMN_NAME = "name";
    public  static final String COLUMN_EMAIL = "email";
    public  static final String COLUMN_MESSAGE = "message";


    // 创建表的 SQL 语句
    private static final String CREATE_TABLE_FEEDBACK = "CREATE TABLE " + TABLE_FEEDBACK + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_EMAIL + " TEXT, "
            + COLUMN_MESSAGE + " TEXT " + ")";  // 默认值为 0（未读）

    public MessageDbHelear(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FEEDBACK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // 添加反馈
    public long addFeedback(String name, String email, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_MESSAGE, message);

        long id = db.insert(TABLE_FEEDBACK, null, values);
        db.close();
        return id;
    }

    // 获取所有反馈
    public Cursor getAllFeedbacks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_FEEDBACK, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_MESSAGE}, null, null, null, null, null);
    }

    // 标记为已读
//    public void markAsRead(int id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_READ, 1);  // 1 表示已读
//        db.update(TABLE_FEEDBACK, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
//        db.close();
//    }

    // 删除反馈
    public void deleteFeedback(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FEEDBACK, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }


}
