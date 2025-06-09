package com.example.myapplication.fragment;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.UserAdapter;
import com.example.myapplication.db.CarDbHelear;
import com.example.myapplication.db.UserDbHelper;
import com.example.myapplication.entity.CarInfo;
import com.example.myapplication.entity.UserInfo;

import java.util.ArrayList;
import java.util.List;


public class ClientFragment extends Fragment {

    private View rootView;
    private UserDbHelper userDbHelper;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private Button addButton, deleteButton, queryButton;
    private String selectedUsername;  // 用于存储选中的用户名


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_client, container, false);

        //初始化
        userDbHelper = new UserDbHelper(getContext());
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //初始化控件
        addButton = rootView.findViewById(R.id.addButton);
        deleteButton = rootView.findViewById(R.id.deleteButton);
        queryButton = rootView.findViewById(R.id.queryButton);

        // 设置按钮点击事件
        addButton.setOnClickListener(v -> showAddUserDialog());
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());

        queryButton.setOnClickListener(v -> queryUsers());

        // 设置按钮点击事件
        addButton.setOnClickListener(v -> showAddUserDialog());
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());
        queryButton.setOnClickListener(v -> queryUsers());

        // 查询所有用户并显示
        queryUsers();

        return rootView;
    }

    private void showAddUserDialog() {
        // 创建一个对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add User");

        // 创建视图
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_user, null);
        final EditText etUsername = dialogView.findViewById(R.id.etUsername);
        final EditText etPassword = dialogView.findViewById(R.id.etPassword);
        final EditText etNickname = dialogView.findViewById(R.id.etNickname);

        // 设置对话框的视图
        builder.setView(dialogView);

        // 设置确定按钮
        builder.setPositiveButton("Add", (dialog, which) -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String nickname = etNickname.getText().toString();
            int role = 0; // 默认为普通用户

            // 获取选中的角色
            if (((android.widget.RadioButton) dialogView.findViewById(R.id.rb_Admin)).isChecked()) {
                role = 1; // 管理员
            } else if (((android.widget.RadioButton) dialogView.findViewById(R.id.rb_User)).isChecked()) {
                role = 0; // 普通用户
            }

            // 调用注册方法
            long insertId = userDbHelper.register(username, password, role);
            if (insertId != -1) {
                // 注册成功，更新列表
                queryUsers();
            }
        });

        // 设置取消按钮
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // 显示对话框
        builder.show();
    }






    // 查询所有用户并显示
    private void queryUsers() {
        // 获取所有用户数据
        List<UserInfo> userList = userDbHelper.getAllUsers();

        // 如果 adapter 为空，初始化它
        if (userAdapter == null) {
            userAdapter = new UserAdapter(userList, username -> {
                selectedUsername = username;  // 设置选中的用户名
            }, getContext(), userDbHelper);  // 传递上下文和 UserDbHelper
            recyclerView.setAdapter(userAdapter);
        } else {
            // 更新现有的 adapter 数据
            userAdapter.updateUserList(userList);
        }
    }


    // 显示删除确认对话框
    private void showDeleteConfirmationDialog() {
        if (selectedUsername == null || selectedUsername.isEmpty()) {
            // 如果没有选中用户，显示提示信息
            new AlertDialog.Builder(requireContext())
                    .setTitle("Error")
                    .setMessage("Please select a user to delete.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("温馨提醒")
                .setMessage("确认是否删除该用户")
                .setPositiveButton("确认", (dialog, which) -> {
                    userDbHelper.deleteuser(selectedUsername);  // 删除选中的用户
                    queryUsers();  // 更新列表
                    selectedUsername = null;  // 清空选中的用户名
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    // 取消操作，不执行任何操作
                })
                .show();
    }

}