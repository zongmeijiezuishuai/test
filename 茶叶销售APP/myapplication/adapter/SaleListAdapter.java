package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entity.OrderInfo;

import java.util.ArrayList;
import java.util.List;

public class SaleListAdapter extends RecyclerView.Adapter<SaleListAdapter.MyHolder> {

    private List<OrderInfo> mOrderInfos = new ArrayList<>();


    // 设置数据
    public void setListData(List<OrderInfo> list) {
        if (list != null) {
            this.mOrderInfos = list;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 修正inflate方式
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_list_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        OrderInfo orderInfo = mOrderInfos.get(position);

        // 设置数据
        holder.product_count.setText("x" + orderInfo.getProduct_count());
        holder.product_title.setText(orderInfo.getProduct_title());
        holder.product_price.setText(String.valueOf(orderInfo.getProduct_price()));  // 使用String.valueOf
        holder.product_img.setImageResource(orderInfo.getProduct_img());
        holder.address.setText("【" + orderInfo.getAddress() + "】；练习方式：" + orderInfo.getMobile());


        // 长按删除事件
        holder.itemView.setOnLongClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(orderInfo, position);
            }
            return true;  // 返回true表示事件已处理
        });
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
        TextView client;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            product_img = itemView.findViewById(R.id.product_img);
            product_title = itemView.findViewById(R.id.product_title);
            product_price = itemView.findViewById(R.id.product_price);
            product_count = itemView.findViewById(R.id.product_count);
            address = itemView.findViewById(R.id.address);
            client = itemView.findViewById(R.id.client);
        }
    }

    private OnItemClickListener mOnItemClickListener;

    // 更新为规范的set方法
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    // 监听器接口
    public interface OnItemClickListener {
        void onItemClick(OrderInfo orderInfo,int position);
    }
}
