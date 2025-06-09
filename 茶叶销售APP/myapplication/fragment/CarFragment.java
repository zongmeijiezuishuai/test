package com.example.myapplication.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CarListAdapter;
import com.example.myapplication.db.CarDbHelear;
import com.example.myapplication.db.OrderDbHelear;
import com.example.myapplication.entity.CarInfo;
import com.example.myapplication.entity.UserInfo;

import java.util.List;


public class CarFragment extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;
    private TextView total;
    private Button btn_total;
    private CarListAdapter mCarListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_car, container, false);

        //初始化控件
        recyclerView =rootView.findViewById(R.id.recyclerView);
        total=rootView.findViewById(R.id.total);
        btn_total=rootView.findViewById(R.id.btn_total);

    return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //初始化mCarListAdapter
        mCarListAdapter = new CarListAdapter();
        //设置设配器
        recyclerView.setAdapter(mCarListAdapter);

        //recyclerView点击事件
        mCarListAdapter.setmOnItemClickListener(new CarListAdapter.onItemClickListener() {
            @Override
            public void onPlusOnClick(CarInfo carInfo, int position) {
                //加
                CarDbHelear.getInstance(getActivity()).updateProduct(carInfo.getCar_id(),carInfo);
                //注意一定要刷新数据
                loadData();
            }

            @Override
            public void onSubtractOnClick(CarInfo carInfo, int position) {
                //减
                CarDbHelear.getInstance(getActivity()).subStartupdateProduct(carInfo.getCar_id(),carInfo);
                loadData();
            }

            @Override
            public void delOnClick(CarInfo carInfo, int position) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("温馨提醒")
                        .setMessage("确认是否删除该商品")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CarDbHelear.getInstance(getActivity()).delete(carInfo.getCar_id()+"");
                                loadData();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })

                        .show();

            }
        });

        //合计点击事件
        btn_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //批量将购物车的数据生成订单
                UserInfo userInfo = UserInfo.getsUserInfo();
                if(null!=userInfo){
                    List<CarInfo> carList = CarDbHelear.getInstance(getActivity()).queryCarList(userInfo.getUsername());
                    if(carList.size()==0){
                        Toast.makeText(getActivity(),"您还没有选择商品！",Toast.LENGTH_SHORT).show();
                    }else {

                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pay_dialog_layout, null);

                        EditText et_mobile=view.findViewById(R.id.et_mobile);
                        EditText et_address=view.findViewById(R.id.et_address);
                        TextView tv_total=view.findViewById(R.id.tv_total);

                        //设置收款码总的价格
                        tv_total.setText(total.getText().toString());

                        //看这里输入的是view还是v
                        new AlertDialog.Builder(getActivity())
                                .setTitle("支付温馨提醒")
                                .setView(view)
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String address=et_address.getText().toString();
                                        String mobile=et_mobile.getText().toString();
                                        if(TextUtils.isEmpty(address) || TextUtils.isEmpty(mobile)){
                                            Toast.makeText(getActivity(),"请完善信息",Toast.LENGTH_SHORT).show();
                                        }else {
                                            //生成点订单
                                            OrderDbHelear.getInstance(getActivity()).insertByAll(carList,address,mobile);
                                            //清空购物车
                                            for (int index = 0; index < carList.size(); index++) { // 修改了这里的变量名，之前是i看后妈回不会出错！！！！！！
                                                CarDbHelear.getInstance(getActivity()).delete(carList.get(index).getCar_id() + "");
                                            }
                                            // 提示清空购物车成功
                                            Toast.makeText(getActivity(), "太有实力了！恭喜你清空购物车啦", Toast.LENGTH_SHORT).show();
                                            loadData();
                                            Toast.makeText(getActivity(), "支付成功咯！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .show();
                    }
                }
            }
        });

        loadData();
    }

    //一进到购物车界面就要结算好
    private void setTotalData(List<CarInfo> list){
        int toTalCount=0;

        for (int i = 0; i < list.size(); i++) {
            int price =list.get(i).getProduct_price()*list.get(i).getProduct_count();
            toTalCount=toTalCount+price;
        }

        //设置数据
        total.setText(toTalCount+".00");
    }

    public void loadData(){

        UserInfo userInfo = UserInfo.getsUserInfo();
        if(null!=userInfo){
            //获取数据,这个用户名要和ProductDetailsActivity里面的用户名一样！！！！！！！！！
            //这里已修改过了
            List<CarInfo> carList = CarDbHelear.getInstance(getActivity()).queryCarList(userInfo.getUsername());
            //设置数据
            mCarListAdapter.setCarInfoList(carList);

            //结算总价格
            setTotalData(carList);
        }

    }

}