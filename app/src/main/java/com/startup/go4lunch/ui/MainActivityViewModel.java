package com.startup.go4lunch.ui;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.SearchRepository;

public class MainActivityViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final LocationRepository locationRepository;
    private final SearchRepository searchRepository;

    public MainActivityViewModel(@NonNull RestaurantRepository restaurantRepository, @NonNull LocationRepository locationRepository, @NonNull SearchRepository searchRepository) {
        this.locationRepository = locationRepository;
        this.restaurantRepository = restaurantRepository;
        this.searchRepository = searchRepository;
    }

    public void updateLocation(Location location) {
        if (location == null) {
            location = LocationRepository.getCompanyLocation();
        }
        locationRepository.setLocation(location);
        restaurantRepository.updateLocationRestaurantList(location);
    }

    public void setMapFragmentSearch(@NonNull String searchString) {
        searchRepository.setMapFragmentSearch(searchString);
    }

    public void setRestaurantListSearch(@NonNull String searchString) {
        searchRepository.setRestaurantListSearch(searchString);
    }
}