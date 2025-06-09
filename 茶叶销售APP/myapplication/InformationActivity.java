package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.db.OrderDbHelear;
import com.example.myapplication.entity.OrderInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class InformationActivity extends AppCompatActivity {

    private TextView orderIdTextView, userPhoneTextView, deliveryAddressTextView,
            orderDateTextView, deliveryDateTextView, orderTotalPriceTextView,
            orderStatusTextView, productTitleTextView, productPriceTextView,
            productCountTextView,orderNumbertextView;
    private ImageView productImageView;
    private Button confirmReceiptButton;
    private OrderDbHelear dbHelper;
    private long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_information2);

        bindViews();

        // 返回按钮
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fillOrderDetails();

        dbHelper = new OrderDbHelear(this, "orders.db", null, 3);

        Intent intent = getIntent();
        OrderInfo orderInfo = (OrderInfo) intent.getSerializableExtra("order_info");
        if (orderInfo != null) {
            orderId = orderInfo.getOrder_id(); // 直接从 OrderInfo 获取订单ID
        } else {
            Log.e("InvalidOrderInfo", "无效的订单信息");
            Toast.makeText(InformationActivity.this, "无效的订单信息，请重试。", Toast.LENGTH_SHORT).show();
            finish(); // 结束当前Activity以避免用户继续操作
            return;
        }

        // 设置按钮点击监听器
        confirmReceiptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleConfirmReceipt();
            }
        });
    }

    private void bindViews() {
        orderIdTextView = findViewById(R.id.order_id_textView);
        userPhoneTextView = findViewById(R.id.user_phone_textView);
        deliveryAddressTextView = findViewById(R.id.delivery_address_textView);
        orderDateTextView = findViewById(R.id.order_date_textView);
        deliveryDateTextView = findViewById(R.id.delivery_date_textView);
        orderTotalPriceTextView = findViewById(R.id.order_total_price_textView);
        orderStatusTextView = findViewById(R.id.order_status_textView);
        productTitleTextView = findViewById(R.id.product_title);
        productPriceTextView = findViewById(R.id.product_price);
        productCountTextView = findViewById(R.id.product_count);
        productImageView = findViewById(R.id.product_img);
        confirmReceiptButton = findViewById(R.id.confirm_receipt_button); // 确认收货按钮
        orderNumbertextView = findViewById(R.id.orderNumbertextView);
    }

    private void fillOrderDetails() {
        Intent intent = getIntent();
        OrderInfo orderInfo = (OrderInfo) intent.getSerializableExtra("order_info");

        if (orderInfo != null) {
            orderIdTextView.setText("订单号: " + orderInfo.getOrder_id());
            userPhoneTextView.setText("电话: " + orderInfo.getMobile());
            deliveryAddressTextView.setText("收货地址: " + orderInfo.getAddress());
            orderDateTextView.setText("下单日期: " + formatDateTime(orderInfo.getOrderTimeFormatted()));
            deliveryDateTextView.setText("收货日期: " + formatDateTime(orderInfo.getDeliveryTimeFormatted()));
            double totalPrice = orderInfo.getProduct_price() * orderInfo.getProduct_count();
            orderTotalPriceTextView.setText(String.format("总价: ¥%.2f", totalPrice));
            orderStatusTextView.setText("状态: " + orderInfo.getOrderStatus());

            productTitleTextView.setText(orderInfo.getProduct_title());
            productPriceTextView.setText(String.format("¥%.2f", (double) orderInfo.getProduct_price()));
            productCountTextView.setText("x" + orderInfo.getProduct_count());

            // 假设 product_img 是一个资源ID
            productImageView.setImageResource(orderInfo.getProduct_img());
            // 显示快递单号
            orderNumbertextView.setText("快递单号: " + orderInfo.getOrderNumber());
        }
    }

    private void handleConfirmReceipt() {
        // 获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());

        // 记录下单时间和确认收货前的状态
        Log.d("OrderDetails", "下单日期: " + orderDateTextView.getText().toString());
        Log.d("OrderDetails", "当前时间: " + currentTime);

        // 更新UI元素 - 确保只更新交付日期和订单状态
        deliveryDateTextView.setText("收货日期: " + currentTime);
        orderStatusTextView.setText("状态: 已收货"); // 更新状态为“已收货”

        // 提示用户操作成功
        Toast.makeText(InformationActivity.this, "确认收货成功！", Toast.LENGTH_SHORT).show();

        // 再次记录下单时间，确保其未被修改
        Log.d("OrderDetails", "下单日期 (after update): " + orderDateTextView.getText().toString());
    }

    private String formatDateTime(String dateTime) {
        // 这里可以加入格式化时间的逻辑
        return dateTime; // 如果不需要格式化，直接返回原始值
    }
}
//    private void handleConfirmReceipt() {
//        Log.d("ConfirmReceipt", "开始处理确认收货，订单ID: " + orderId);
//
//        // 获取当前时间
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        String currentTime = sdf.format(new Date());
//
//        // 定义新状态
//        String newStatus = "已收货";
//
//        // 更新数据库中的交付时间和订单状态
//        try {
//            // 确保 orderId 类型与 updateDeliveryAndStatus 方法参数匹配
//            int orderIdInt = (int) orderId; // 如果 orderId 是 long 类型
//
//            boolean success = dbHelper.updateDeliveryAndStatus(orderIdInt, currentTime, newStatus);
//            if (success) {
//                Log.d("ConfirmReceipt", "数据库更新成功");
//                // 只更新相关的UI元素
//                deliveryDateTextView.setText("收货日期: " + currentTime);
//                orderStatusTextView.setText("状态: " + newStatus); // 更新状态为“已收货”
//
//                // 提示用户操作成功
//                Toast.makeText(InformationActivity.this, "确认收货成功！", Toast.LENGTH_SHORT).show();
//            } else {
//                Log.e("ConfirmReceipt", "数据库更新失败，订单ID: " + orderId);
//                // 如果更新失败，提示用户
//                Toast.makeText(InformationActivity.this, "确认收货失败，请重试。", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            Log.e("ConfirmReceipt", "处理确认收货时发生异常，订单ID: " + orderId, e);
//            Toast.makeText(InformationActivity.this, "确认收货时发生异常，请重试。", Toast.LENGTH_SHORT).show();
//        }
//    }
