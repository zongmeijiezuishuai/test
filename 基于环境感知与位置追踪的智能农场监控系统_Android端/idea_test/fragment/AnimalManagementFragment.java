package com.example.idea_test.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.idea_test.Adapter.AnimalAdapter;
import com.example.idea_test.Handler.AnimalDatabaseHelper;
import com.example.idea_test.R;
import com.example.idea_test.SharedViewModel;
import com.example.idea_test.entity.Animal;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.*;


public class AnimalManagementFragment extends Fragment {
    private static final String TAG = "AnimalManagementFragment";
    private AnimalAdapter adapter;
    private List<Animal> animalList = new ArrayList<>();
    private TextView tvEmpty;
    private MaterialButton btnAddAnimal;
    private AnimalDatabaseHelper dbHelper;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // 获取 Activity 的 ActionBar 并隐藏它
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); // 完全隐藏 ActionBar（包括背景）
        }
        View view = inflater.inflate(R.layout.fragment_animal_management, container, false);


        // 初始化数据库帮助类
        dbHelper = new AnimalDatabaseHelper(getContext());

        // 初始化视图
        RecyclerView rvAnimals = view.findViewById(R.id.rv_animals);
        tvEmpty = view.findViewById(R.id.tv_empty);
        btnAddAnimal = view.findViewById(R.id.btn_add_animal);
        adapter = new AnimalAdapter(new ArrayList<>());

        rvAnimals.setAdapter(adapter);

        // 添加动物按钮点击事件
        btnAddAnimal.setOnClickListener(v -> showAddAnimalDialog());

        initViews(view);
        setupRecyclerView(view);
        loadAnimals();

        return view;
    }

    private void initViews(View view) {
        dbHelper = new AnimalDatabaseHelper(requireContext());
        tvEmpty = view.findViewById(R.id.tv_empty);
        btnAddAnimal = view.findViewById(R.id.btn_add_animal);

        btnAddAnimal.setOnClickListener(v -> showAddAnimalDialog());
    }

    private void setupRecyclerView(View view) {
        RecyclerView rvAnimals = view.findViewById(R.id.rv_animals);
        rvAnimals.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AnimalAdapter(animalList);
        adapter.setOnItemClickListener(new AnimalAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(Animal animal) {
                deleteAnimal(animal);
            }

            @Override
            public void onEditClick(Animal animal) {
                showEditAnimalDialog(animal);  // 新增编辑方法
            }

            @Override
            public void onItemClick(Animal animal) {
                Toast.makeText(getContext(), "点击: " + animal.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        rvAnimals.setAdapter(adapter);
    }


    private void showEditAnimalDialog(Animal animal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_add_animal, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        // 初始化对话框视图
        TextInputEditText etName = dialogView.findViewById(R.id.et_animal_name);
        TextInputEditText etRfid = dialogView.findViewById(R.id.et_animal_rfid);
        TextInputEditText etBreed = dialogView.findViewById(R.id.et_animal_breed);
        TextInputEditText etAge = dialogView.findViewById(R.id.et_animal_age);
        RadioGroup rgGender = dialogView.findViewById(R.id.rg_gender);
        MaterialButton btnScanRfid = dialogView.findViewById(R.id.btn_scan_rfid);
        MaterialButton btnSubmit = dialogView.findViewById(R.id.btn_submit);

        // 设置原有值
        etName.setText(animal.getName());
        etRfid.setText(animal.getRfid());
        etBreed.setText(animal.getBreed());
        etAge.setText(String.valueOf(animal.getAge()));
        if ("公".equals(animal.getGender())) {
            rgGender.check(R.id.rb_male);
        } else {
            rgGender.check(R.id.rb_female);
        }

        // 修改按钮文本为"更新"
        btnSubmit.setText("更新");

        // 扫描RFID按钮逻辑保持不变
        btnScanRfid.setOnClickListener(v -> {
            // ...原有扫描逻辑...
        });

        // 提交按钮 - 现在执行更新操作
        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String rfid = etRfid.getText().toString().trim();
            String breed = etBreed.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();
            String gender = rgGender.getCheckedRadioButtonId() == R.id.rb_male ? "公" : "母";

            // 验证输入
            if (name.isEmpty() || rfid.isEmpty() || breed.isEmpty() || ageStr.isEmpty()) {
                Toast.makeText(getContext(), "请填写所有字段", Toast.LENGTH_SHORT).show();
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "请输入有效的年龄", Toast.LENGTH_SHORT).show();
                return;
            }

            // 创建更新后的动物对象
            Animal updatedAnimal = new Animal();
            updatedAnimal.setName(name);
            updatedAnimal.setRfid(rfid);
            updatedAnimal.setBreed(breed);
            updatedAnimal.setAge(age);
            updatedAnimal.setGender(gender);
            updatedAnimal.setEntryDate(animal.getEntryDate()); // 保持原入库日期
            updatedAnimal.setStatus(animal.getStatus()); // 保持原状态

            // 更新数据库
            new Thread(() -> {
                // 检查名称是否被其他动物使用（除了当前编辑的动物）
                boolean nameExists = dbHelper.checkAnimalNameExistsExcludingCurrent(name, animal.getRfid());
                // 检查RFID是否被其他动物使用
                boolean rfidExists = dbHelper.checkRfidExistsExcludingCurrent(rfid, animal.getRfid());

                requireActivity().runOnUiThread(() -> {
                    if (nameExists) {
                        Toast.makeText(getContext(), "名称已被其他动物使用", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (rfidExists) {
                        Toast.makeText(getContext(), "RFID已被其他动物使用", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 执行更新
                    new Thread(() -> {
                        int result = dbHelper.updateAnimal(animal.getRfid(), updatedAnimal);
                        requireActivity().runOnUiThread(() -> {
                            if (result > 0) {
                                Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();
                                adapter.updateItem(animal.getRfid(), updatedAnimal);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getContext(), "更新失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                });
            }).start();
        });
    }
    private void loadAnimals() {
        Log.d(TAG, "开始加载动物数据...");
        showLoading(true);
        new Thread(() -> {
            try {
                List<Animal> animals = dbHelper.getAllAnimals();
                Log.d(TAG, "从数据库获取到 " + animals.size() + " 条记录");

                requireActivity().runOnUiThread(() -> {
                    adapter.updateData(animals);
                    Log.d(TAG, "适配器数据已更新，当前数量: " + adapter.getItemCount());
                    checkEmptyState();
                    showLoading(false);
                });
            } catch (Exception e) {
                Log.e(TAG, "加载动物数据失败", e);
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "加载数据失败", Toast.LENGTH_SHORT).show();
                    showLoading(false);
                });
            }
        }).start();
    }
    private void showLoading(boolean isLoading) {
        // 可以在这里添加加载动画的显示/隐藏逻辑
    }


    private void checkEmptyState() {
        tvEmpty.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private void showAddAnimalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_add_animal, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        // 初始化对话框视图
        TextInputEditText etName = dialogView.findViewById(R.id.et_animal_name);
        TextInputEditText etRfid = dialogView.findViewById(R.id.et_animal_rfid);
        TextInputEditText etBreed = dialogView.findViewById(R.id.et_animal_breed);
        TextInputEditText etAge = dialogView.findViewById(R.id.et_animal_age);
        RadioGroup rgGender = dialogView.findViewById(R.id.rg_gender);
        MaterialButton btnScanRfid = dialogView.findViewById(R.id.btn_scan_rfid);
        MaterialButton btnSubmit = dialogView.findViewById(R.id.btn_submit);

        // 扫描RFID按钮 - 修改为获取最新RFID
        btnScanRfid.setOnClickListener(v -> {
            // 直接获取 SharedViewModel 实例
            SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

            // 从 ViewModel 获取实时数据
            List<String> rfidList = sharedViewModel.getCardNumbers().getValue();
            Log.d(TAG, "从 ViewModel 获取的 RFID 列表: " + rfidList);

            if (rfidList != null && !rfidList.isEmpty()) {
                String latestRfid = rfidList.get(rfidList.size() - 1);
                etRfid.setText(latestRfid); // 显示最新 RFID
                Toast.makeText(getContext(), "最新 RFID: " + latestRfid, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "暂无 RFID 数据", Toast.LENGTH_SHORT).show();
            }
        });


        // 提交按钮 - 添加数据库操作日志
        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String rfid = etRfid.getText().toString().trim();
            String breed = etBreed.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();
            String gender = rgGender.getCheckedRadioButtonId() == R.id.rb_male ? "公" : "母";

            // 验证输入
            if (name.isEmpty() || rfid.isEmpty() || breed.isEmpty() || ageStr.isEmpty()) {
                Toast.makeText(getContext(), "请填写所有字段", Toast.LENGTH_SHORT).show();
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "请输入有效的年龄", Toast.LENGTH_SHORT).show();
                return;
            }

            // 创建动物对象
            Animal animal = new Animal();
            animal.setName(name);
            animal.setRfid(rfid);
            animal.setBreed(breed);
            animal.setAge(age);
            animal.setGender(gender);
            animal.setEntryDate(new Date());
            animal.setStatus("健康");

            // 添加到数据库 - 添加详细日志
            new Thread(() -> {
                boolean nameExists = dbHelper.checkAnimalNameExists(name);
                boolean rfidExists = dbHelper.checkRfidExists(rfid);

                // 修改提交成功后的处理：
                requireActivity().runOnUiThread(() -> {
                    if (nameExists) {
                        Toast.makeText(getContext(), "名称已存在，请使用其他名称", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (rfidExists) {
                        Toast.makeText(getContext(), "RFID已存在，请使用其他RFID", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    long id = dbHelper.insertAnimal(animal);
                    if (id != -1) {
                        Log.d(TAG, "准备添加动物到适配器: " + animal.toString());
                        Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                        adapter.addItem(animal); // 使用Adapter的addItem方法
                        Log.d(TAG, "添加后适配器数量: " + adapter.getItemCount());
                        checkEmptyState();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "添加失败，RFID可能已存在", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }


    private void deleteAnimal(Animal animal) {
        new AlertDialog.Builder(requireContext())
                .setTitle("确认删除")
                .setMessage("确定要删除" + animal.getName() + "吗？")
                .setPositiveButton("删除", (dialog, which) -> {
                    new Thread(() -> {
                        try {
                            int result = dbHelper.deleteAnimal(animal.getRfid());
                            requireActivity().runOnUiThread(() -> {
                                if (result > 0) {
                                    adapter.removeItem(animal); // 使用Adapter的removeItem方法
                                    checkEmptyState();
                                    Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            Log.e(TAG, "删除动物失败", e);
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show());
                        }
                    }).start();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

}