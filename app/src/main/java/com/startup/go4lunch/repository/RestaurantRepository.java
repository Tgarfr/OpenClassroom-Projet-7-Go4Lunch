package com.startup.go4lunch.repository;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.startup.go4lunch.api.RestaurantApi;
import com.startup.go4lunch.model.Restaurant;

import java.util.List;

public class RestaurantRepository {

    private MutableLiveData<List<Restaurant>> restaurantListLivedata;
    private final RestaurantApi restaurantApi;

    public RestaurantRepository(@NonNull RestaurantApi restaurantApi) {
        this.restaurantApi = restaurantApi;
    }

    @NonNull
    public LiveData<List<Restaurant>> getRestaurantListLiveData() {
        return restaurantListLivedata;
    }

    public void updateLocationRestaurantList(@NonNull Location location) {
        restaurantListLivedata = restaurantApi.getRestaurantListLiveData(location);
    }
}