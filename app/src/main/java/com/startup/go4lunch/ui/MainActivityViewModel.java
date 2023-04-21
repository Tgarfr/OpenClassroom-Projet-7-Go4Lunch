package com.startup.go4lunch.ui;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.SearchRepository;
import com.startup.go4lunch.repository.WorkmateRepository;

public class MainActivityViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final LocationRepository locationRepository;
    private final SearchRepository searchRepository;
    private final WorkmateRepository workmateRepository;

    public MainActivityViewModel(@NonNull RestaurantRepository restaurantRepository, @NonNull LocationRepository locationRepository, @NonNull WorkmateRepository workmateRepository, @NonNull SearchRepository searchRepository) {
        this.locationRepository = locationRepository;
        this.restaurantRepository = restaurantRepository;
        this.searchRepository = searchRepository;
        this.workmateRepository = workmateRepository;
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

    public void createWorkmate(FirebaseUser firebaseUser) {
        String name = firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "Name";
        String urlAvatar = firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null;
        Workmate workmate = new Workmate(firebaseUser.getUid(), name,urlAvatar, 0);
        workmateRepository.createWorkmate(workmate);
    }
}