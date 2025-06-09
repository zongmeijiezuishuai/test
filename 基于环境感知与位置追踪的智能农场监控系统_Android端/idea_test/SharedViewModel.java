package com.example.idea_test;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<String>> cardNumbers = new MutableLiveData<>(new ArrayList<>());

    public void addCardNumber(String cardNumber) {
        List<String> currentList = cardNumbers.getValue();
        currentList.add(cardNumber);
        cardNumbers.setValue(currentList);
    }

    public LiveData<List<String>> getCardNumbers() {
        return cardNumbers;
    }
}