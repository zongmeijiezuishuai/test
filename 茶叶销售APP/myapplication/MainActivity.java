package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.fragment.CarFragment;
import com.example.myapplication.fragment.HomeFragment;
import com.example.myapplication.fragment.HotFragment;
import com.example.myapplication.fragment.MineFragment;
import com.example.myapplication.fragment.OrderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //先定义
    private HotFragment mHotFragment;
    private HomeFragment mHomeFragment;
    private CarFragment mCarFragment;
    private OrderFragment mOrderFragment;
    private MineFragment mMineFragment;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        mBottomNavigationView=findViewById(R.id.bottomNavigationView);
        //mBottomNavigationView设置点击事件
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.hot){
                    selectedFragment(0);
                    
                } else if (item.getItemId()==R.id.home) {
                    selectedFragment(1);
                    
                } else if (item.getItemId()==R.id.car) {
                    selectedFragment(2);
                }  else if (item.getItemId()==R.id.order) {
                    selectedFragment(3);
                } else {
                    selectedFragment(4);
                }
                return true;
            }
        });


        //默认首页选中
        selectedFragment(0);

    }

    private void selectedFragment(int position) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(fragmentTransaction); // 隐藏其他 Fragment

        if(position == 0) {  // 热卖
            if(mHotFragment == null) {
                mHotFragment = new HotFragment();
                fragmentTransaction.add(R.id.content, mHotFragment);
            } else {
                fragmentTransaction.show(mHotFragment);
            }
        } else if (position == 1) {  // 分类
            if(mHomeFragment == null) {
                mHomeFragment = new HomeFragment();
                fragmentTransaction.add(R.id.content, mHomeFragment);
            } else {
                fragmentTransaction.show(mHomeFragment);
            }
        } else if (position == 2) {  // 购物车
            if(mCarFragment == null) {
                mCarFragment = new CarFragment();
                fragmentTransaction.add(R.id.content, mCarFragment);
            } else {
                fragmentTransaction.show(mCarFragment);
                //要加载数据，就是刷新
                mCarFragment.loadData();
            }
        }else if (position == 3) {  // 订单
            if(mOrderFragment == null) {
                mOrderFragment = new OrderFragment();
                fragmentTransaction.add(R.id.content, mOrderFragment);
            } else {
                fragmentTransaction.show(mOrderFragment);
                //要加载数据，就是刷新
                mOrderFragment.loadData();
            }
        } else {  // 我的
            if(mMineFragment == null) {
                mMineFragment = new MineFragment();
                fragmentTransaction.add(R.id.content, mMineFragment);
            } else {
                fragmentTransaction.show(mMineFragment);
            }
        }

        fragmentTransaction.commit();  // 提交事务
    }


    private void hideFragment(FragmentTransaction fragmentTransaction){
        if(mHotFragment!=null){
            fragmentTransaction.hide(mHotFragment);
        }

        if(mHomeFragment!=null){
            fragmentTransaction.hide(mHomeFragment);
        }

        if(mCarFragment!=null){
            fragmentTransaction.hide(mCarFragment);
        }

        if(mOrderFragment!=null){
            fragmentTransaction.hide(mOrderFragment);
        }

        if(mMineFragment!=null){
            fragmentTransaction.hide(mMineFragment);
        }
    }

}