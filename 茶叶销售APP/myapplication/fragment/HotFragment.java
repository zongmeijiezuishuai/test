package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.myapplication.ProductDetailsActivity;
import com.example.myapplication.R;
import com.example.myapplication.SaleActivity;
import com.example.myapplication.SearchResultsActivity;
import com.example.myapplication.UserCommentActivity;
import com.example.myapplication.adapter.LoopViewAdapter;
import com.example.myapplication.entity.ProductInfo;

import java.util.ArrayList;
import java.util.List;

public class HotFragment extends Fragment implements LoopViewAdapter.OnItemClickListener {

    private EditText etSearch;
    private Button btnSearch;
    private View rootView;
    private ViewPager viewPager;
    private LoopViewAdapter loopViewAdapter;
    private List<ProductInfo> productList;
    private ImageView imageView;
    private Handler handler;
    private Runnable runnable;
    private int currentIndex = 0;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_hot, container, false);

        // 获取控件
        viewPager = rootView.findViewById(R.id.viewPager);
        imageView = rootView.findViewById(R.id.imageView);
        etSearch = rootView.findViewById(R.id.et_search);
        btnSearch = rootView.findViewById(R.id.btn_search);
        horizontalScrollView = rootView.findViewById(R.id.horizontalScrollView);
        linearLayout = rootView.findViewById(R.id.linearLayout);
        relativeLayout=rootView.findViewById(R.id.buy_comments);

        // 初始化
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // 获取当前显示的 ImageView 的位置
                int currentScrollX = horizontalScrollView.getScrollX();
                int nextScrollX = currentScrollX + 100; // 每次滚动 100dp

                // 计算下一个 ImageView 的位置
                if (nextScrollX > linearLayout.getWidth() - horizontalScrollView.getWidth()) {
                    nextScrollX = 0; // 滚动到开头
                }

                // 滚动到下一个位置
                horizontalScrollView.smoothScrollTo(nextScrollX, 0);

                // 重新调度 Runnable
                handler.postDelayed(this, 1000); // 每 3 秒滚动一次
            }
        };

        // 开始自动轮播
        startAutoScroll2();

        // 为 imageView 设置点击事件，点击后跳转到 SaleActivity打折
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            if (child instanceof ImageView) {
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SaleActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }

        //用户评论,后面要修改结合当前的用户
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建 Intent
                Intent intent = new Intent(getActivity(), UserCommentActivity.class);
                // 启动新的 Activity
                startActivity(intent);
            }
        });

        // 设置搜索按钮点击事件
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = etSearch.getText().toString().trim();
                if (!keyword.isEmpty()) {
                    Intent searchIntent = new Intent(getActivity(), SearchResultsActivity.class);
                    searchIntent.putExtra("keyword", keyword);
                    startActivity(searchIntent);
                }
            }
        });

        // 准备图片数据列表
        productList = new ArrayList<>();
        productList.add(new ProductInfo(1002, R.drawable.bojuehongcha, "英国伯爵红茶奶茶店专用原材料英式格雷佛手柑特调高香特价500g", "这款英国伯爵红茶奶茶店专用原材料采用精选英式格雷佛（Earl Grey）红茶，特调高香的佛手柑精华，为您提供一款充满经典英式风味的茶叶原料。独特的柑橘香气与红茶的浓郁底味完美融合，是奶茶店、咖啡馆以及茶饮店等饮品店铺的理想选择。\n" +
                "产地：福建泉州，净含量：500g\n", 29));
        productList.add(new ProductInfo(1008, R.drawable.biluochun, "三万昌碧螺春2024新茶雨前一级绿茶苏州洞庭原产送礼盒送长辈茶叶", "三万昌碧螺春雨前一级绿茶，来自江苏苏州洞庭山的优质茶园。碧螺春被誉为中国十大名茶之一，以其独特的香气、清新的口感和细腻的茶叶著称。此款碧螺春采摘自雨前时节，选取茶树的新芽，茶叶鲜嫩，滋味更加清香。精美的礼盒包装非常适合送长辈、送亲友、商务赠礼，表达心意与尊重，呈现出高雅的品味。\n" +
                "产地：江苏苏州，净含量：250g\n", 269));
        productList.add(new ProductInfo(1022, R.drawable.baihao, "霖慧堂福鼎白茶白毫银针茶叶高山特级正宗明前白毫银针茶叶50g", "这款来自宫明茶叶的2023年云南普洱茶头春冰岛甜香古树普洱生茶，是普洱茶爱好者不容错过的优质茶品。选用云南名茶产区——冰岛地区的古树茶叶制作而成，茶叶采摘自2023年春季的头春茶，茶质上乘，香气浓郁、口感甘甜、回甘持久，茶汤清澈透亮。它代表了云南普洱茶的传统与创新，适合珍藏和品饮，是送礼和自用的上佳之选。这款霖慧堂福鼎白茶白毫银针茶叶，采自福建福鼎的高山茶园，精选明前白毫银针，每一根茶芽都包裹着显著的白毫，色泽银亮，茶香清新，口感鲜爽甘甜。作为白茶中的珍品，白毫银针茶叶内含丰富的营养成分，且极具保健功效。这款茶叶是对传统白茶工艺的传承与升华，适合茶友们日常饮用、品鉴及收藏。\n" +
                "产地：福建宁德， 净含量：150g\n", 158));

        // 创建适配器并设置到 ViewPager
        loopViewAdapter = new LoopViewAdapter(requireContext(), productList, this);
        viewPager.setAdapter(loopViewAdapter);

        startAutoScroll();

        return rootView;
    }

    @Override
    public void onItemClick(ProductInfo productInfo) {
        Intent intent = new Intent(requireContext(), ProductDetailsActivity.class);
        intent.putExtra("productInfo", productInfo);
        startActivity(intent);
    }

    // 自动滚动功能
    private void startAutoScroll() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                count++;
                if (count == loopViewAdapter.getCount()) {
                    count = 0;
                }
                viewPager.setCurrentItem(count, true);
                handler.postDelayed(this, 3000); // 每3秒滑动一次
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    private void startAutoScroll2() {
        handler.post(runnable);
    }

    private void stopAutoScroll() {
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAutoScroll(); // 停止自动轮播
    }
}