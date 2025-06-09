package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.InformationActivity;
import com.example.myapplication.R;
import com.example.myapplication.db.OrderDbHelear;
import com.example.myapplication.entity.OrderInfo;

import java.util.ArrayList;
import java.util.List;

public class OrdersaleAdapter extends RecyclerView.Adapter<OrdersaleAdapter.MyHolder> {
    private List<OrderInfo> mOrderInfos = new ArrayList<>();
    private Context context;
    private OrderDbHelear dbHelper;

    // 定义监听器接口
    public interface OnItemClickListener {
        void onItemClick(OrderInfo orderInfo, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(OrderInfo orderInfo, int position);
    }
    public void setListData(List<OrderInfo> list) {
        if (list != null) {
            this.mOrderInfos.clear();
            this.mOrderInfos.addAll(list);
            notifyDataSetChanged(); // 通知RecyclerView数据发生了变化
        }
    }

    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mLongClickListener = listener;
    }

    public OrdersaleAdapter(List<OrderInfo> orderList, Context context) {
        this.mOrderInfos = orderList;
        this.context = context;
        dbHelper = new OrderDbHelear(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item2, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final OrderInfo orderInfo = mOrderInfos.get(position);

        holder.product_count.setText("x" + orderInfo.getProduct_count());
        holder.product_title.setText(orderInfo.getProduct_title());
        holder.product_price.setText(String.valueOf(orderInfo.getProduct_price()));
        holder.product_img.setImageResource(orderInfo.getProduct_img());
        holder.address.setText("【" + orderInfo.getAddress() + "】；联系方式：" + orderInfo.getMobile());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mLongClickListener != null) {
                    mLongClickListener.onItemLongClick(orderInfo, holder.getAdapterPosition());
                }
                return true;  // 返回true表示事件已消费
            }
        });

        holder.xinxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrderStatusAndNotify(orderInfo.getOrder_id(), "已发货", holder);
            }
        });
    }

    private void updateOrderStatusAndNotify(int orderId, String status, final RecyclerView.ViewHolder holder) {
        boolean success = dbHelper.updateOrderStatus(orderId, status);
        if (success) {
            Toast.makeText(context, "订单状态已更改为" + status, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "更新订单状态失败，请重试。", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mOrderInfos.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        ImageView product_img;
        TextView product_title;
        TextView product_price;
        TextView product_count;
        TextView address;
        Button xinxi;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            product_img = itemView.findViewById(R.id.product_img);
            product_title = itemView.findViewById(R.id.product_title);
            product_price = itemView.findViewById(R.id.product_price);
            product_count = itemView.findViewById(R.id.product_count);
            address = itemView.findViewById(R.id.address);
            xinxi = itemView.findViewById(R.id.xinxi);
        }
    }
}