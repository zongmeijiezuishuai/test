package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ProductDetailsActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.LeftListAdapter;
import com.example.myapplication.adapter.RightListAdapter;
import com.example.myapplication.entity.DataService;
import com.example.myapplication.entity.ProductInfo;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View rootView;
    private RecyclerView leftRecyclerView;
    private RecyclerView rightRecyclerView;
    private LeftListAdapter mLeftListAdapter;

    private RightListAdapter mRightListAdapter;
    private List<String> leftDataList=new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_home,container,false);

        //初始化控件
        leftRecyclerView = rootView.findViewById(R.id.leftRecyclerView);
        rightRecyclerView = rootView.findViewById(R.id.rightRecyclerView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        leftDataList.add("红茶");
        leftDataList.add("绿茶");
        leftDataList.add("青茶（乌龙茶）");
        leftDataList.add("黄茶");
        leftDataList.add("黑茶");
        leftDataList.add("白茶");

        mLeftListAdapter=new LeftListAdapter(leftDataList);
        leftRecyclerView.setAdapter(mLeftListAdapter);

        mRightListAdapter =new RightListAdapter();
        rightRecyclerView.setAdapter(mRightListAdapter);

        //默认加载红茶数据
        mRightListAdapter.setListData(DataService.getListData(0));

        // 设置右侧RecyclerView点击事件监听器
        mRightListAdapter.setOnItemClickListener(new RightListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(ProductInfo productInfo, int position) {
                //跳转传值
                Intent intent=new Intent(getActivity(), ProductDetailsActivity.class);
                //Intent传递对象的时候，实体类一定要implements Serializable
                intent.putExtra("productInfo",productInfo);
                startActivity(intent);
            }
        });
        
        //recyclerView点击事件
        mLeftListAdapter.setmLeftListOnClickItemListener(new LeftListAdapter.LeftListOnClickItemListener() {
            @Override
            public void onItemClick(int position) {
                mLeftListAdapter.setCurrentIndex(position);
                //点击左侧分类切换对应的列表数据
                mRightListAdapter.setListData(DataService.getListData(position));
            }
        });

    }
}