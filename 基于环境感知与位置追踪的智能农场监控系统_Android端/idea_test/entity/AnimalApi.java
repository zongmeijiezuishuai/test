package com.example.idea_test.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnimalApi {
    // 从云平台获取动物列表
    public static List<Animal> getAnimalsFromCloud() {
        // 实际项目中实现网络请求逻辑
        List<Animal> animals = new ArrayList<>();

        // 模拟数据
        for (int i = 1; i <= 5; i++) {
            Animal animal = new Animal();
            animal.setName("动物" + i);
            animal.setRfid("RFID-" + (10000 + i));
            animal.setBreed(i % 2 == 0 ? "品种A" : "品种B");
            animal.setAge(i % 3 + 1);
            animal.setGender(i % 2 == 0 ? "公" : "母");
            animal.setEntryDate(new Date());
            animal.setStatus(i % 3 == 0 ? "健康" : (i % 3 == 1 ? "患病" : "怀孕"));
            animals.add(animal);
        }

        return animals;
    }

    // 添加动物到云平台
    public static boolean addAnimalToCloud(Animal animal) {
        // 实际项目中实现网络请求逻辑
        return true; // 模拟成功
    }

    // 从云平台删除动物
    public static boolean removeAnimalFromCloud(String rfid) {
        // 实际项目中实现网络请求逻辑
        return true; // 模拟成功
    }
}