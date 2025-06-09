package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.db.UserDbHelper;
import com.example.myapplication.entity.UserInfo;

public class LoginActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private CheckBox cb_is_login;
    private RadioButton rb_user,rb_admin;
    private boolean is_login;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //控件初始化,这个位置一定要在前面！！！！！！！！！！！！！！！！不准改！！！！！！！！！！！！！！！！
        et_username=findViewById(R.id.et_username);
        et_password=findViewById(R.id.et_password);
        cb_is_login = findViewById(R.id.cb_is_login);
        rb_user = findViewById(R.id.rb_user);    // 用户角色RadioButton
        rb_admin = findViewById(R.id.rb_admin);  //管理员


        //获取SharedPreferences。用于记住密码
        mSharedPreferences=getSharedPreferences("user",MODE_PRIVATE);

        //是否勾选记住了密码
        is_login = mSharedPreferences.getBoolean("is_login", false);
        if(is_login){
            String username=mSharedPreferences.getString("username",null);
            String password=mSharedPreferences.getString("password",null);
            et_username.setText(username);
            et_password.setText(password);
            cb_is_login.setChecked(true);
        }


        //点击注册
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转到注册界面
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //点击忘记密码
        findViewById(R.id.forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, UpdatePwdActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.bt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                } else {
                    // 调用 UserDbHelper 的 login 方法，获取用户信息
                    UserInfo login = UserDbHelper.getInstance(LoginActivity.this).login(username);

                    if (login != null) {
                        // 如果用户名和密码正确，进行角色判断
                        if (username.equals(login.getUsername()) && password.equals(login.getPassword())) {
                            // 保存登录状态和用户信息
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putBoolean("is_login", true);  // 修改为true，表示已登录
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.commit();

                            // 保存用户信息到全局
                            UserInfo.setsUserInfo(login);

                            // 判断角色，根据角色跳转到不同的界面
                            if (login.getRole() == 0) {
                                // 如果是普通用户（role = 0）
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (login.getRole() == 1) {
                                // 如果是管理员（role = 1）
                                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // 如果角色值不合法
                                Toast.makeText(LoginActivity.this, "角色无效，请联系管理员", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // 如果用户名或密码错误
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // 如果没有找到对应的用户，提示用户注册
                        Toast.makeText(LoginActivity.this, "该账号尚未注册", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        // 记住密码的状态监听
        cb_is_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                is_login = isChecked;  // 设置是否记住密码
            }
        });

    }
}