package com.startup.go4lunch.repository;

import androidx.lifecycle.LiveData;

import com.startup.go4lunch.api.RestaurantApi;
import com.startup.go4lunch.model.Restaurant;

import java.util.List;

public class RestaurantRepository {

    private LiveData<List<Restaurant>> restaurantList;

    public RestaurantRepository(RestaurantApi apiService) {
        this.restaurantList = apiService.getRestaurantListLiveData();
    }

    public LiveData<List<Restaurant>> getRestaurantListLiveData() {
        return restaurantList;
    }
}
