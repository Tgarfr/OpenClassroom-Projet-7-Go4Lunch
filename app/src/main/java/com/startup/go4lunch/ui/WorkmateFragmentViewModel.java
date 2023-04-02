package com.startup.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.Query;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.WorkmateRepository;

public class WorkmateFragmentViewModel extends ViewModel {

    private final WorkmateRepository workmateRepository;
    private final RestaurantRepository restaurantRepository;

    public WorkmateFragmentViewModel(@NonNull WorkmateRepository workmateRepository, RestaurantRepository restaurantRepository) {
        this.workmateRepository = workmateRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Query getWorkmateListQuery() {
        return workmateRepository.getWorkmateListQuery();
    }

    public RestaurantRepository getRestaurantRepository() {
        return restaurantRepository;
    }
}