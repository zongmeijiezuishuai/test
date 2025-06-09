package com.example.idea_test.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.idea_test.R;
import com.example.idea_test.dao.UserDao;
import com.example.idea_test.entity.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "mysql-party-register";
    private TextInputEditText etAccount, etUserName, etPassword;
    private MaterialButton registerButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_register);
        // 初始化视图
        etAccount = findViewById(R.id.userAccount);
        etUserName = findViewById(R.id.userName);
        etPassword = findViewById(R.id.userPassword);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        // 设置点击监听器
        registerButton.setOnClickListener(v -> register());
        if (loginButton != null) {
            loginButton.setOnClickListener(v -> {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            });
        }
    }

    private void register() {
        String account = etAccount.getText().toString().trim();
        String userName = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 验证输入
        if (account.isEmpty()) {
            etAccount.setError("请输入手机号");
            return;
        }

        if (!account.matches("^1[3-9]\\d{9}$")) {
            etAccount.setError("请输入正确的手机号");
            return;
        }

        if (userName.isEmpty()) {
            etUserName.setError("请输入昵称");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("请输入密码");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("密码至少6位");
            return;
        }

        // 显示加载状态
        registerButton.setEnabled(false);
        registerButton.setText("注册中...");

        // 执行注册逻辑
        new Thread(() -> {
            User user = new User(0,account, password, userName, 0, 1, 0);
            int result = new UserDao().register(user);

            runOnUiThread(() -> {
                registerButton.setEnabled(true);
                registerButton.setText("注册");

                handleRegisterResult(result);
            });
        }).start();
    }

    private void handleRegisterResult(int result) {
        View rootView = findViewById(android.R.id.content);
        String message;
        String detail = "";

        switch (result) {
            case 0:
                message = "注册失败";
                detail = "\n原因：服务器错误";
                break;
            case 1:
                message = "注册成功";
                // 返回登录界面并传递账号
                Intent intent = new Intent();
                intent.putExtra("account", etAccount.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                return;
            case 2:
                message = "注册失败";
                detail = "\n原因：账号已存在";
                etAccount.setError("账号已存在");
                etAccount.requestFocus();
                break;
            default:
                message = "未知错误";
                detail = "\n错误代码：" + result;
        }

        // 显示Snackbar提示
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(message);
        if (!detail.isEmpty()) {
            sb.append(detail);
            sb.setSpan(new ForegroundColorSpan(Color.GRAY),
                    message.length(), sb.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(new RelativeSizeSpan(0.8f),
                    message.length(), sb.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        Snackbar.make(rootView, sb, Snackbar.LENGTH_LONG)
                .setAction("确定", v -> {
                    if (result == 2) {
                        etAccount.requestFocus();
                    }
                })
                .show();
    }
}