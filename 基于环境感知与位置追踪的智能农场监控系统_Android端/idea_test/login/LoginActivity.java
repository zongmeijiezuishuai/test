package com.example.idea_test.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.*;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.idea_test.MainActivity;
import com.example.idea_test.R;
import com.example.idea_test.dao.UserDao;
import com.example.idea_test.fragment.MapFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "mysql-party-Login";
    private TextInputEditText textInputEditTextAccount, textInputEditTextPassword;
    private MaterialButton loginButton, registerButton;
    private TextView forgotPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_login);

        // 初始化视图
        textInputEditTextAccount = findViewById(R.id.uesrAccount);
        textInputEditTextPassword = findViewById(R.id.userPassword);
        loginButton = findViewById(R.id.login_bt);
        registerButton = findViewById(R.id.register_bt);
        forgotPasswordText = findViewById(R.id.forgotPassword);

        // 设置点击监听器
        loginButton.setOnClickListener(v -> login());
        registerButton.setOnClickListener(v -> reg());
        forgotPasswordText.setOnClickListener(v -> onForgotPasswordClick());

        // 从注册页面传递过来的账号
        String registeredAccount = getIntent().getStringExtra("account");
        if (!TextUtils.isEmpty(registeredAccount)) {
            textInputEditTextAccount.setText(registeredAccount);
            textInputEditTextPassword.requestFocus(); // 自动聚焦到密码输入框
        }
    }

    private void reg() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void login() {
        String account = textInputEditTextAccount.getText().toString().trim();
        String password = textInputEditTextPassword.getText().toString().trim();

        if (account.isEmpty()) {
            textInputEditTextAccount.setError("请输入账号");
            return;
        }

        if (password.isEmpty()) {
            textInputEditTextPassword.setError("请输入密码");
            return;
        }

        // 显示加载状态
        loginButton.setEnabled(false);
        loginButton.setText("登录中...");

        new Thread(() -> {
            UserDao userDao = new UserDao();
            int result = userDao.login(account, password);

            // 发送结果到主线程
            runOnUiThread(() -> {
                loginButton.setEnabled(true);
                loginButton.setText("登录");
                handleLoginResult(result);
            });
        }).start();
    }

    private void handleLoginResult(int result) {
        View rootView = findViewById(android.R.id.content);
        String message;
        String detail = "";

        switch (result) {
            case 0:
                message = "登录失败";
                detail = "\n原因：网络连接异常或服务器错误";
                break;
            case 1:
                message = "登录成功";
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            case 2:
                message = "密码错误";
                detail = "\n请检查密码后重试";
                textInputEditTextPassword.setError("密码错误");
                textInputEditTextPassword.requestFocus();
                break;
            case 3:
                message = "账号不存在";
                detail = "\n请注册新账号或检查账号输入";
                textInputEditTextAccount.setError("账号不存在");
                textInputEditTextAccount.requestFocus();
                break;
            default:
                message = "未知错误";
                detail = "\n错误代码：" + result;
        }

        // 构建带详细信息的Snackbar
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(message);
        if (!detail.isEmpty()) {
            sb.append(detail);
            // 设置详细信息的样式（灰色小字）
            sb.setSpan(new ForegroundColorSpan(Color.GRAY),
                    message.length(), sb.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(new RelativeSizeSpan(0.8f),
                    message.length(), sb.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        Snackbar.make(rootView, sb, Snackbar.LENGTH_LONG)
                .setAction("确定", v -> {
                    // 点击确定后的操作
                    if (result == 2 || result == 3) {
                        textInputEditTextPassword.requestFocus();
                    }
                })
                .show();
    }

//    private void onForgotPasswordClick() {
//        // 这里可以添加忘记密码的逻辑
//        Toast.makeText(this, "忘记密码功能暂未实现", Toast.LENGTH_SHORT).show();
//    }

    public void onForgotPasswordClick() {
        showForgotPasswordDialog();
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        // 初始化视图
        TextInputEditText etAccount = dialogView.findViewById(R.id.etAccount);
        TextInputEditText etVerificationCode = dialogView.findViewById(R.id.etVerificationCode);
        TextInputEditText etNewPassword = dialogView.findViewById(R.id.etNewPassword);
        TextInputEditText etConfirmPassword = dialogView.findViewById(R.id.etConfirmPassword);
        TextInputLayout tilVerificationCode = dialogView.findViewById(R.id.tilVerificationCode);
        TextInputLayout tilNewPassword = dialogView.findViewById(R.id.tilNewPassword);
        TextInputLayout tilConfirmPassword = dialogView.findViewById(R.id.tilConfirmPassword);
        MaterialButton btnGetCode = dialogView.findViewById(R.id.btnGetCode);
        MaterialButton btnSubmit = dialogView.findViewById(R.id.btnSubmit);

        // 当前步骤：1-输入账号，2-输入验证码，3-输入新密码
        final int[] currentStep = {1};

        // 获取验证码按钮点击事件
        btnGetCode.setOnClickListener(v -> {
            String account = etAccount.getText().toString().trim();
            if (account.isEmpty()) {
                etAccount.setError("请输入账号");
                return;
            }

            new Thread(() -> {
                boolean success = new UserDao().sendVerificationCode(account);
                runOnUiThread(() -> {
                    if (success) {
                        startCountDown(btnGetCode);
                        Toast.makeText(LoginActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        // 提交按钮点击事件
        btnSubmit.setOnClickListener(v -> {
            String account = etAccount.getText().toString().trim();

            if (currentStep[0] == 1) {
                // 第一步：验证账号
                if (account.isEmpty()) {
                    etAccount.setError("请输入账号");
                    return;
                }

                new Thread(() -> {
                    boolean accountExists = new UserDao().findUser(account) != null;
                    runOnUiThread(() -> {
                        if (accountExists) {
                            tilVerificationCode.setVisibility(View.VISIBLE);
                            btnSubmit.setText("验证");
                            currentStep[0] = 2;
                        } else {
                            etAccount.setError("账号不存在");
                        }
                    });
                }).start();

            } else if (currentStep[0] == 2) {
                // 第二步：验证验证码
                String code = etVerificationCode.getText().toString().trim();
                if (code.isEmpty()) {
                    etVerificationCode.setError("请输入验证码");
                    return;
                }

                new Thread(() -> {
                    boolean codeValid = new UserDao().verifyCode(account, code);
                    runOnUiThread(() -> {
                        if (codeValid) {
                            tilNewPassword.setVisibility(View.VISIBLE);
                            tilConfirmPassword.setVisibility(View.VISIBLE);
                            btnSubmit.setText("重置密码");
                            currentStep[0] = 3;
                        } else {
                            etVerificationCode.setError("验证码错误");
                        }
                    });
                }).start();

            } else if (currentStep[0] == 3) {
                // 第三步：重置密码
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (newPassword.isEmpty()) {
                    etNewPassword.setError("请输入新密码");
                    return;
                }

                if (confirmPassword.isEmpty()) {
                    etConfirmPassword.setError("请确认新密码");
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    etConfirmPassword.setError("两次输入的密码不一致");
                    return;
                }

                new Thread(() -> {
                    int result = new UserDao().resetPassword(account, newPassword);
                    runOnUiThread(() -> {
                        if (result == 1) {
                            Toast.makeText(LoginActivity.this, "密码重置成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(LoginActivity.this, "密码重置失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            }
        });
    }

    // 验证码倒计时
    private void startCountDown(final MaterialButton button) {
        final int[] seconds = {60};
        button.setEnabled(false);

        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                button.setText(String.format("%ds后重新获取", seconds[0]--));
            }

            @Override
            public void onFinish() {
                button.setEnabled(true);
                button.setText("获取验证码");
            }
        }.start();
    }
}



