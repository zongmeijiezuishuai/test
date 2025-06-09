package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.db.GoodsDbHelear;
import com.example.myapplication.entity.GoodsInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class UpdataProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAPTURE_IMAGE_REQUEST = 2;
    private ImageView product_img;
    private TextView product_title;
    private TextView product_price;
    private TextView product_details;
    private TextView product_count;
    private GoodsInfo goodsInfo;
    private GoodsDbHelear goodsDbHelear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_product);

        //获取传递的数据
        goodsInfo = (GoodsInfo) getIntent().getSerializableExtra("goodsInfo");

        // 如果传递的 productInfo 为空，退出活动
        if (goodsInfo == null) {
            Toast.makeText(this, "未找到产品信息", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        //返回
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初始化控件
        product_img = findViewById(R.id.product_img);
        product_title = findViewById(R.id.product_title);
        product_details = findViewById(R.id.product_details);
        product_price = findViewById(R.id.product_price);
        product_count = findViewById(R.id.product_count);

        // 初始化数据库帮助类
        goodsDbHelear = GoodsDbHelear.getInstance(this);

        //设置数据
        if (null != goodsInfo) {
            loadProductImage(product_img, goodsInfo.getProductImg());
            //  product_img.setImageResource(goodsInfo.getProductImg());
            product_title.setText(goodsInfo.getProductTitle());
            product_details.setText(goodsInfo.getProductDetails());
            product_price.setText(goodsInfo.getProductPrice() + "");
            product_count.setText(goodsInfo.getProductCount() + "");
        }

        //修改数据
        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateDialog();
            }
        });

        // 选择图片
        product_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerOptions();
            }
        });
    }

    private void showImagePickerOptions() {
        CharSequence[] items = {"从图库选择", "拍照"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择图片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            openGallery();
                        } else {
                            openCamera();
                        }
                    }
                })
                .show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    saveAndLoadImage(selectedImageUri);
                }
            } else if (requestCode == CAPTURE_IMAGE_REQUEST && data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (photo != null) {
                    saveAndLoadImage(photo);
                }
            }
        }
    }

    private void saveAndLoadImage(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            saveImage(bitmap);
            loadProductImage(product_img, goodsInfo.getProductImg());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAndLoadImage(Bitmap bitmap) {
        try {
            File file = new File(getExternalFilesDir(null), "product_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            goodsInfo.setProductImg(file.getAbsolutePath());
            loadProductImage(product_img, goodsInfo.getProductImg());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void saveImage(Bitmap bitmap) throws IOException {
        File file = new File(getExternalFilesDir(null), "product_" + System.currentTimeMillis() + ".jpg");
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();

        goodsInfo.setProductImg(file.getAbsolutePath());
    }


    private void loadProductImage(ImageView imageView, String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            Glide.with(this)
                    .load(imagePath)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.fenghuang); // 默认图片
        }
    }


    private void showUpdateDialog() {
        // 获取当前数据
        final String currentTitle = goodsInfo.getProductTitle();
        final String currentDetails = goodsInfo.getProductDetails();
        final int currentPrice = goodsInfo.getProductPrice();
        final int currentCount = goodsInfo.getProductCount();

        // 创建一个对话框布局
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_product, null);

        // 获取布局中的控件
        final EditText edtTitle = dialogView.findViewById(R.id.edt_title);
        final EditText edtDetails = dialogView.findViewById(R.id.edt_details);
        final EditText edtPrice = dialogView.findViewById(R.id.edt_price);
        final EditText edtCount = dialogView.findViewById(R.id.edt_count);

        // 设置默认值
        edtTitle.setText(currentTitle);
        edtDetails.setText(currentDetails);
        edtPrice.setText(String.valueOf(currentPrice));
        edtCount.setText(String.valueOf(currentCount));

        // 创建AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改产品信息")
                .setView(dialogView)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取用户输入的数据
                        String newTitle = edtTitle.getText().toString();
                        String newDetails = edtDetails.getText().toString();
                        String priceStr = edtPrice.getText().toString();
                        String countStr = edtCount.getText().toString();

                        // 校验输入是否合法
                        if (newTitle.isEmpty() || newDetails.isEmpty() || priceStr.isEmpty() || countStr.isEmpty()) {
                            Toast.makeText(UpdataProductActivity.this, "请填写完整的信息", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // 校验价格是否合法
                        int newPrice;
                        try {
                            newPrice = Integer.parseInt(priceStr);
                            if (newPrice <= 0) {
                                Toast.makeText(UpdataProductActivity.this, "价格必须为正数", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(UpdataProductActivity.this, "请输入有效的价格", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // 校验数量是否合法
                        int newCount;
                        try {
                            newCount = Integer.parseInt(countStr);
                            if (newCount < 0) {
                                Toast.makeText(UpdataProductActivity.this, "数量不能为负数", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(UpdataProductActivity.this, "请输入有效的数量", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // 更新 GoodsInfo 对象
                        goodsInfo.setProductTitle(newTitle);
                        goodsInfo.setProductDetails(newDetails);
                        goodsInfo.setProductPrice(newPrice);  // 使用 int 类型的价格
                        goodsInfo.setProductCount(newCount);  // 更新商品数量

                        // 更新数据库
                        int result = goodsDbHelear.updateProduct(goodsInfo.getProductId(), goodsInfo.getProductImg(), newTitle, newDetails, newPrice, newCount, goodsInfo.getType());

                        // 根据返回的结果显示不同的Toast消息
                      // 在更新成功后设置返回结果
//                        if (result > 0) {
//                            Toast.makeText(UpdataProductActivity.this, "商品信息更新成功", Toast.LENGTH_SHORT).show();
//                            Intent resultIntent = new Intent();
//                            resultIntent.putExtra("updatedGoodsInfo", goodsInfo);
//                            setResult(RESULT_OK, resultIntent);
//                           // finish();  // 关闭当前页面，返回上一页
//                        } else {
//                            Toast.makeText(UpdataProductActivity.this, "更新失败，请稍后重试", Toast.LENGTH_SHORT).show();
//                        }
                        updateUI();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void updateUI() {
        // 更新界面显示
        product_title.setText(goodsInfo.getProductTitle());
        product_details.setText(goodsInfo.getProductDetails());
        product_price.setText(String.valueOf(goodsInfo.getProductPrice()));
        product_count.setText(String.valueOf(goodsInfo.getProductCount()));
    }

}