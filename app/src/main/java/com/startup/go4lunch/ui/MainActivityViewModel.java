package com.startup.go4lunch.ui;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.startup.go4lunch.model.Restaurant;
import com.google.firebase.auth.FirebaseUser;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.WorkmateRepository;

import java.util.List;

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

    @NonNull
    public List<Restaurant> searchRestaurantBy(@NonNull String text) {
        return restaurantRepository.getRestaurantListResearchedByText(text);
    }

    public void createWorkmate(FirebaseUser firebaseUser) {
        String name = firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "Name";
        String urlAvatar = firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null;
        Workmate workmate = new Workmate(firebaseUser.getUid(), name,urlAvatar, 0);
        workmateRepository.createWorkmate(workmate);
    }
}