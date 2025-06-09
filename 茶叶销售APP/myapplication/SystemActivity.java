package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
public class SystemActivity extends AppCompatActivity {

    private EditText etPublisher;
    private EditText etContent;
    private Button btnPublish;
    private RecyclerView rvNotifications;
    private List<SystemNotification> notificationList;
    private NotificationAdapter notificationAdapter;
    private NoticeDbHelear dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);

        // 初始化控件
        etPublisher = findViewById(R.id.et_publisher);
        etContent = findViewById(R.id.et_content);
        btnPublish = findViewById(R.id.btn_publish);
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

        // 发布按钮点击事件
        btnPublish.setOnClickListener(v -> publishNotification());

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

    private void publishNotification() {
        String publisher = etPublisher.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (publisher.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "发布者和内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = dbHelper.addSystemNotification(publisher, content);
        if (id > 0) {
            etPublisher.setText("");
            etContent.setText("");
            loadNotifications(); // 重新加载通知列表
            Toast.makeText(this, "通知发布成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "通知发布失败", Toast.LENGTH_SHORT).show();
        }
    }
}
