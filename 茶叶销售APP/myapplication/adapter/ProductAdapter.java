package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.entity.ProductInfo;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entity.ProductInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductInfo> productList;
    private Consumer<ProductInfo> onProductClickListener;

    // 构造函数
    public ProductAdapter(List<ProductInfo> productList, Consumer<ProductInfo> onProductClickListener) {
        this.productList = productList;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_list_item, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductInfo productInfo = productList.get(position); // 使用 productList 而不是 mProductInfos
        holder.product_img.setImageResource(productInfo.getProduct_img());
        holder.product_title.setText(productInfo.getProduct_title());
        holder.product_details.setText(productInfo.getProduct_details());
        holder.product_price.setText(String.valueOf(productInfo.getProduct_price()));

        // 在点击事件中调用 Consumer<ProductInfo> 类型的回调
        holder.itemView.setOnClickListener(v -> {
            if (onProductClickListener != null) {
                onProductClickListener.accept(productInfo);  // 调用 Consumer 的 accept 方法
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();  // 使用 productList 而不是 mProductInfos
    }

    // 更新列表数据
    public void updateList(List<ProductInfo> newList) {
        productList.clear();
        productList.addAll(newList);
        notifyDataSetChanged();
    }

    // ViewHolder 类
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView product_img;
        TextView product_title;
        TextView product_details;
        TextView product_price;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            product_img = itemView.findViewById(R.id.product_img);
            product_title = itemView.findViewById(R.id.product_title);
            product_details = itemView.findViewById(R.id.product_details);
            product_price = itemView.findViewById(R.id.product_price);
        }
    }
}
