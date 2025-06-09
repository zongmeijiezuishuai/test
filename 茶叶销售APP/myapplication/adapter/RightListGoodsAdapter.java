package com.example.myapplication.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.entity.CarInfo;
import com.example.myapplication.entity.GoodsInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RightListGoodsAdapter extends RecyclerView.Adapter<RightListGoodsAdapter.MyHolder> {

    private List<GoodsInfo> goodsList = new ArrayList<>();
    private onItemClickListener onItemClickListener;

    private OnItemLongClickListener mOnItemLongClickListener;


    // 设置数据
    public void setGoodsList(List<GoodsInfo> goodsList) {
        this.goodsList = goodsList;
        notifyDataSetChanged();  // 刷新RecyclerView
    }

    // 设置点击事件监听器
    public void setOnItemClickListener(onItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    //常按事件
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_list_item_goods, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        GoodsInfo goodsInfo = goodsList.get(position);

        // 绑定数据到ViewHolder
        holder.product_title.setText(goodsInfo.getProductTitle());
        holder.product_details.setText(goodsInfo.getProductDetails());
        holder.product_price.setText(String.valueOf(goodsInfo.getProductPrice()));
        holder.product_count.setText(String.valueOf(goodsInfo.getProductCount()));
        holder.product_id.setText(String.valueOf(goodsInfo.getProductId()));

        // 使用Glide加载图片
        loadProductImage(holder.product_img, goodsInfo.getProductImg());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(goodsInfo, position);
            }
        });

//        String imagePath = goodsInfo.getProductImg();
//
//        Log.d("GoodsFragment", "Image Path: " + imagePath);
//
//        if (!TextUtils.isEmpty(imagePath)) {
//            Glide.with(holder.itemView.getContext())
//                    .load(imagePath)
//                    .into(holder.productImage);
//        } else {
//            holder.productImage.setImageResource(R.drawable.fenghuang); // 默认图片
//        }

        // 设置长按事件监听器
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (mOnItemLongClickListener != null) {
//                    // 调用接口中的删除方法，并传入当前的商品信息和位置
//                    mOnItemLongClickListener.onItemLongClick(goodsInfo, position);
//                    return true; // 表示事件已经被消费
//                }
//                return false;
//            }
//        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    Log.d("RightListGoodsAdapter", "Long click on item: " + goodsInfo.getProductId());
                    mOnItemLongClickListener.onItemLongClick(goodsInfo, position);
                    return true; // 表示事件已经被消费
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    public interface onItemClickListener {
        void onItemClick(GoodsInfo goodsInfo, int position);
    }

    // 定义长按点击接口，包含删除逻辑
    public interface OnItemLongClickListener {
        void onItemLongClick(GoodsInfo goodsInfo, int position);
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        ImageView product_img;
        TextView product_title;
        TextView product_details;
        TextView product_price;
        TextView product_count;
        TextView product_id;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            product_img = itemView.findViewById(R.id.product_img);
            product_title = itemView.findViewById(R.id.product_title);
            product_details = itemView.findViewById(R.id.product_details);
            product_price = itemView.findViewById(R.id.product_price);
            product_count = itemView.findViewById(R.id.product_count);
            product_id = itemView.findViewById(R.id.product_id);
        }
    }

    // 加载图片的方法
    private void loadProductImage(ImageView imageView, String imageUrl) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load("file://" + imageUrl) // 注意这里的前缀 "file://"
                .into(imageView);

    }


}