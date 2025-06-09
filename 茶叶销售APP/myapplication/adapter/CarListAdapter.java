package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entity.CarInfo;

import java.util.ArrayList;
import java.util.List;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.MyHolder> {

    private List<CarInfo> mCarInfoList = new ArrayList<>();

    //注意这里可能会有错误，是setCarInfoList还是setmCarInfoList呢？
    public void setCarInfoList(List<CarInfo> list){
        this.mCarInfoList=list;
        //这句话不能少
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_list_item, null);

        return new  MyHolder(view);
    }

    //此处也有错误，但是也能运行，这是第二处，还有一处也是这样，不管他！！！！！！！！！！！
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //绑定数据
        CarInfo carInfo=mCarInfoList.get(position);
        holder.product_img.setImageResource(carInfo.getProduct_img());
        holder.product_title.setText(carInfo.getProduct_title());
        holder.product_price.setText(carInfo.getProduct_price()+"");
        holder.product_count.setText(carInfo.getProduct_count()+"");

        //设置点击事件
        //加
        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=mOnItemClickListener){
                    mOnItemClickListener.onPlusOnClick(carInfo,position);
                }
            }
        });

        //减
        holder.btn_subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=mOnItemClickListener){
                    mOnItemClickListener.onSubtractOnClick(carInfo,position);
                }
            }
        });

        //长按删除购物车商品
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.delOnClick(carInfo,position);
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCarInfoList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{

        ImageView product_img;
        TextView product_title;
        TextView product_price;
        TextView product_count;
        TextView btn_plus;
        TextView btn_subtract;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            product_img=itemView.findViewById(R.id.product_img);
            product_title=itemView.findViewById(R.id.product_title);
            product_price=itemView.findViewById(R.id.product_price);
            product_count=itemView.findViewById(R.id.product_count);
            btn_subtract=itemView.findViewById(R.id.btn_subtract);
            btn_plus=itemView.findViewById(R.id.btn_plus);
        }
    }

    private onItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(onItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //写个回调
    public interface onItemClickListener{
        void onPlusOnClick(CarInfo carInfo,int position);
        void onSubtractOnClick(CarInfo carInfo,int position);
        void delOnClick(CarInfo carInfo,int position);
    }
}
