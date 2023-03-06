package com.startup.go4lunch.api.utilApi;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface OverpassRetrofitService {

    @GET("api/interpreter?data=[out:json][timeout:100];nwr['amenity'='restaurant'](47.892466327195194,1.8943480243466757,47.91112700305079,1.9238203758926233);out body;")
    Call<OverpassGsonObject> getRestaurantList();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://overpass-api.de/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}