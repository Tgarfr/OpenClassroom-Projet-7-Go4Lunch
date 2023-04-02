package com.startup.go4lunch.repository;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.startup.go4lunch.api.RestaurantApi;
import com.startup.go4lunch.model.Restaurant;

import java.util.List;
import java.util.Objects;

public class RestaurantRepository {

    private MutableLiveData<List<Restaurant>> restaurantListLivedata;
    private final RestaurantApi restaurantApi;

    public RestaurantRepository(@NonNull RestaurantApi restaurantApi) {
        this.restaurantApi = restaurantApi;
    }

    @NonNull
    public LiveData<List<Restaurant>> getRestaurantListLiveData() {
        return restaurantApi.getRestaurantListLiveData();
    }

    public void updateLocationRestaurantList(@NonNull Location location) {
        restaurantApi.fetchLocationNearLocation(location);
    }

    @Nullable
    public Restaurant getRestaurantFromId(long id) {
        for (Restaurant restaurant: Objects.requireNonNull(restaurantListLivedata.getValue())) {
            if (restaurant.getId() == id) {
                return restaurant;
            }
        }
        return null;
    }
}