package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entity.UserInfo;

import java.util.List;
import androidx.appcompat.app.AlertDialog;
import com.example.myapplication.db.UserDbHelper;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyHolder>{

    private List<UserInfo> userList;
    private OnUserClickListener onUserClickListener;
    private Context context;  // 添加上下文变量

    private UserDbHelper userDbHelper;  // 添加 UserDbHelper 变量

    public UserAdapter(List<UserInfo> userList,OnUserClickListener onUserClickListener, Context context, UserDbHelper userDbHelper) {
        this.userList = userList;
        this.context = context;
        this.onUserClickListener = onUserClickListener;
        this.userDbHelper = userDbHelper;
       // notifyDataSetChanged();
    }



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user, null);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        UserInfo userInfo=userList.get(position);
        holder.tv_username.setText(userInfo.getUsername());
        holder.tv_nickname.setText(userInfo.getNickname());
        holder.tv_password.setText(userInfo.getPassword());
        holder.tv_role.setText(userInfo.getRole() == 0 ? "User" : "Admin");
      //  holder.bind(userInfo,onUserClickListener);

        // 设置点击监听器
        holder.itemView.setOnClickListener(v -> {
            if (onUserClickListener != null) {
                onUserClickListener.onUserClick(userInfo.getUsername());  // 传递完整的 UserInfo 对象
            }
        });

        // 设置长按监听器
        holder.itemView.setOnLongClickListener(v -> {
            showDeleteConfirmationDialog(userInfo, position);
            return true;  // 返回 true 表示已经处理了长按事件
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // 更新用户列表的方法
    public void updateUserList(List<UserInfo> newList) {
        this.userList = newList;
        notifyDataSetChanged(); // 通知适配器数据已更改
    }

    // 显示删除确认对话框
    private void showDeleteConfirmationDialog(UserInfo userInfo, final int position) {
        new AlertDialog.Builder(context)
                .setTitle("温馨提醒")
                .setMessage("确认是否删除该用户")
                .setPositiveButton("确认", (dialog, which) -> {
                    userDbHelper.deleteuser(userInfo.getUsername());  // 删除选中的用户
                    userList.remove(position);  // 从列表中移除
                    notifyItemRemoved(position);  // 通知适配器移除项
                    notifyItemRangeChanged(position, userList.size());  // 通知适配器范围变化
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    // 取消操作，不执行任何操作
                })
                .show();
    }

    // 回调接口
    public interface OnUserClickListener {
        void onUserClick(String username);
    }

    static class MyHolder extends RecyclerView.ViewHolder{

        TextView tv_username,tv_nickname,tv_role,tv_password;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_username=itemView.findViewById(R.id.tv_username);
            tv_nickname=itemView.findViewById(R.id.tv_nickname);
            tv_role=itemView.findViewById(R.id.tv_role);
            tv_password=itemView.findViewById(R.id.tv_password);

        }
    }

}
