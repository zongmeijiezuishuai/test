package com.example.myapplication.fragment;

import static android.app.Activity.RESULT_OK;

import static java.nio.file.Files.delete;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.AddGoodsActivity;
import com.example.myapplication.R;
import com.example.myapplication.UpdataProductActivity;
import com.example.myapplication.adapter.LeftListAdapter;
import com.example.myapplication.adapter.RightListGoodsAdapter;
import com.example.myapplication.db.GoodsDbHelear;
import com.example.myapplication.db.OrderDbHelear;
import com.example.myapplication.entity.GoodsInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class GoodsFragment extends Fragment {

    private View rootView;
    private RecyclerView leftRecyclerView;
    private RecyclerView rightRecyclerView;
    private LeftListAdapter mLeftListAdapter;

    private RightListGoodsAdapter mRightListGoodsAdapter;

    private List<String> leftDataList=new ArrayList<>();
    private FloatingActionButton fab;
    private static final int REQUEST_CODE_UPDATE = 1001;
    private List<GoodsInfo> goodsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_goods, container, false);

        // 初始化控件
        leftRecyclerView = rootView.findViewById(R.id.leftRecyclerView);
        rightRecyclerView = rootView.findViewById(R.id.rightRecyclerView);
        fab=rootView.findViewById(R.id.fab);

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

        mRightListGoodsAdapter =new RightListGoodsAdapter();
        rightRecyclerView.setAdapter(mRightListGoodsAdapter);

        //默认加载红茶数据
        loadGoodsData("红茶");

        // 设置右侧RecyclerView点击事件监听器
        mRightListGoodsAdapter.setOnItemClickListener(new RightListGoodsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(GoodsInfo goodsInfo, int position) {
                // 跳转到更新页面，传递商品信息
                Intent intent = new Intent(getActivity(), UpdataProductActivity.class);
                intent.putExtra("goodsInfo", goodsInfo);  // 传递商品信息
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_UPDATE);
            }
        });
        // 设置右侧RecyclerView长按事件监听器
        mRightListGoodsAdapter.setOnItemLongClickListener(new RightListGoodsAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(GoodsInfo goodsInfo, int position) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("温馨提醒")
                        .setMessage("确认是否删除该商品")
                        //后面要检查这两个按键有没有出错！！！！！！！！！！！！！！！！！！！！！！！！！
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int row = GoodsDbHelear.getInstance(getActivity()).delete(goodsInfo.getProductId() + "");
                                if(row>0){
                                    Toast.makeText(getActivity(),"删除成功",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getActivity(),"删除失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })

                        .show();
            }
        });



        //recyclerView点击事件
        mLeftListAdapter.setmLeftListOnClickItemListener(new LeftListAdapter.LeftListOnClickItemListener() {
            @Override
            public void onItemClick(int position) {
                // 更新当前选中的左侧分类
                mLeftListAdapter.setCurrentIndex(position);
                // 获取点击的分类类型
                String type = leftDataList.get(position);
                // 根据选择的分类加载右侧商品数据
                loadGoodsData(type);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到添加商品界面
                Intent intent = new Intent(getActivity(), AddGoodsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadGoodsData(String type) {

        List<GoodsInfo> goodsList = GoodsDbHelear.getInstance(getActivity()).getAllGoodsInfoByType(type);     // 更新右侧的RecyclerView适配器
        mRightListGoodsAdapter.setGoodsList(goodsList);
    }

    //中处理 UpdataProductActivity 返回的结果，并刷新 RecyclerView 的数据。
    // 处理商品信息更新并刷新 RecyclerView
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE && resultCode == RESULT_OK) {
            // 获取更新后的商品信息
            GoodsInfo updatedGoodsInfo = (GoodsInfo) data.getSerializableExtra("updatedGoodsInfo");
            if (updatedGoodsInfo != null) {
                // 更新数据源
                updateDataSource(updatedGoodsInfo);
                // 找到更新的位置并刷新该项
                int position = getUpdatedItemPosition(updatedGoodsInfo);
                if (position != -1) {
                    mRightListGoodsAdapter.notifyItemChanged(position);
                }
            }
        }
    }

    // 更新数据源
    private void updateDataSource(GoodsInfo updatedGoodsInfo) {
        for (int i = 0; i < goodsList.size(); i++) {
            if (goodsList.get(i).getProductId() == updatedGoodsInfo.getProductId()) {
                goodsList.set(i, updatedGoodsInfo);
                break;
            }
        }
    }

    // 获取更新商品的索引位置
    private int getUpdatedItemPosition(GoodsInfo updatedGoodsInfo) {
        for (int i = 0; i < goodsList.size(); i++) {
            if (goodsList.get(i).getProductId() == updatedGoodsInfo.getProductId()) {
                return i;
            }
        }
        return -1; // 如果未找到对应的商品
    }
}

