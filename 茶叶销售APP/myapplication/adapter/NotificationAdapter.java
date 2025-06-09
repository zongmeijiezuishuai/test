package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.NotificationDetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.db.NoticeDbHelear;
import com.example.myapplication.entity.SystemNotification;

import java.util.List;

import java.util.List;public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<SystemNotification> notificationList;
    private NoticeDbHelear dbHelper;
    private Context context;

    public NotificationAdapter(List<SystemNotification> notificationList, NoticeDbHelear dbHelper, Context context) {
        this.notificationList = notificationList;
        this.dbHelper = dbHelper;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        SystemNotification notification = notificationList.get(position);
        holder.tvPublisher.setText(notification.getPublisher());
        holder.tvTimestamp.setText(notification.getTimestamp());
        holder.tvContent.setText(notification.getContent());

        // 设置删除按钮的点击事件
        holder.btnDelete.setOnClickListener(v -> {
            // 从数据库中删除通知
            boolean isDeleted = dbHelper.deleteSystemNotification(notification.getId());
            if (isDeleted) {
                // 从列表中移除通知
                notificationList.remove(position);
                // 通知适配器数据集已更改
                notifyItemRemoved(position);
                Toast.makeText(context, "通知删除成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "通知删除失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView tvPublisher;
        TextView tvTimestamp;
        TextView tvContent;
        Button btnDelete;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPublisher = itemView.findViewById(R.id.tv_publisher);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
            tvContent = itemView.findViewById(R.id.tv_content);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
