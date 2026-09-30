package com.example.ContadorOMDb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OmdbApi {
    @GET("/")
    Call<PeliculaDto> getPelicula(
            @Query("apikey") String apiKey,
            @Query("i") String imdbId
    );
}