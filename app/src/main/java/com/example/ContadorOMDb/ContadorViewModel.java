package com.example.ContadorOMDb;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContadorViewModel extends ViewModel {

    private final MutableLiveData<Integer> contador = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> isRunning = new MutableLiveData<>(false);

    public LiveData<Integer> getContador() {
        return contador;
    }

    public LiveData<Boolean> getIsRunning() {
        return isRunning;
    }

    public void iniciarContador() {
        if (Boolean.TRUE.equals(isRunning.getValue())) return;

        isRunning.setValue(true);
        contador.setValue(0);

        new Thread(() -> {
            for (int i = 1; i <= 20; i++) {
                try {
                    Thread.sleep(1000);
                    // Usamos postValue() porque estamos en un hilo secundario
                    contador.postValue(i);
                } catch (InterruptedException e) {
                    break;
                }
            }
            isRunning.postValue(false);
        }).start();
    }
}