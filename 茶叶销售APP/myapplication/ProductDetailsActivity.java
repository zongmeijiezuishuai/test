package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.db.CarDbHelear;
import com.example.myapplication.entity.ProductInfo;
import com.example.myapplication.entity.UserInfo;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView product_img;
    private TextView product_title;
    private TextView product_price;
    private TextView product_details;
    private ProductInfo productInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        //获取传递的数据
       // productInfo= (ProductInfo) getIntent().getSerializableExtra("productInfo");
        ProductInfo productInfo = (ProductInfo) getIntent().getSerializableExtra("productInfo");

        //返回
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初始化控件
        product_img=findViewById(R.id.product_img);
        product_title=findViewById(R.id.product_title);
        product_details=findViewById(R.id.product_details);
        product_price=findViewById(R.id.product_price);

        //设置数据
        if(null!=productInfo){
            product_img.setImageResource(productInfo.getProduct_img());
            product_title.setText(productInfo.getProduct_title());
            product_details.setText(productInfo.getProduct_details());
            product_price.setText(productInfo.getProduct_price()+"");
        }


        //加入购物车
        findViewById(R.id.addCar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ProductDetailsActivity.this)
                        .setTitle("确认是否加入购物车")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //调用这个就不是写死的了
                                UserInfo userInfo=UserInfo.getsUserInfo();
                                if(userInfo!=null){
                                    //下面的那个写法是写死的
                                    //如果我没记错的话，一直登录的是dd。后面查不到数据要修改！！！！！！！！！！！！！！！！！！！！！！！！！
                                    // int row = CarDbHelear.getInstance(ProductDetailsActivity.this).addCar("dd", productInfo.getProduct_id(), productInfo.getProduct_img(), productInfo.getProduct_title(), productInfo.getProduct_price());
                                    int row = CarDbHelear.getInstance(ProductDetailsActivity.this).addCar(userInfo.getUsername(), productInfo.getProduct_id(), productInfo.getProduct_img(), productInfo.getProduct_title(), productInfo.getProduct_price());
                                    //添加成功
                                    if(row>0){
                                        Toast.makeText(ProductDetailsActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else {
                                        Toast.makeText(ProductDetailsActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                     .show();
            }
        });

    }
}