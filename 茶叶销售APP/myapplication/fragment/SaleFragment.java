package com.example.myapplication.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.OrderListAdapter;
import com.example.myapplication.adapter.OrdersaleAdapter;
import com.example.myapplication.db.OrderDbHelear;
import com.example.myapplication.entity.OrderInfo;
import com.example.myapplication.entity.UserInfo;

//import org.chromium.base.task.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class SaleFragment extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;
    private OrdersaleAdapter mOrdersaleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sale, container, false);
        // 初始化控件
        recyclerView = rootView.findViewById(R.id.recyclerView);
        return rootView;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        // 初始化mOrderListAdapter，传入空列表和上下文
//        List<OrderInfo> orderList = new ArrayList<>();
//        mOrdersaleAdapter = new OrdersaleAdapter(orderList, getContext());
//
//        // 设置OrderListAdapter
//        recyclerView.setAdapter(mOrdersaleAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        // 获取数据
//        loadData();
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<OrderInfo> orderList = new ArrayList<>();
        mOrdersaleAdapter = new OrdersaleAdapter(orderList, getContext());

        recyclerView.setAdapter(mOrdersaleAdapter);

        // 设置长按监听器
        mOrdersaleAdapter.setOnItemLongClickListener(new OrdersaleAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(OrderInfo orderInfo, int position) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("温馨提醒")
                        .setMessage("确认是否删除该订单")
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 取消操作
                            }
                        })
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int row = OrderDbHelear.getInstance(getActivity()).delete(orderInfo.getOrder_id() + "");
                                loadData();
                                if (row > 0) {
                                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .show();
            }
        });

        // 获取数据
        loadData();
    }

    public void onOrderStatusChanged(final OrderInfo orderInfo) {
        Executors.newSingleThreadExecutor().submit(() -> {
            // 更新数据库中的订单状态
            boolean success = OrderDbHelear.getInstance(getActivity()).updateOrderStatus(orderInfo.getOrder_id(), "已发货");

            // 在主线程更新UI
            getActivity().runOnUiThread(() -> {
                if (success) {
                    Toast.makeText(getContext(), "订单状态已更改为已发货", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "更新订单状态失败", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public void loadData() {
        List<OrderInfo> orderInfos = OrderDbHelear.getInstance(getActivity()).queryAllOrderListData();
        mOrdersaleAdapter.setListData(orderInfos);
    }
}