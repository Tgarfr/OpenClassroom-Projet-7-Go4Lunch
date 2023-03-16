package com.startup.go4lunch.api;

import com.startup.go4lunch.model.OverpassGsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OverpassService {

    @GET("api/interpreter")
    Call<OverpassGsonObject> getRestaurantList(@Query("data") String data);
}