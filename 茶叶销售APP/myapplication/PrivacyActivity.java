package com.example.myapplication;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PrivacyActivity extends AppCompatActivity {

    TextView tvPrivacyPolicy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        // 初始化控件
         tvPrivacyPolicy = findViewById(R.id.tv_privacy_policy);

        // 设置隐私政策内容
        String privacyPolicy = "## 隐私政策\n\n" +
                "### 引言\n" +
                "本隐私政策旨在解释我们如何收集、使用、存储和保护您的个人信息。本政策适用于我们的网站、移动应用程序和其他在线服务。\n\n" +
                "### 信息收集\n" +
                "#### 个人信息\n" +
                "我们可能会收集以下类型的个人信息：\n" +
                "- 姓名\n" +
                "- 电子邮件地址\n" +
                "- 电话号码\n" +
                "- 地址\n" +
                "- 账户信息\n\n" +
                "#### 非个人信息\n" +
                "我们还可能收集以下类型的非个人信息：\n" +
                "- 设备信息（如设备型号、操作系统版本）\n" +
                "- IP地址\n" +
                "- 浏览器类型和版本\n" +
                "- 访问时间和频率\n\n" +
                "#### 第三方服务\n" +
                "我们可能会使用第三方服务提供商来帮助我们提供服务。这些服务提供商可能会收集某些信息，但它们只能按照我们的指示使用这些信息。\n\n" +
                "### 信息使用\n" +
                "我们收集的信息主要用于以下目的：\n" +
                "- 提供和改进我们的服务\n" +
                "- 处理您的订单和请求\n" +
                "- 发送营销和促销信息（如果您同意接收）\n" +
                "- 保护我们的服务免受欺诈和滥用\n\n" +
                "### 信息保护\n" +
                "我们采取以下措施来保护您的个人信息：\n" +
                "- 使用加密技术保护数据传输\n" +
                "- 实施物理、电子和管理程序来保护数据安全\n" +
                "- 定期审查和更新我们的安全措施\n\n" +
                "### 用户权利\n" +
                "您有权：\n" +
                "- 访问和更正您的个人信息\n" +
                "- 请求删除您的个人信息\n" +
                "- 反对或限制对您的个人信息的处理\n\n" +
                "如果您希望行使这些权利，请通过以下联系方式与我们联系。\n\n" +
                "### 数据传输\n" +
                "我们可能会将您的个人信息传输到其他国家/地区，这些国家/地区的数据保护法律可能与您所在国家/地区的不同。我们将采取适当的措施来确保您的个人信息得到充分保护。\n\n" +
                "### 隐私政策更新\n" +
                "我们可能会不时更新本隐私政策。如有重大变更，我们会通过电子邮件或在我们的网站上发布公告通知您。\n\n" +
                "### 联系方式\n" +
                "如果您对本隐私政策有任何疑问或建议，请通过以下方式联系我们：\n" +
                "- 电子邮件：2518651452@qq.com\n" +
                "- 电话：+19148164224\n" +
                "- 邮寄地址：广西梧州万秀区梧州学院\n\n" +
                "感谢您使用我们的服务！";

        // 设置隐私政策内容到TextView
        tvPrivacyPolicy.setText(privacyPolicy);
        tvPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance()); // 支持点击链接

        //返回
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}