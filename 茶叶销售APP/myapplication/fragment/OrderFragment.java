package com.example.myapplication.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.OrderListAdapter;
import com.example.myapplication.db.OrderDbHelear;
import com.example.myapplication.entity.OrderInfo;
import com.example.myapplication.entity.UserInfo;

import java.util.List;

public class OrderFragment extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;
    private OrderListAdapter mOrderListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_order, container, false);

        //初始化控件
        recyclerView=rootView.findViewById(R.id.recyclerView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //初始化mOrderListAdapter
        mOrderListAdapter=new OrderListAdapter();

        //设置OrderListAdapter
        recyclerView.setAdapter(mOrderListAdapter);

        //recyclerView点击事件
        mOrderListAdapter.setmOnItemClickListener(new OrderListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(OrderInfo orderInfo, int position) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("温馨提醒")
                        .setMessage("确认是否删除该订单")
                        //后面要检查这两个按键有没有出错！！！！！！！！！！！！！！！！！！！！！！！！！
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int row = OrderDbHelear.getInstance(getActivity()).delete(orderInfo.getOrder_id() + "");
                                loadData();
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
        //获取数据
        loadData();

    }
    public void loadData(){
        UserInfo userInfo=UserInfo.getsUserInfo();
        if(null!=userInfo){
            List<OrderInfo> orderInfos = OrderDbHelear.getInstance(getActivity()).queryOrderListData(userInfo.getUsername());
            mOrderListAdapter.setListData(orderInfos);
        }
    }

}