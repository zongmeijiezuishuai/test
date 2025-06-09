package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.FeedbackItem;
import com.example.myapplication.R;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>{

    private List<FeedbackItem> feedbackList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(FeedbackItem item);
        void onDeleteClick(FeedbackItem item);
    }

    public FeedbackAdapter(List<FeedbackItem> feedbackList, OnItemClickListener  listener) {
        this.feedbackList = feedbackList;
        this.listener = listener;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        FeedbackItem feedbackItem = feedbackList.get(position);
        holder.tvName.setText(feedbackItem.getName());
        holder.tvEmail.setText(feedbackItem.getEmail());
        holder.tvMessage.setText(feedbackItem.getMessage());

//        if (feedbackItem.isRead()) {
//            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(android.R.color.darker_gray));
//        } else {
//            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(android.R.color.white));
//        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(feedbackItem));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(feedbackItem));

    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    static class FeedbackViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        TextView tvEmail;
        TextView tvMessage;

        Button btnDelete;
        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvMessage = itemView.findViewById(R.id.tv_message);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
