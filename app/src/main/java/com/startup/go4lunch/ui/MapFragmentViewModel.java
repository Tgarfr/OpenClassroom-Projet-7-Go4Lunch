package com.startup.go4lunch.ui;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.SearchRepository;

import java.util.List;

public class MapFragmentViewModel extends ViewModel {

    private final LocationRepository locationRepository;
    private final RestaurantRepository restaurantRepository;
    private final SearchRepository searchRepository;

    public MapFragmentViewModel(@NonNull LocationRepository locationRepository, @NonNull RestaurantRepository restaurantRepository, @NonNull SearchRepository searchRepository) {
        this.locationRepository = locationRepository;
        this.restaurantRepository = restaurantRepository;
        this.searchRepository = searchRepository;
    }

    @NonNull
    public LiveData<Location> getLocationLiveData() {
        return locationRepository.getLocationLiveData();
    }

    @NonNull
    public LiveData<List<Restaurant>> getRestaurantListLiveData() {
        return restaurantRepository.getRestaurantListLiveData();
    }

    @NonNull
    public LiveData<String> getSearchStringLivedata() {
        return searchRepository.getMapFragmentSearchLivedata();
    }

    @Nullable
    public Restaurant getRestaurantByString(@NonNull String string) {
        return restaurantRepository.getRestaurantResearchedByString(string);
    }

    public void setEndSearch() {
        searchRepository.setMapFragmentSearch(null);
    }
 }