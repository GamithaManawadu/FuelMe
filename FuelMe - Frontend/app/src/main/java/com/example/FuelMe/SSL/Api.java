package com.example.FuelMe.SSL;

import com.example.FuelMe.models.FuelStation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    String BASE_URL = " http://192.168.8.137:5000";
    @GET("/name")
    Call<List<FuelStation>> loadStations();
}