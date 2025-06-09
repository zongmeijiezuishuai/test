package com.example.myapplication;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.adapter.OrderWithComments;
import com.example.myapplication.db.OrderDbHelear;

import java.util.Calendar;
import java.util.List;

public class CommentViewModel extends AndroidViewModel {
    private OrderDbHelear dbHelper;
    private LiveData<List<OrderWithComments>> ordersWithComments;

    public CommentViewModel(@NonNull Application application) {
        super(application);
        dbHelper = OrderDbHelear.getInstance(application);
        // 初始化 LiveData
        ordersWithComments = new MutableLiveData<>();
        loadOrdersWithComments();
    }

    private void loadOrdersWithComments() {
        AsyncTask.execute(() -> {
            List<OrderWithComments> orderList = dbHelper.fetchOrdersWithComments();
            ((MutableLiveData<List<OrderWithComments>>) ordersWithComments).postValue(orderList);
        });
    }

    private List<OrderWithComments> fetchOrdersWithCommentsFromDatabase() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return null; // 替换为实际的数据获取逻辑
    }

    public LiveData<List<OrderWithComments>> getOrdersWithComments() {
        return ordersWithComments;
    }
}