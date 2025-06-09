package com.example.myapplication.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.AboutActivity;
import com.example.myapplication.AdminFeedbackActivity;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.PrivacyActivity;
import com.example.myapplication.R;
import com.example.myapplication.SystemActivity;
import com.example.myapplication.UpdatePwdActivity;
import com.example.myapplication.db.UserDbHelper;
import com.example.myapplication.entity.UserInfo;


public class AmineFragment extends Fragment {
    private View rootView;
    private TextView tv_username;
    private  TextView tv_nickname;
    private UserDbHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_amine, container, false);

        //初始化控件
        tv_username=rootView.findViewById(R.id.tv_username);
        tv_nickname=rootView.findViewById(R.id.tv_nickname);

        // 初始化数据库帮助类
        dbHelper = UserDbHelper.getInstance(getContext());

      //  tv_nickname.setText("留下你的个性签名吧~");

        // 设置昵称点击事件
//        tv_nickname.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 弹出对话框让用户修改昵称
//                final EditText input = new EditText(getContext());
//                input.setText(tv_nickname.getText().toString());  // 设置现有昵称为默认值
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("修改昵称")
//                        .setView(input)
//                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                String newNickname = input.getText().toString().trim();
//
//                                // 检查新的昵称是否有效
//                                if (!newNickname.isEmpty()) {
//                                    // 调用数据库更新方法，保存新的昵称
//                                    int result = dbHelper.updateNickname(getCurrentUsername(), newNickname);
//                                    if (result > 0) {
//                                        // 更新成功，显示新的昵称
//                                        tv_nickname.setText(newNickname);
//                                        Toast.makeText(getContext(), "昵称更新成功", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(getContext(), "昵称更新失败", Toast.LENGTH_SHORT).show();
//                                    }
//                                } else {
//                                    Toast.makeText(getContext(), "请输入有效的昵称", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        })
//                        .setNegativeButton("取消", null)
//                        .show();
//            }
//        });

        // 获取当前用户信息
        loadUserData();

        // 点击昵称，弹出修改对话框
        tv_nickname.setOnClickListener(v -> showNicknameDialog());


        //退出登录
        rootView.findViewById(R.id.tv_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("温馨提示")
                        .setMessage("确定要退出吗?")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //退出登录的逻辑
                                getActivity().finish();
                                //回到登录的界面
                                Intent intent =new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        })

                        .show();
            }
        });

        //修改密码
        rootView.findViewById(R.id.updatePwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdatePwdActivity.class);
                startActivityForResult(intent,1000);
            }
        });

        //关于APP
        rootView.findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        //联系客服
        rootView.findViewById(R.id.kefu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), AdminFeedbackActivity.class);
                startActivity(intent);
            }
        });

        //系统通知
        rootView.findViewById(R.id.notice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), SystemActivity.class);
                startActivity(intent);
            }
        });

        //隐私政策
        rootView.findViewById(R.id.privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), PrivacyActivity.class);
                startActivity(intent);
            }
        });



      return rootView;
    }

    // 加载当前用户数据
    private void loadUserData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 获取当前用户名，并查询当前用户的昵称
        String currentUsername = getCurrentUsername();  // 假设你已经有方法返回当前登录用户名
        Cursor cursor = db.query("user_table", new String[]{"username", "nickname"},
                "username = ?", new String[]{currentUsername}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String nickname = cursor.getString(cursor.getColumnIndex("nickname"));

            // 确保昵称不为空，若为空则显示默认提示
            if (nickname == null || nickname.isEmpty()) {
                nickname = "留下你的个性签名吧~";
            }

            // 显示数据
            tv_username.setText(username);
            tv_nickname.setText(nickname);  // 显示当前用户的昵称
            cursor.close();
        } else {
            // 如果查询失败，设置默认显示
            tv_username.setText("未知用户");
            tv_nickname.setText("留下你的个性签名吧~");
        }
    }




    // 获取当前用户名，假设可以从登录状态或其它地方获得
    private String getCurrentUsername() {
        return "xiaomei"; // 示例：根据需求返回实际的当前用户名
    }

    // 弹出修改昵称的对话框
    private void showNicknameDialog() {
        // 创建一个输入框
        final EditText input = new EditText(getContext());
        input.setText(tv_nickname.getText());  // 设置当前昵称为默认值

        // 创建并显示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("修改昵称")
                .setMessage("请输入新的昵称")
                .setView(input)  // 设置输入框
                .setPositiveButton("确认", (dialog, which) -> {
                    String newNickname = input.getText().toString().trim();

                    if (newNickname.isEmpty()) {
                        Toast.makeText(getContext(), "昵称不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        updateNicknameInDatabase(newNickname);
                    }
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.cancel())  // 取消按钮
                .show();
    }

    // 更新昵称到数据库
    private void updateNicknameInDatabase(String newNickname) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nickname", newNickname);

        // 获取当前用户名，并更新当前用户的昵称
        String currentUsername = getCurrentUsername();  // 假设你已经有方法返回当前登录用户名

        // 只更新当前用户的昵称，不影响其他用户
        int rowsAffected = db.update("user_table", values, "username = ?", new String[]{currentUsername});

        if (rowsAffected > 0) {
            // 更新成功后，刷新界面
            tv_nickname.setText(newNickname);
            Toast.makeText(getContext(), "昵称更新成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "更新失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //设置用户数据
        UserInfo userInfo =UserInfo.getsUserInfo();
        if(null!=userInfo){

            tv_username.setText(userInfo.getUsername());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1000){
            getActivity().finish();
            Intent intent=new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);
        }
    }
}