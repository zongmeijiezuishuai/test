package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.CommentAdapter;
import com.example.myapplication.adapter.OrderListAdapter;
import com.example.myapplication.adapter.OrderWithComments;
import com.example.myapplication.db.OrderDbHelear;
import com.example.myapplication.entity.CommentInfo;
import com.example.myapplication.entity.OrderInfo;
import com.example.myapplication.entity.UserInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CommentAdapter mCommentAdapter;
    private OrderDbHelear dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // 初始化控件
        recyclerView = findViewById(R.id.recyclerView);

        // 设置 RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCommentAdapter = new CommentAdapter(this, dbHelper);
        recyclerView.setAdapter(mCommentAdapter);

        //初始化数据库
        dbHelper = OrderDbHelear.getInstance(this);

        //返回
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        CommentViewModel viewModel = new ViewModelProvider(this).get(CommentViewModel.class);

        viewModel.getOrdersWithComments().observe(this, orderList -> {
            if (orderList != null && mCommentAdapter != null) {
                mCommentAdapter.setListData(orderList);
            }
        });
    }
}