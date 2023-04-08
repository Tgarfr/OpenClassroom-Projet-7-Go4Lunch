package com.startup.go4lunch.ui;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final LocationRepository locationRepository;

    public MainActivityViewModel(@NonNull RestaurantRepository restaurantRepository,@NonNull LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public void updateLocation(Location location) {
        if (location == null) {
            location = LocationRepository.getCompanyLocation();
        }
        locationRepository.setLocation(location);
        restaurantRepository.updateLocationRestaurantList(location);
    }

    @NonNull
    public List<Restaurant> searchRestaurantBy(@NonNull String text) {
        return restaurantRepository.getRestaurantListResearchedByText(text);
    }
}