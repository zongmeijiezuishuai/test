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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.OnAddToCartClickListener;
import com.example.myapplication.R;
import com.example.myapplication.db.CarDbHelear;
import com.example.myapplication.entity.SaleProduct;
import com.example.myapplication.entity.UserInfo;

import java.util.List;

public class SaleProductAdapter  extends RecyclerView.Adapter<SaleProductAdapter.ProductViewHolder>{
    private List<SaleProduct> productList;
    private Context context;


    public SaleProductAdapter(Context context, List<SaleProduct> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        SaleProduct saleProduct = productList.get(position);
        holder.textViewProductName.setText(saleProduct.getName());
        holder.textViewProductOriginalPrice.setText("原价: " + saleProduct.getOriginalPrice());
        holder.textViewProductDiscountedPrice.setText("折后价: " + saleProduct.getDiscountedPrice());
        holder.imageViewProduct.setImageResource(saleProduct.getImageResourceId());

        holder.buttonAddToCart.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("确认是否加入购物车")
                    .setPositiveButton("确认", (dialogInterface, i) -> {
                        UserInfo userInfo = UserInfo.getsUserInfo();
                        if (userInfo != null) {
                            int row = CarDbHelear.getInstance(context).addCar(
                                    userInfo.getUsername(),
                                    saleProduct.getId(),
                                    saleProduct.getImageResourceId(),
                                    saleProduct.getName(),
                                    saleProduct.getDiscountedPrice()
                            );
                            if (row > 0) {
                                Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("取消", (dialogInterface, i) -> {})
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewProductName;
        public TextView textViewProductOriginalPrice;
        public TextView textViewProductDiscountedPrice;
        public ImageView imageViewProduct;
        public Button buttonAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.tv_product_name);
            textViewProductOriginalPrice = itemView.findViewById(R.id.tv_ProductOriginalPrice);
            textViewProductDiscountedPrice = itemView.findViewById(R.id.tv_ProductDiscountedPrice);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            buttonAddToCart = itemView.findViewById(R.id.btn_AddToCart);
        }
    }
}
