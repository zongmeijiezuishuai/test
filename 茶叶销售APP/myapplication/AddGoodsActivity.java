package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.db.GoodsDbHelear;

import java.io.IOException;

public class AddGoodsActivity extends AppCompatActivity {

    private EditText et_ProductTitle, et_ProductDetails, et_ProductId, et_Product_Price, et_ProductCount;
    private Spinner teaTypeSpinner;
    private Button btnAddProduct;
    private GoodsDbHelear goodsDbHelear;
    private Toolbar toolbar;

    // 选择图片的请求码
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);

        //初始化控件
        et_ProductTitle = findViewById(R.id.et_productTitle);
        et_ProductDetails = findViewById(R.id.et_productDetails);
        et_ProductId = findViewById(R.id.et_productId);
        et_Product_Price = findViewById(R.id.et_productPrice);
        et_ProductCount = findViewById(R.id.et_productCount);
        teaTypeSpinner = findViewById(R.id.teaTypeSpinner);
        btnAddProduct = findViewById(R.id.bt_add);
        ImageView productImg = findViewById(R.id.product_img);



        // 设置点击事件让用户选择图片
        productImg.setOnClickListener(v -> openImageChooser());

        // 初始化数据库助手类
        goodsDbHelear = GoodsDbHelear.getInstance(this);

        // 获取Spinner控件
        Spinner teaTypeSpinner = findViewById(R.id.teaTypeSpinner);

        // 定义下拉框数据源
        String[] teaTypes = {"红茶", "绿茶", "青茶（乌龙茶）", "黄茶", "黑茶", "白茶"};

        // 创建适配器，将数据源绑定到Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teaTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 设置Spinner的适配器
        teaTypeSpinner.setAdapter(adapter);

        // 设置Spinner的选项选择监听
        teaTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 当用户选择某个项时显示选中的茶叶类型
                String selectedTea = parentView.getItemAtPosition(position).toString();
                Toast.makeText(AddGoodsActivity.this, "你选择了: " + selectedTea, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 处理没有选择的情况
            }
        });

        //返回
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //添加商品按钮点击事件
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductToDatabase();
            }
        });
    }


    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");  // 指定类型为图片
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true); // 限制选择本地文件
        startActivityForResult(Intent.createChooser(intent, "选择图片"), PICK_IMAGE_REQUEST);
    }

    // 获取用户选择的图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                // 将图片显示到 ImageView 中
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ImageView productImg = findViewById(R.id.product_img);
                productImg.setImageBitmap(bitmap);

                // 保存图片路径到数据库或文件系统
                saveImageToDatabase(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImageToDatabase(Uri imageUri) {
        // 将图片路径存储到数据库中
        String imagePath = getImagePathFromUri(imageUri);

        // 使用 SQLite 或其他数据库操作来保存路径
        ContentValues contentValues = new ContentValues();
        contentValues.put("productImg", imagePath);  // 假设 "productImg" 是数据库表中的字段

        // 假设你有一个 SQLiteOpenHelper 类来管理数据库
        SQLiteDatabase db = goodsDbHelear.getWritableDatabase();
        db.insert("goods_table", null, contentValues);
    }

    // 获取图片的路径
    private String getImagePathFromUri(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(projection[0]);
            return cursor.getString(columnIndex);
        }
        return null;
    }


    private void addProductToDatabase() {
        // 获取输入的信息
        String productTitle = et_ProductTitle.getText().toString().trim();
        String productDetails = et_ProductDetails.getText().toString().trim();
        String productIdStr = et_ProductId.getText().toString().trim();
        String productPriceStr = et_Product_Price.getText().toString().trim();
        String productCountStr = et_ProductCount.getText().toString().trim();
        String teaType = teaTypeSpinner.getSelectedItem().toString().trim(); // 获取Spinner选择的茶叶类型

        // 输入验证
        if (productTitle.isEmpty() || productDetails.isEmpty() || productIdStr.isEmpty() || productPriceStr.isEmpty() || productCountStr.isEmpty()) {
            Toast.makeText(this, "请填写所有信息", Toast.LENGTH_SHORT).show();
            return;
        }

        // 转换数据类型
        int productId = Integer.parseInt(productIdStr);
        int productPrice = Integer.parseInt(productPriceStr);
        int productCount = Integer.parseInt(productCountStr);

        // 你可能需要一个图片ID，这里假设是0
        String productImg = "0"; // 修改为字符串类型

        // 调用数据库方法插入商品
        int result = goodsDbHelear.addProduct(productImg, productTitle, productDetails, productPrice, productCount, teaType);

        // 根据返回的结果显示不同的Toast消息
        if (result == 1) {
            Toast.makeText(this, "商品添加成功", Toast.LENGTH_SHORT).show();
            finish();  // 关闭当前页面，返回上一页
        } else if (result == -1) {
            Toast.makeText(this, "商品已存在，已更新数量", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "添加失败，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }

}
