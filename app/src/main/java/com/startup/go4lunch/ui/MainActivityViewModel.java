package com.startup.go4lunch.ui;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
 import com.startup.go4lunch.repository.WorkmateRepository;

public class MainActivityViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final LocationRepository locationRepository;
    private final WorkmateRepository workmateRepository;

    public MainActivityViewModel(@NonNull RestaurantRepository restaurantRepository, @NonNull LocationRepository locationRepository, @NonNull WorkmateRepository workmateRepository) {
        this.locationRepository = locationRepository;
        this.restaurantRepository = restaurantRepository;
        this.workmateRepository = workmateRepository;
    }

    public void updateLocation(Location location) {
        if (location == null) {
            location = LocationRepository.getCompanyLocation();
        }
        locationRepository.setLocation(location);
        restaurantRepository.updateLocationRestaurantList(location);
    }

    public void createWorkmate() {
        workmateRepository.createWorkmate();
    }
}