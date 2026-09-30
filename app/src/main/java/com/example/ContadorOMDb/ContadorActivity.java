package com.example.ContadorOMDb;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.ContadorOMDb.databinding.ActivityContadorBinding;

public class ContadorActivity extends AppCompatActivity {

    private ActivityContadorBinding binding;
    private ContadorViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Instanciar ViewModel
        viewModel = new ViewModelProvider(this).get(ContadorViewModel.class);

        // Observar el valor del contador
        viewModel.getContador().observe(this, valor -> {
            binding.textView4.setText(String.valueOf(valor));
        });

        // Observar el estado de ejecución para habilitar/deshabilitar el botón
        viewModel.getIsRunning().observe(this, corriendo -> {
            binding.btnIniciar.setEnabled(!corriendo);
        });

        // Eventos de los botones
        binding.btnIniciar.setOnClickListener(v -> viewModel.iniciarContador());
        binding.btnRegresar.setOnClickListener(v -> finish());
    }
}