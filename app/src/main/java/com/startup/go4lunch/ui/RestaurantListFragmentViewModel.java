package com.startup.go4lunch.ui;

import android.location.Location;

import androidx.lifecycle.LiveData;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class RestaurantListFragmentViewModel extends androidx.lifecycle.ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final LocationRepository locationRepository;

    public RestaurantListFragmentViewModel(RestaurantRepository restaurantRepository, LocationRepository locationRepository) {
        this.restaurantRepository = restaurantRepository;
        this.locationRepository = locationRepository;
    }

    public LiveData<List<Restaurant>> getRestaurantListLiveData() {
        return restaurantRepository.getRestaurantListLiveData();
    }

    public LiveData<Location> getLocationLiveData() {
        return locationRepository.getLocationLiveData();
    }
}