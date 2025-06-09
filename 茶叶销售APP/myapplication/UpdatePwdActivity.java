package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.db.UserDbHelper;
import com.example.myapplication.entity.UserInfo;

public class UpdatePwdActivity extends AppCompatActivity {
    private EditText et_new_password;
    private EditText et_confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);

        //初始化控件
        et_new_password=findViewById(R.id.et_new_password);
        et_confirm_password=findViewById(R.id.et_confirm_password);

        //修改密码点击事件
        findViewById(R.id.btn_update_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_pwd=et_new_password.getText().toString();
                String confirm_pwd=et_confirm_password.getText().toString();

                if(TextUtils.isEmpty(new_pwd) || TextUtils.isEmpty(confirm_pwd)){
                    Toast.makeText(UpdatePwdActivity.this,"信息不能为空",Toast.LENGTH_SHORT).show();
                } else if (!new_pwd.equals(confirm_pwd)) {
                    Toast.makeText(UpdatePwdActivity.this,"新密码和确认密码不一致",Toast.LENGTH_SHORT).show();
                }else {
                    UserInfo userInfo=UserInfo.getsUserInfo();
                    if(null!=userInfo){
                        int row = UserDbHelper.getInstance(UpdatePwdActivity.this).updatePwd(userInfo.getUsername(), new_pwd);
                        if(row>0){
                            Toast.makeText(UpdatePwdActivity.this,"密码修改成功，请重新登录",Toast.LENGTH_SHORT).show();
                            //回调到登录界面，startActivityForResult启动一个界面，并且在该界面设置setResult
                            setResult(1000);
                            finish();
                        }else {
                            Toast.makeText(UpdatePwdActivity.this,"密码修改失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        //返回
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}