package com.startup.go4lunch.api;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.startup.go4lunch.model.Restaurant;

import java.util.List;

public interface RestaurantApi {

    @NonNull
    MutableLiveData<List<Restaurant>> getRestaurantListLiveData();

    void fetchLocationNearLocation(@NonNull Location location);
}