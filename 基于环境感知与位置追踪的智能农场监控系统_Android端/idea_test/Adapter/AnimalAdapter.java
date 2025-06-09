package com.example.idea_test.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.collection.CircularArray;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.example.idea_test.R;
import com.example.idea_test.entity.Animal;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {
    private static List<Animal> animalList;
    private static OnItemClickListener listener;


    // 定义点击事件接口
    public interface OnItemClickListener {
        void onDeleteClick(Animal animal);
        void onItemClick(Animal animal);
        void onEditClick(Animal animal);  // 新增编辑点击

    }

    public AnimalAdapter(List<Animal> animalList) {
        this.animalList = animalList != null ? animalList : new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @NotNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_animal, parent, false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AnimalViewHolder holder, int position) {
        Animal animal = animalList.get(position);
        holder.bind(animal);

        // 设置删除按钮点击事件
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(animal);
            }
        });

        // 设置整个项的点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(animal);
            }
        });
    }

        public void updateData(List<Animal> newList) {
        this.animalList = newList != null ? new ArrayList<>(newList) : new ArrayList<>();
        notifyDataSetChanged(); // 必须在主线程调用
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }

    public void addItem(Animal animal) {
        animalList.add(animal);
        notifyItemInserted(animalList.size() - 1);
    }

    public void removeItem(Animal animal) {
        int position = animalList.indexOf(animal);
        if (position >= 0) {
            animalList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateItem(String oldRfid, Animal updatedAnimal) {
        for (int i = 0; i < animalList.size(); i++) {
            if (animalList.get(i).getRfid().equals(oldRfid)) {
                animalList.set(i, updatedAnimal);
                notifyItemChanged(i);
                break;
            }
        }
    }



    public static class AnimalViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvRfid, tvBreed, tvAge, tvGender, tvStatus, tvEntryDate;
        public MaterialButton btnDelete,btnEdit;

        public AnimalViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_animal_name);
            tvRfid = itemView.findViewById(R.id.tv_animal_rfid);
            tvBreed = itemView.findViewById(R.id.tv_animal_breed);
            tvAge = itemView.findViewById(R.id.tv_animal_age);
            tvGender = itemView.findViewById(R.id.tv_animal_gender);
            tvStatus = itemView.findViewById(R.id.tv_animal_status);
            tvEntryDate = itemView.findViewById(R.id.tv_animal_entry_date);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnEdit = itemView.findViewById(R.id.btn_edit);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(animalList.get(position));
                }
            });

            // Set up the edit button click listener if needed
            btnEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEditClick(animalList.get(position));  // Assuming you have this method in the interface
                }
            });

        }

        public void bind(Animal animal) {
            tvName.setText(animal.getName());
            tvRfid.setText(animal.getRfid());          // 移除 "RFID: "
            tvBreed.setText(animal.getBreed());        // 移除 "品种: "
            tvAge.setText(String.valueOf(animal.getAge())); // 移除 "年龄: " 和 "岁"
            tvGender.setText(animal.getGender());      // 移除 "性别: "
            tvStatus.setText(animal.getStatus());      // 移除 "状态: "

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            tvEntryDate.setText(sdf.format(animal.getEntryDate())); // 移除 "入库日期: "
        }
    }
}
