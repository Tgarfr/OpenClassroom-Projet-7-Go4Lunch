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

    @NonNull
    public List<Restaurant> getRestaurantListResearchedFromString(@Nullable String string) {
        List<Restaurant> restaurantList = restaurantApi.getRestaurantListLiveData().getValue();
        if (restaurantList != null) {
            if (string == null) {
                return restaurantList;
            }
            List<Restaurant> restaurantListResearched = new ArrayList<>();
            for (Restaurant restaurant: restaurantList) {
                if (restaurant.getName().toLowerCase().contains(string.toLowerCase())) {
                    restaurantListResearched.add(restaurant);
                }
            }
            return restaurantListResearched;
        }
        return new ArrayList<>();
    }

    @Nullable
    public Restaurant getRestaurantResearchedFromString(@NonNull String string) {
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