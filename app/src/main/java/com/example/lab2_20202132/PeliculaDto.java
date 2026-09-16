package com.example.lab2_20202132;

import com.google.gson.annotations.SerializedName;

public class PeliculaDto {
    @SerializedName("Title")
    private String title;

    @SerializedName("Year")
    private String year;

    @SerializedName("Response")
    private String response;

    public String getTitle() { return title; }
    public String getYear() { return year; }
    public String getResponse() { return response; }
}