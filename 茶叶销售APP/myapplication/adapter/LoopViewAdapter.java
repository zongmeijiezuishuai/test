package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.myapplication.R;
import com.example.myapplication.entity.ProductInfo;

import java.util.ArrayList;
import java.util.List;

public class LoopViewAdapter extends PagerAdapter {


    private Context context;
    private List<ProductInfo> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProductInfo productInfo);
    }


    public LoopViewAdapter(Context context, List<ProductInfo> productList, OnItemClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_image, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        imageView.setImageResource(productList.get(position).getProduct_img());

        // 设置点击事件
        imageView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(productList.get(position));
            }
        });

        container.addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
