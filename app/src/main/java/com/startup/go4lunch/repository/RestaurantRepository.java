package com.startup.go4lunch.repository;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.startup.go4lunch.api.RestaurantApi;
import com.startup.go4lunch.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantRepository {

    private final RestaurantApi restaurantApi;
    private final MutableLiveData<List<Restaurant>> restaurantListLivedata;

    public RestaurantRepository(@NonNull RestaurantApi restaurantApi) {
        this.restaurantApi = restaurantApi;
        restaurantListLivedata = restaurantApi.getRestaurantListLiveData();
    }

    @NonNull
    public LiveData<List<Restaurant>> getRestaurantListLiveData() {
        return restaurantListLivedata;
    }

    public void updateLocationRestaurantList(@NonNull Location location) {
        restaurantApi.fetchLocationNearLocation(location);
    }

    public List<Restaurant> getRestaurantListResearchedByText(String text) {
        List<Restaurant> restaurantList = restaurantApi.getRestaurantListLiveData().getValue();
        List<Restaurant> restaurantListResearched = new ArrayList<>();
        if (restaurantList != null) {
            for (Restaurant restaurant: restaurantList) {
                if (restaurant.getName().contains(text)) {
                    restaurantListResearched.add(restaurant);
                }
            }
        }
        return restaurantListResearched;
    }

    @Nullable
    public Restaurant getRestaurantFromId(long id) {
        List<Restaurant> restaurantList = restaurantListLivedata.getValue();
        if (restaurantList != null) {
            for (Restaurant restaurant: restaurantList) {
                if (restaurant.getId() == id) {
                    return restaurant;
                }
            }
        }
        return null;
    }
}