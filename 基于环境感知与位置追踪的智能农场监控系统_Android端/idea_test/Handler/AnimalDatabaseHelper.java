package com.example.idea_test.Handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.idea_test.entity.Animal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnimalDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "AnimalDatabase";
    private static final String DATABASE_NAME = "onenet.db";
    private static final int DATABASE_VERSION = 3;

    // 表名和列名
    public static final String TABLE_ANIMALS = "animals";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_RFID = "rfid";
    public static final String COLUMN_BREED = "breed";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_ENTRY_DATE = "entry_date";
    public static final String COLUMN_STATUS = "status";

    // 创建表的SQL语句（根据您提供的字段要求）
    private static final String CREATE_TABLE_ANIMALS =
            "CREATE TABLE " + TABLE_ANIMALS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " VARCHAR(255) NOT NULL, " +
                    COLUMN_RFID + " VARCHAR(255) NOT NULL, " +
                    COLUMN_BREED + " VARCHAR(255), " +
                    COLUMN_AGE + " INTEGER, " +
                    COLUMN_GENDER + " VARCHAR(255), " +
                    COLUMN_ENTRY_DATE + " DATETIME, " +
                    COLUMN_STATUS + " VARCHAR(255))";

    public AnimalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ANIMALS);
        Log.d(TAG, "动物表创建完成");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 在数据库版本更新时执行的操作
        if (oldVersion < DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMALS);
            onCreate(db);
            Log.d(TAG, "数据库升级完成：表已重建");
        }

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMALS);
        onCreate(db);
        Log.d(TAG, "数据库降级：表已重建");
    }


    // 插入动物数据
    public long insertAnimal(Animal animal) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, animal.getName());
        values.put(COLUMN_RFID, animal.getRfid());
        values.put(COLUMN_BREED, animal.getBreed());
        values.put(COLUMN_AGE, animal.getAge());
        values.put(COLUMN_GENDER, animal.getGender());
        values.put(COLUMN_ENTRY_DATE, animal.getEntryDate().getTime()); // 存储时间戳
        values.put(COLUMN_STATUS, animal.getStatus());

        long id = db.insert(TABLE_ANIMALS, null, values);
        db.close();

        Log.d(TAG, "插入动物数据: " + (id != -1 ? "成功, ID=" + id : "失败"));
        return id;
    }

    // 获取所有动物
    public List<Animal> getAllAnimals() {
        List<Animal> animals = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_ANIMALS,
                null, // 所有列
                null, null, null, null,
                COLUMN_ENTRY_DATE + " DESC" // 按入库时间倒序
        );

        if (cursor.moveToFirst()) {
            do {
                Animal animal = new Animal();
                animal.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                animal.setRfid(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RFID)));
                animal.setBreed(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BREED)));
                animal.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)));
                animal.setGender(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER)));

                // 转换时间戳为Date对象
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ENTRY_DATE));
                animal.setEntryDate(new Date(timestamp));

                animal.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));

                animals.add(animal);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.d(TAG, "从数据库加载动物数量: " + animals.size());
        return animals;
    }

    // 根据RFID删除动物
    public int deleteAnimal(String rfid) {
        SQLiteDatabase db = getWritableDatabase();
        int count = db.delete(
                TABLE_ANIMALS,
                COLUMN_RFID + " = ?",
                new String[]{rfid}
        );
        db.close();
        Log.d(TAG, "删除RFID为" + rfid + "的动物: " + (count > 0 ? "成功" : "未找到"));
        return count;
    }

    // 检查RFID是否已存在
    public boolean isRfidExists(String rfid) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_ANIMALS,
                new String[]{COLUMN_RFID},
                COLUMN_RFID + " = ?",
                new String[]{rfid},
                null, null, null
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean checkAnimalNameExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ANIMALS,
                new String[]{COLUMN_NAME},
                COLUMN_NAME + "=?",
                new String[]{name},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkRfidExists(String rfid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ANIMALS,
                new String[]{COLUMN_RFID},
                COLUMN_RFID + "=?",
                new String[]{rfid},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // 检查名称是否被其他动物使用（排除当前编辑的动物）
    public boolean checkAnimalNameExistsExcludingCurrent(String name, String currentRfid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ANIMALS,
                new String[]{COLUMN_NAME},
                COLUMN_NAME + "=? AND " + COLUMN_RFID + "!=?",
                new String[]{name, currentRfid},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // 检查RFID是否被其他动物使用（排除当前编辑的动物）
    public boolean checkRfidExistsExcludingCurrent(String rfid, String currentRfid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ANIMALS,
                new String[]{COLUMN_RFID},
                COLUMN_RFID + "=? AND " + COLUMN_RFID + "!=?",
                new String[]{rfid, currentRfid},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // 更新动物信息
    public int updateAnimal(String oldRfid, Animal animal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, animal.getName());
        values.put(COLUMN_RFID, animal.getRfid());
        values.put(COLUMN_BREED, animal.getBreed());
        values.put(COLUMN_AGE, animal.getAge());
        values.put(COLUMN_GENDER, animal.getGender());
        // 保持原有状态和入库日期不变

        return db.update(TABLE_ANIMALS, values, COLUMN_RFID + "=?", new String[]{oldRfid});
    }
}