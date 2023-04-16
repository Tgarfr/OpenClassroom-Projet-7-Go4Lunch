package com.startup.go4lunch.repository;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.startup.go4lunch.api.RestaurantApi;
import com.startup.go4lunch.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantRepository {

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

    @NonNull
    public List<Restaurant> getRestaurantListResearchedByString(@NonNull String string) {
        List<Restaurant> restaurantList = restaurantApi.getRestaurantListLiveData().getValue();
        List<Restaurant> restaurantListResearched = new ArrayList<>();
        if (restaurantList != null) {
            for (Restaurant restaurant: restaurantList) {
                if (restaurant.getName().toLowerCase().contains(string.toLowerCase())) {
                    restaurantListResearched.add(restaurant);
                }
            }
        }
        return restaurantListResearched;
    }

    @Nullable
    public Restaurant getRestaurantResearchedByString(@NonNull String string) {
        List<Restaurant> restaurantList = restaurantApi.getRestaurantListLiveData().getValue();
        if (restaurantList != null) {
            for (Restaurant restaurant: restaurantList) {
                if (restaurant.getName().toLowerCase().contains(string.toLowerCase())) {
                    return restaurant;
                }
            }
        }
        return null;
    }
}