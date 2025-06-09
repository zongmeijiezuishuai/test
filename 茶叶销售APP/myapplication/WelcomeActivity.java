package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
public class WelcomeActivity extends AppCompatActivity {

    private TextView tv_Countdown;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 5000; // 设置倒计时时长，单位为毫秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // 初始化控件
        tv_Countdown = findViewById(R.id.tv_countdown);

        // 设置点击事件监听器，允许用户点击跳过
        tv_Countdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipCountdown();
            }
        });

        // 启动倒计时
        startCountdown();
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                tv_Countdown.setText(secondsRemaining + " s");
            }

            @Override
            public void onFinish() {
                // 倒计时结束后的操作，例如跳转到主页面
                navigateToNextActivity();
            }
        }.start();
    }

    private void skipCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        navigateToNextActivity();
    }

    private void navigateToNextActivity() {
        finish(); // 结束当前活动
        // 跳转到登录页面（根据逻辑可以调整）
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}