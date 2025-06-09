package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.NotificationAdapter;
import com.example.myapplication.db.NoticeDbHelear;
import com.example.myapplication.entity.SystemNotification;

import java.util.ArrayList;
import java.util.List;

public class NotificationDetailActivity extends AppCompatActivity {

    private RecyclerView rvNotifications;
    private List<SystemNotification> notificationList;
    private NotificationAdapter notificationAdapter;
    private NoticeDbHelear dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);


        rvNotifications = findViewById(R.id.rv_notifications);

        // 初始化数据库帮助类
        dbHelper = new NoticeDbHelear(this);

        // 初始化通知列表
        notificationList = new ArrayList<>();

        // 初始化适配器，并传递 notificationList, dbHelper, 和 context
        notificationAdapter = new NotificationAdapter(notificationList, dbHelper, this);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        rvNotifications.setAdapter(notificationAdapter);

        // 加载通知数据
        loadNotifications();

        // 返回
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void loadNotifications() {
        notificationList.clear();
        notificationList.addAll(dbHelper.getAllSystemNotifications());
        notificationAdapter.notifyDataSetChanged();
    }

}