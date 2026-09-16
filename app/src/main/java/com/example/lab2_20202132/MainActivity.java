package com.example.lab2_20202132;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab2_20202132.databinding.ActivityMainBinding;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Ir al Contador
        binding.btnIngresar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContadorActivity.class);
            startActivity(intent);
        });

        // Comprobar Conexión
        binding.btnComprobar.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                Toast.makeText(this, "Success: Conexión establecida", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error: Sin conexión a Internet", Toast.LENGTH_SHORT).show();
            }
        });

        // Buscar Película
        binding.btnBuscar.setOnClickListener(v -> {
            String imdbId = binding.etCodigo.getText().toString().trim();
            if (imdbId.isEmpty()) {
                Toast.makeText(this, "Ingrese un ID de IMDb válido", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, DetalleActivity.class);
            intent.putExtra("IMDB_ID", imdbId);
            startActivity(intent);
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        Network network = cm.getActiveNetwork();
        if (network == null) return false;

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

}