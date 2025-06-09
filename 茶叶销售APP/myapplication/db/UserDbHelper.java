package com.example.myapplication.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myapplication.entity.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class UserDbHelper extends SQLiteOpenHelper {
    private static UserDbHelper sHelper;
    private static final String DB_NAME="user.db";
    private static final int VERSION=2;


    public UserDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public UserDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    //创建单例，供使用调用该类里面的增删查改的方法
    public synchronized static UserDbHelper getInstance(Context context){
        if(null==sHelper){
            sHelper=new UserDbHelper(context,DB_NAME,null,VERSION);
        }
        return sHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建user_table表
        db.execSQL("create table user_table(user_id integer primary key autoincrement," +
                "username text,"+ //用户名
                "password text,"+ //密码
                "nickname text,"+  //昵称
                "role integer"+   //0是用户rb_user，1是管理员rb_admin
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < 2) {  // 假设从版本1升级到版本2，增加了role列
            // 如果数据库版本低于2，则执行ALTER TABLE语句
            sqLiteDatabase.execSQL("ALTER TABLE user_table ADD COLUMN role INTEGER DEFAULT 0");
        }
    }


    /**
     * 登录  根据用户名查找用户
     */
    @SuppressLint("Range")
    public UserInfo login(String username) {
        // 获取SQLiteDatabase实例
        SQLiteDatabase db = getReadableDatabase();
        UserInfo userInfo = null;

        // 执行查询SQL，获取用户信息
        String sql = "select user_id, username, password, nickname, role from user_table where username=?";
        String[] selectionArgs = {username};
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        if (cursor.moveToNext()) {
            // 获取各个字段的数据
            int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
            String name = cursor.getString(cursor.getColumnIndex("username"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
            int role = cursor.getInt(cursor.getColumnIndex("role"));  // role 是整数（0 或 1）

            // 创建 UserInfo 对象并返回
            userInfo = new UserInfo(user_id, name, password, nickname, role);
        }

        cursor.close();
        db.close();
        return userInfo;
    }

    /**
     * 注册方法
     */
    public long register(String username, String password, int role) {
        // 获取SQLiteDatabase实例
        SQLiteDatabase db = getWritableDatabase();

        // 创建ContentValues对象，填充要插入的数据
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        //values.put("nickname", nickname);  // 昵称应该是字符串类型
        values.put("role", role);          // 角色应该是整数（0 或 1）

        // 执行插入操作，返回插入的行号
        long insertId = db.insert("user_table", null, values);  // 不需要nullColumnHack参数
        db.close();
        return insertId;  // 返回插入的行 ID
    }

    /**
     * 修改密码
     */

    public int updatePwd(String username, String password) {
        //获取SQLiteDatabase实例
        SQLiteDatabase db = getWritableDatabase();
        // 填充占位符
        ContentValues values = new ContentValues();
        values.put("password", password);
        // 执行SQL
        int update = db.update("user_table", values, " username=?", new String[]{username});
        // 关闭数据库连接
        db.close();
        return update;

    }

    /**
     * 增加就是用注册的,查找用登录的
     */

    /**
     * 删除
     */

    public int deleteuser(String username) {
        //获取SQLiteDatabase实例
        SQLiteDatabase db = getWritableDatabase();
        // 执行SQL
        int delete = db.delete("user_table", " username=?", new String[]{username});
        // 关闭数据库连接
        db.close();
        return delete;
    }

    /**
     * 修改信息
     */
    public int update(String username, String password,String nickname,int role) {
        //获取SQLiteDatabase实例
        SQLiteDatabase db = getWritableDatabase();
        // 填充占位符
        ContentValues values = new ContentValues();
        values.put("password", password);
        values.put("nickname", nickname);
        values.put("role", role);
        // 执行SQL
        int update = db.update("user_table", values, " username=?", new String[]{username});
        // 关闭数据库连接
        db.close();
        return update;

    }
    /**
     * 查询所有用户
     */
    @SuppressLint("Range")
    public List<UserInfo> getAllUsers() {
        // 获取SQLiteDatabase实例
        SQLiteDatabase db = getReadableDatabase();
        List<UserInfo> userList = new ArrayList<>();

        // 执行查询SQL，获取所有用户信息
        String sql = "SELECT user_id, username, password, nickname, role FROM user_table";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                // 获取各个字段的数据
                int userId = cursor.getInt(cursor.getColumnIndex("user_id"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
                int role = cursor.getInt(cursor.getColumnIndex("role"));

                // 创建 UserInfo 对象并添加到列表中
                UserInfo userInfo = new UserInfo(userId, username, password, nickname, role);
                userList.add(userInfo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userList;
    }

    /**
     * 修改昵称
     */
    public int updateNickname(String username, String newNickname) {
        // 获取SQLiteDatabase实例
        SQLiteDatabase db = getWritableDatabase();
        // 填充占位符
        ContentValues values = new ContentValues();
        values.put("nickname", newNickname);  // 设置新的昵称
        // 执行SQL
        int update = db.update("user_table", values, "username=?", new String[]{username});
        // 关闭数据库连接
        db.close();
        return update;
    }

}
