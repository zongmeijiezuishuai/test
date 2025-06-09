package com.example.myapplication.adapter;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.db.OrderDbHelear;
import com.example.myapplication.entity.CommentInfo;
import com.example.myapplication.entity.OrderInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyHolder> {

    private List<OrderWithComments> mOrdersWithComments = new ArrayList<>();
    private OrderDbHelear dbHelper;
    private Context mContext;

    // 构造函数，传入上下文和数据库帮助类实例
    public CommentAdapter(Context context, OrderDbHelear dbHelper) {
        this.mContext = context;
        this.dbHelper = dbHelper;
    }

    // 设置数据列表
    public void setListData(List<OrderWithComments> list) {
        if (list != null) {
            this.mOrdersWithComments = list;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_comment, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        OrderWithComments orderWithComments = mOrdersWithComments.get(position);

        // 绑定订单信息
        OrderInfo orderInfo = orderWithComments.getOrderInfo();
        holder.product_count.setText("x" + orderInfo.getProduct_count());
        holder.product_title.setText(orderInfo.getProduct_title());
        holder.product_price.setText(String.valueOf(orderInfo.getProduct_price()));
        holder.product_img.setImageResource(orderInfo.getProduct_img());
        holder.address.setText("【" + orderInfo.getAddress() + "】；联系方法：" + orderInfo.getMobile());

        // 绑定已有评论
        bindComments(holder, orderWithComments.getComments());

        // 设置提交评论的逻辑，并传递 position 参数
        setupCommentSubmission(holder,position);
    }

    private void bindComments(MyHolder holder, List<CommentInfo> comments) {
        // 这里可以是将评论显示在一个子RecyclerView或者动态添加TextView来展示所有评论
        //后面再写
    }

    private void setupCommentSubmission(MyHolder holder, final int position) {
        holder.input_box.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // 显示确认对话框
                showConfirmDialog(holder, position);
                return true;
            }
            return false;
        });
    }

    private void showConfirmDialog(final MyHolder holder, final int position) {
        new AlertDialog.Builder(holder.itemView.getContext())
                .setTitle("确认评论")
                .setMessage("您确定要提交以下评论吗？\n" + holder.input_box.getText().toString())
                .setPositiveButton("确定", (dialog, which) -> {
                    // 用户点击了确定按钮
                    String commentContent = holder.input_box.getText().toString();
                    float ratingFloat = holder.user_rating.getRating();
                    double rating = (double) ratingFloat;

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String createdAt = dateFormat.format(new Date());

                    saveComment(mOrdersWithComments.get(position).getOrderId(), holder, position, commentContent, rating, createdAt);
                })
                .setNegativeButton("取消", null) // 用户点击取消后不做任何事情
                .create()
                .show();
    }

    private void saveComment(int orderId, MyHolder holder, int position, String commentContent, double rating, String createdAt) {
        // 获取当前登录用户的用户名
        SharedPreferences prefs = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "default_user");

        // 确保 dbHelper 不为 null
        if (dbHelper == null) {
            dbHelper = new OrderDbHelear(mContext, "orders.db", null, 3); // 或者使用适当的构造参数
        }

        // 确保这个方法不是在主线程上调用
        AsyncTask.execute(() -> {
            SQLiteDatabase db = null;
            try {
                db = dbHelper.getWritableDatabase();
                db.beginTransaction();

                ContentValues values = new ContentValues();
                values.put("order_id", orderId);
                values.put("user_name", username); // 使用实际的用户名
                values.put("content", commentContent);
                values.put("rating", rating);
                values.put("created_at", createdAt);

                // 执行插入操作并获取插入的行数
                long insertId = db.insert("comments_table", null, values);

                if (insertId != -1) { // 插入成功
                    db.setTransactionSuccessful();

                    // 更新内存中的评论列表
                    OrderWithComments orderWithComments = mOrdersWithComments.get(position);
                    List<CommentInfo> comments = orderWithComments.getComments();
                    if (comments == null) {
                        comments = new ArrayList<>();
                        orderWithComments.setComments(comments);
                    }
                    CommentInfo newComment = new CommentInfo((int) insertId, orderId, username, rating, commentContent, createdAt);
                    comments.add(newComment);

                    // 更新 UI 需要在主线程中完成
                    ((Activity) mContext).runOnUiThread(() -> {
                        notifyItemInserted(position); // 刷新特定位置，注意这里使用 notifyItemInserted 以反映新添加的项目

                        // 显示成功的提示
                        Toast.makeText(mContext, "评论提交成功！", Toast.LENGTH_SHORT).show();
                        Log.d("CommentAdapter", "Comment inserted with ID: " + insertId); // 添加日志输出

                        // 将新评论显示在输入框下方或适当的位置
                        // 假设 holder 中有一个用于显示评论的视图，例如 RecyclerView 或 ListView
                        // 如果是 RecyclerView，则需要调用 adapter.notifyDataSetChanged() 或其他合适的方法来刷新视图
                    });
                } else {
                    // 插入失败
                    ((Activity) mContext).runOnUiThread(() -> {
                        Toast.makeText(mContext, "评论提交失败，请重试。", Toast.LENGTH_SHORT).show();
                        Log.e("CommentAdapter", "Failed to insert comment into database");
                    });
                }
            } catch (SQLiteException e) {
                // 处理异常
                Log.e("CommentAdapter", "Database operation failed: ", e);
                ((Activity) mContext).runOnUiThread(() -> {
                    Toast.makeText(mContext, "评论提交失败，请重试。", Toast.LENGTH_SHORT).show();
                });
            } finally {
                if (db != null) {
                    db.endTransaction();
                    // 注意：不要在此处关闭数据库连接，因为 dbHelper 可能在应用的其他地方使用
                    // db.close();
                }
            }
        });

        // 不要在这里清空输入框和评分
        // holder.input_box.setText("");
        // holder.user_rating.setRating(0); // 如果有 user_rating 控件
    }

    @Override
    public int getItemCount() {
        return mOrdersWithComments.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        ImageView product_img;
        TextView product_title;
        TextView product_price;
        TextView product_count;
        TextView address;
        RatingBar user_rating;
        EditText input_box;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            product_img = itemView.findViewById(R.id.product_img);
            product_title = itemView.findViewById(R.id.product_title);
            product_price = itemView.findViewById(R.id.product_price);
            product_count = itemView.findViewById(R.id.product_count);
            address = itemView.findViewById(R.id.address);
            user_rating = itemView.findViewById(R.id.user_rating);
            input_box = itemView.findViewById(R.id.input_box);
        }
    }

    // 点击事件接口
    private onItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(onItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(OrderWithComments orderWithComments, int position);
    }
}

