package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.adapter.ProductAdapter;
import com.example.myapplication.entity.DataService;
import com.example.myapplication.entity.ProductInfo;
import com.example.myapplication.ProductDetailsActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private TextView tvSearchTitle;
    private RecyclerView rvSearchResults;
    private ProductAdapter productAdapter;
    private Spinner Spinner;
    private List<ProductInfo> productList = new ArrayList<>();
    private List<ProductInfo> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // 初始化UI组件
        rvSearchResults = findViewById(R.id.rv_search_results);
        tvSearchTitle=findViewById(R.id.tv_search_title);
        Spinner = findViewById(R.id.Spinner);

        // 获取传递的搜索关键词
        Intent intent = getIntent();
        String keyword = intent.getStringExtra("keyword");

        // 更新标题
        if (tvSearchTitle != null && keyword != null) {
            tvSearchTitle.setText("搜索结果: " + keyword);
        } else {
            Log.e("SearchResultsActivity", "TextView or keyword is null");
        }

        // 初始化商品列表
        productList.addAll(DataService.getListData(0)); // 可以根据需要加载不同类别的商品
        productList.addAll(DataService.getListData(1));
        productList.addAll(DataService.getListData(2));
        productList.addAll(DataService.getListData(3));
        productList.addAll(DataService.getListData(4));

        // 过滤商品列表
        filteredList = filterProducts(keyword);

        // 设置RecyclerView
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(filteredList, this::onProductClick);
        rvSearchResults.setAdapter(productAdapter);


        //返回
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // 设置Spinner适配器
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.sort_options_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner.setAdapter(adapter);

        // 处理Spinner选择事件
        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // 默认选项，不排序
                        break;
                    case 1: // 价格升序
                        sortProductsByPriceAsc();
                        break;
                    case 2: // 价格降序
                        sortProductsByPriceDesc();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_results_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_sort_asc) {
            sortProductsByPriceAsc();
            return true;
        } else if (itemId == R.id.action_sort_desc) {
            sortProductsByPriceDesc();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void sortProductsByPriceAsc() {
        Collections.sort(filteredList, new Comparator<ProductInfo>() {
            @Override
            public int compare(ProductInfo p1, ProductInfo p2) {
                return Float.compare(p1.getProduct_price(), p2.getProduct_price());
            }
        });
        productAdapter.notifyDataSetChanged();
    }

    private void sortProductsByPriceDesc() {
        Collections.sort(filteredList, new Comparator<ProductInfo>() {
            @Override
            public int compare(ProductInfo p1, ProductInfo p2) {
                return Float.compare(p2.getProduct_price(), p1.getProduct_price());
            }
        });
        productAdapter.notifyDataSetChanged();
    }

    private List<ProductInfo> filterProducts(String keyword) {
        List<ProductInfo> filteredList = new ArrayList<>();
        for (ProductInfo product : productList) {
            if (product.getProduct_title().contains(keyword) || product.getProduct_details().contains(keyword)) {
                filteredList.add(product);
            }
        }
        return filteredList;
    }

    private void onProductClick(ProductInfo productInfo) {
        Intent intent = new Intent(this,ProductDetailsActivity.class);
        intent.putExtra("productInfo", productInfo);
        startActivity(intent);
    }
}