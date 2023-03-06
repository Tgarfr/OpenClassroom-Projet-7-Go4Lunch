package com.startup.go4lunch.api;

import androidx.lifecycle.LiveData;

import com.startup.go4lunch.model.Restaurant;

import java.util.List;

public interface RestaurantApi {

    LiveData<List<Restaurant>> getRestaurantListLiveData();
}