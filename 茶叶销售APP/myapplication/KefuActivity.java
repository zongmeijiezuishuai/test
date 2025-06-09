package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.db.MessageDbHelear;


public class KefuActivity extends AppCompatActivity {

    private EditText etName, etEmail, etMessage;
    private Button btnSend,viewMoreFaqButton;
    private TextView tvReply;
    private MessageDbHelear messageDbHelear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kefu);

        // 初始化控件
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
        tvReply = findViewById(R.id.tv_reply);
        viewMoreFaqButton=findViewById(R.id.viewMoreFaqButton);

        messageDbHelear = new MessageDbHelear(this);

        // 设置发送按钮点击事件
        btnSend.setOnClickListener(v -> sendFeedback());

        //返回
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 查看更多常见问题按钮点击事件
        viewMoreFaqButton.setOnClickListener(v -> showFaqDialog());
    }

    private void sendFeedback() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String message = etMessage.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
            return;
        }

        // 将反馈数据存储到数据库
        long feedbackId = messageDbHelear.addFeedback(name, email, message);

        if (feedbackId != -1) {
            Toast.makeText(this, "感谢您的反馈，我们会尽快处理！", Toast.LENGTH_SHORT).show();

            // 清空输入框
            etName.setText("");
            etEmail.setText("");
            etMessage.setText("");

            // 显示等待客服回复的提示
            tvReply.setVisibility(View.VISIBLE);
            tvReply.setText("等待客服回复...");
        } else {
            Toast.makeText(this, "提交失败，请稍后再试", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFaqDialog() {
        // 创建一个包含常见问题的字符串数组
        String[] faqItems = new String[]{
                "Q: 如何选择喜欢的茶？\nA: 您可以在首页浏览茶叶商品，点击进入商品详情页后选择规格和数量，然后点击购买按钮，按照提示完成支付流程即可。",
                "Q:购买后多久发货？\nA:一般来说，在下单后48小时内会安排发货。",
                "Q: 如何查询订单状态？\nA: 您可以在个人中心的订单列表中查看订单状态，已付款、已发货、已完成等状态都会清晰显示。",
                "Q: 茶的保质期是多久？\nA: 一般来说，在适宜的储存条件下，茶可以长期保存且越陈越香。但如果储存不当，可能会影响茶叶品质。建议将茶叶存放在干燥、通风、无异味的环境中。",
                "Q:茶叶的价格差异很大，如何选择适合自己的茶叶？\nA:茶叶价格因品种、产地、制作工艺以及品牌等多种因素而异。选择适合自己需求的茶叶可以从以下几点考虑：\n" +
                        "\n" +
                        "明确用途: 如果是为了日常饮用，可以选择性价比较高的中档茶叶；如果是用来送礼或者是特殊场合使用，则可以根据预算挑选更高级别的茶叶。\n" +
                        "了解个人喜好: 不同类型的茶叶具有独特的风味特征，比如有些人喜欢清淡爽口的绿茶，而另一些人可能偏爱浓烈香醇的红茶或普洱茶。根据个人口味偏好来决定购买哪种类型的茶叶。\n" +
                        "关注产地和等级: 某些特定地区生产的茶叶以其优良品质闻名，如西湖龙井、黄山毛峰等。同时，不同级别的茶叶也会对口感产生影响，一般而言，级别越高，茶叶的质量越好，但价格也相应较高。\n" +
                        "注意保质期和保存条件: 新鲜度对于茶叶来说非常重要，尤其是绿茶这类不易长期存放的茶品。购买时要注意查看生产日期，并确保茶叶是在良好的条件下储存的。\n" +
                        "尝试小包装试饮: 在不确定某款茶叶是否适合自己之前，可以先从小包装开始尝试，避免一次性购买大量不合适的茶叶造成浪费。"
        };

        // 构建 AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(KefuActivity.this);
        builder.setTitle("查看更多常见问题")
                .setItems(faqItems, null) // 只显示内容，不设置点击事件
                .setPositiveButton("关闭", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}
