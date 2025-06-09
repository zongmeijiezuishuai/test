package com.example.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.entity.SystemNotification;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoticeDbHelear extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notice.db";
    private static final int DATABASE_VERSION = 1;

    // 表名
    public static final String TABLE_SYSTEM_NOTIFICATIONS = "system_notifications";

    // 列名
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PUBLISHER = "publisher";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_CONTENT = "content";

    // 创建表的 SQL 语句
    private static final String CREATE_TABLE_SYSTEM_NOTIFICATIONS =
            "CREATE TABLE " + TABLE_SYSTEM_NOTIFICATIONS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_PUBLISHER + " TEXT, "
                    + COLUMN_TIMESTAMP + " TEXT, "
                    + COLUMN_CONTENT + " TEXT"
                    + ")";

    public NoticeDbHelear(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SYSTEM_NOTIFICATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYSTEM_NOTIFICATIONS);
        onCreate(db);
    }

    // 添加系统通知
//    public long addSystemNotification(String publisher, String content) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_PUBLISHER, publisher);
//        values.put(COLUMN_TIMESTAMP, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//        values.put(COLUMN_CONTENT, content);
//
//        return db.insert(TABLE_SYSTEM_NOTIFICATIONS, null, values);
//    }

    public long addSystemNotification(String publisher, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PUBLISHER, publisher);

        // 使用 java.time 获取当前时间并格式化
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = now.format(formatter);

        values.put(COLUMN_TIMESTAMP, formattedTimestamp);
        values.put(COLUMN_CONTENT, content);

        return db.insert(TABLE_SYSTEM_NOTIFICATIONS, null, values);
    }

    // 获取所有系统通知
    public List<SystemNotification> getAllSystemNotifications() {
        List<SystemNotification> notifications = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_SYSTEM_NOTIFICATIONS,
                new String[]{COLUMN_ID, COLUMN_PUBLISHER, COLUMN_TIMESTAMP, COLUMN_CONTENT},
                null, null, null, null, COLUMN_TIMESTAMP + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                SystemNotification notification = new SystemNotification(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PUBLISHER)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))
                );
                notifications.add(notification);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return notifications;
    }

    public boolean deleteSystemNotification(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_SYSTEM_NOTIFICATIONS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }
}
