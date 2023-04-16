package com.startup.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.SearchRepository;

import java.util.List;

public class RestaurantListFragmentViewModel extends androidx.lifecycle.ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final SearchRepository searchRepository;

    public RestaurantListFragmentViewModel(@NonNull RestaurantRepository restaurantRepository,@NonNull SearchRepository searchRepository) {
        this.restaurantRepository = restaurantRepository;
        this.searchRepository = searchRepository;
    }

    @NonNull
    public LiveData<List<Restaurant>> getRestaurantListLiveData() {
        return restaurantRepository.getRestaurantListLiveData();
    }

    @NonNull
    public LiveData<String> getRestaurantListSearchString() {
        return searchRepository.getRestaurantListFragmentSearchLiveData();
    }

    @NonNull
    public List<Restaurant> getRestaurantSearchList(@NonNull String searchString) {
        return restaurantRepository.getRestaurantListResearchedByString(searchString);
    }
}