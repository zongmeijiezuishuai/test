package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.FeedbackAdapter;
import com.example.myapplication.db.MessageDbHelear;

import java.util.ArrayList;
import java.util.List;

public class AdminFeedbackActivity extends AppCompatActivity implements FeedbackAdapter.OnItemClickListener{

    private RecyclerView rvFeedbacks;
    private List<FeedbackItem> feedbackList;
    private FeedbackAdapter feedbackAdapter;
    private MessageDbHelear messageDbHelear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_feedback);

        // 初始化控件
        rvFeedbacks = findViewById(R.id.rv_feedbacks);

        // 初始化数据库帮助类
        messageDbHelear = new MessageDbHelear(this);

        // 初始化反馈列表
        feedbackList = new ArrayList<>();
        //feedbackAdapter = new FeedbackAdapter(feedbackList, this);  // 传递当前 Activity 作为监听器
        feedbackAdapter = new FeedbackAdapter(feedbackList, this);

        // 设置 RecyclerView
        rvFeedbacks.setLayoutManager(new LinearLayoutManager(this));
        rvFeedbacks.setAdapter(feedbackAdapter);

        //返回
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 从数据库获取反馈数据
        loadFeedbacks();
    }

    private void loadFeedbacks() {
        Cursor cursor = messageDbHelear.getAllFeedbacks();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MessageDbHelear.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(MessageDbHelear.COLUMN_NAME));
                String email = cursor.getString(cursor.getColumnIndex(MessageDbHelear.COLUMN_EMAIL));
                String message = cursor.getString(cursor.getColumnIndex(MessageDbHelear.COLUMN_MESSAGE));

                FeedbackItem feedbackItem = new FeedbackItem(id, name, email, message);
                feedbackList.add(feedbackItem);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // 刷新适配器
        feedbackAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onItemClick(FeedbackItem item) {
//        item.setRead(true);
//        messageDbHelear.markAsRead(item.getId());
//        feedbackAdapter.notifyItemChanged(feedbackList.indexOf(item));
//        Toast.makeText(this, "标记为已读", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onItemClick(FeedbackItem item) {

    }

    @Override
    public void onDeleteClick(FeedbackItem item) {
        messageDbHelear.deleteFeedback(item.getId());
        feedbackList.remove(item);
        feedbackAdapter.notifyItemRemoved(feedbackList.indexOf(item));
        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
    }
}