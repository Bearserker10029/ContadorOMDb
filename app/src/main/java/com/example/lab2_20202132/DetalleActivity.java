package com.example.lab2_20202132;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab2_20202132.databinding.ActivityDetalleBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetalleActivity extends AppCompatActivity {
    private ActivityDetalleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String imdbId = getIntent().getStringExtra("IMDB_ID");
        if (imdbId != null) {
            obtenerDetallesPelicula(imdbId);
        }

        binding.btnRegresar.setOnClickListener(v -> mostrarDialogoConfirmacion());
    }

    private void obtenerDetallesPelicula(String imdbId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OmdbApi api = retrofit.create(OmdbApi.class);
        api.getPelicula("bf81d461", imdbId).enqueue(new Callback<PeliculaDto>() {
            @Override
            public void onResponse(Call<PeliculaDto> call, Response<PeliculaDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PeliculaDto pelicula = response.body();
                    if ("True".equalsIgnoreCase(pelicula.getResponse())) {
                        binding.titulo.setText("Título: " + pelicula.getTitle());
                        binding.ano.setText("Año: " + pelicula.getYear());
                    } else {
                        Toast.makeText(DetalleActivity.this, "Película no encontrada", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PeliculaDto> call, Throwable t) {
                Toast.makeText(DetalleActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoConfirmacion() {
        new AlertDialog.Builder(this)
                .setMessage("¿Desea volver al menú principal?")
                .setPositiveButton("Sí", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }
}