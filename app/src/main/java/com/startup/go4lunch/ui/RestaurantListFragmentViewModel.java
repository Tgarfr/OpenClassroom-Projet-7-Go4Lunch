package com.startup.go4lunch.ui;

import androidx.lifecycle.LiveData;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.repository.RestaurantRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class RestaurantListFragmentViewModel extends androidx.lifecycle.ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final Executor executor;

    public RestaurantListFragmentViewModel(RestaurantRepository restaurantRepository, Executor executor) {
        this.restaurantRepository = restaurantRepository;
        this.executor = executor;
    }

    public LiveData<List<Restaurant>> getRestaurantListLiveData() {
        return restaurantRepository.getRestaurantListLiveData();
    }
}