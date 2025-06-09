package com.example.idea_test.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.idea_test.R;
import org.jetbrains.annotations.NotNull;
import android.util.Log;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private List<String> cardNumbers;

    public CardAdapter(List<String> cardNumbers) {
        this.cardNumbers = cardNumbers;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_number, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        String cardNumber = cardNumbers.get(position);
        holder.cardNumberText.setText("卡号: " + cardNumber);
        Log.d("CardAdapter", "显示RFID: " + cardNumbers.get(position));
        holder.cardStatusText.setText("状态: 在线"); // 根据实际状态设置
    }

    @Override
    public int getItemCount() {
        return cardNumbers.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView cardNumberText;
        TextView cardStatusText;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardNumberText = itemView.findViewById(R.id.card_number);
            cardStatusText = itemView.findViewById(R.id.card_status);
        }
    }
}