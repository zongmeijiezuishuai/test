package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.fragment.AmineFragment;
import com.example.myapplication.fragment.ClientFragment;
import com.example.myapplication.fragment.GoodsFragment;
import com.example.myapplication.fragment.SaleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminActivity extends AppCompatActivity {

    //先定义
    private GoodsFragment mGoodsFragment;
    private ClientFragment mClientFragment;
    private AmineFragment mAmineFragment;
    private BottomNavigationView mBottomNavigationView;
    private SaleFragment mSaleFragment;
    private FloatingActionButton fab;
    private TextView addProductText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //初始化控件
        mBottomNavigationView=findViewById(R.id.bottomNavigationView);
       // fab = findViewById(R.id.fab);
        addProductText = findViewById(R.id.addProductText);


        //+点击事件
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //跳转到添加商品界面
//                Intent intent = new Intent(AdminActivity.this, AddGoodsActivity.class);
//                startActivity(intent);
//
//            }
//        });

        //mBottomNavigationView设置点击事件
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.goods){
                    selectedFragment(0);

                }else if (item.getItemId()==R.id.client) {
                    selectedFragment(1);
                }else if (item.getItemId()==R.id.sale) {
                    selectedFragment(2);
                } else {
                    selectedFragment(3);
                }
                return true;
            }
        });


        //默认首页选中
        selectedFragment(0);

    }

    //这个是修改过的，不行改回Main那个

    private void selectedFragment(int position) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(fragmentTransaction); // 隐藏其他 Fragment

        switch (position) {
            case 0: // 首页
                if (mGoodsFragment == null) {
                    mGoodsFragment = new GoodsFragment();
                    fragmentTransaction.add(R.id.content, mGoodsFragment);
                } else {
                    fragmentTransaction.show(mGoodsFragment);
                }
                break;

            case 1: // 用户管理
                if (mClientFragment == null) {
                    mClientFragment = new ClientFragment();
                    fragmentTransaction.add(R.id.content, mClientFragment);
                } else {
                    fragmentTransaction.show(mClientFragment);
                    // 这里可以考虑调用 loadData() 来刷新数据
                    // mClientFragment.loadData();  // 如果需要加载数据
                }
                break;

            case 2: // 订单管理
                if (mSaleFragment == null) {
                    mSaleFragment = new SaleFragment();
                    fragmentTransaction.add(R.id.content, mSaleFragment);
                } else {
                    fragmentTransaction.show(mSaleFragment);
                    // 这里可以考虑调用 loadData() 来刷新数据
                    // mClientFragment.loadData();  // 如果需要加载数据
                }
                break;

            case 3: // 我的
                if (mAmineFragment == null) {
                    mAmineFragment = new AmineFragment();
                    fragmentTransaction.add(R.id.content, mAmineFragment);
                } else {
                    fragmentTransaction.show(mAmineFragment);
                }
                break;

            default:
                break;
        }

        fragmentTransaction.commit(); // 提交事务
    }


    private void hideFragment(FragmentTransaction fragmentTransaction){
        if(mGoodsFragment!=null){
            fragmentTransaction.hide(mGoodsFragment);
        }

        if(mClientFragment!=null){
            fragmentTransaction.hide(mClientFragment);
        }

        if(mSaleFragment!=null){
            fragmentTransaction.hide(mSaleFragment);
        }

        if(mAmineFragment!=null){
            fragmentTransaction.hide(mAmineFragment);
        }
    }
}