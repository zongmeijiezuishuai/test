package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.db.UserDbHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private EditText et_password_again;
    private RadioButton rb_user,rb_admin;

    //用了数据库就可以不用这个了
    //private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //获取SharedPreferences,用于存储少量数据,存储用户名和密码
        //mSharedPreferences=getSharedPreferences("user",MODE_PRIVATE);

        //初始化控件
        et_username=findViewById(R.id.et_username);
        et_password=findViewById(R.id.et_password);
        et_password_again=findViewById(R.id.et_password_again);
        rb_user = findViewById(R.id.rb_user);    // 用户角色RadioButton
        rb_admin = findViewById(R.id.rb_admin);  //管理员

        //返回登录界面
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // 点击注册
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String passwordAgain = et_password_again.getText().toString().trim();

                // 检查输入是否合法
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordAgain)) {
                    Toast.makeText(RegisterActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                } else if (username.length() < 3) {
                    Toast.makeText(RegisterActivity.this, "用户名至少需要3个字符", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "密码至少需要6个字符", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(passwordAgain)) {
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    // 获取用户选择的角色
                    int role = rb_user.isChecked() ? 0 : (rb_admin.isChecked() ? 1 : -1);  // 0为用户，1为管理员，-1为未选择

                    if (role == -1) {
                        Toast.makeText(RegisterActivity.this, "请选择角色", Toast.LENGTH_SHORT).show();
                    } else {
                        // 调用数据库操作，存储注册信息
                        long row = UserDbHelper.getInstance(RegisterActivity.this).register(username, password, role); // 角色参数传递int类型

                        if (row > 0) {
                            // 注册成功
                            Toast.makeText(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                            finish();  // 结束当前注册活动
                        } else {
                            // 注册失败，给出失败提示
                            Toast.makeText(RegisterActivity.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


    }
}