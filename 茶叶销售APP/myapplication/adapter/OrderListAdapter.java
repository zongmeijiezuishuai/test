package com.example.myapplication.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.InformationActivity;
import com.example.myapplication.R;
import com.example.myapplication.entity.OrderInfo;

import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter  extends RecyclerView.Adapter<OrderListAdapter.MyHolder> {

    private List<OrderInfo> mOrderInfos = new ArrayList<>();

    public void setListData(List<OrderInfo> list) {
        if (list != null) {
            this.mOrderInfos = list;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, null);
        return new MyHolder(view);
    }

    //此处也有错误，但是也能运行，这是第三处，还有一处也是这样，不管他！！！！！！！！！！！
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        // 绑定数据
        OrderInfo orderInfo = mOrderInfos.get(position);
        // 设置数据
        holder.product_count.setText("x" + orderInfo.getProduct_count());
        holder.product_title.setText(orderInfo.getProduct_title());
        holder.product_price.setText(orderInfo.getProduct_price()+"");
        holder.product_img.setImageResource(orderInfo.getProduct_img());
        //设置收货地址
        holder.address.setText("【"+orderInfo.getAddress()+"】；练习方式："+orderInfo.getMobile());


        //长按删除事件
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(null!=mOnItemClickListener){
                    mOnItemClickListener.onItemClick(orderInfo,position);
                }
                return true;
            }
        });

        // 点击info按钮事件
        holder.xinxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), InformationActivity.class);
                intent.putExtra("order_info", orderInfo);
                holder.itemView.getContext().startActivity(intent);
            }
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
        Button xinxi;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            product_img = itemView.findViewById(R.id.product_img);
            product_title = itemView.findViewById(R.id.product_title);
            product_price = itemView.findViewById(R.id.product_price);
            product_count = itemView.findViewById(R.id.product_count);
            address=itemView.findViewById(R.id.address);
            xinxi=itemView.findViewById(R.id.xinxi);
        }
    }

    private onItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(onItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface onItemClickListener{
        void onItemClick(OrderInfo orderInfo,int position);
    }
}
