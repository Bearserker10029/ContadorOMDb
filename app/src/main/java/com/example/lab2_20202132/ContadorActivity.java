package com.example.lab2_20202132;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab2_20202132.databinding.ActivityContadorBinding;

public class ContadorActivity extends AppCompatActivity {
    private ActivityContadorBinding binding;
    private int contador = 0;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState != null) {
            contador = savedInstanceState.getInt("CONTADOR_KEY", 0);
            isRunning = savedInstanceState.getBoolean("IS_RUNNING_KEY", false);
            binding.textView4.setText(String.valueOf(contador));
            if (isRunning && contador < 20) {
                iniciarConteoThread();
            }
        }

        binding.btnIniciar.setOnClickListener(v -> {
            if (!isRunning) {
                contador = 0;
                iniciarConteoThread();
            }
        });

        binding.btnRegresar.setOnClickListener(v -> finish());
    }

    private void iniciarConteoThread() {
        isRunning = true;
        binding.btnIniciar.setEnabled(false);

        new Thread(() -> {
            while (contador < 20 && isRunning) {
                try {
                    Thread.sleep(1000);
                    contador++;
                    runOnUiThread(() -> binding.textView4.setText(String.valueOf(contador)));
                } catch (InterruptedException e) {
                    break;
                }
            }
            isRunning = false;
            runOnUiThread(() -> binding.btnIniciar.setEnabled(true));
        }).start();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CONTADOR_KEY", contador);
        outState.putBoolean("IS_RUNNING_KEY", isRunning);
    }
}