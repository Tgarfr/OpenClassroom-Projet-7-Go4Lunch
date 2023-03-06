package com.startup.go4lunch.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.startup.go4lunch.api.utilApi.FakeRestaurantList;
import com.startup.go4lunch.model.Restaurant;

import java.util.List;

public class FakeRestaurantApi implements RestaurantApi {

    private final MutableLiveData<List<Restaurant>> restaurantListLiveData = new MutableLiveData<>();

    public FakeRestaurantApi() {
        restaurantListLiveData.setValue(FakeRestaurantList.getFakeRestaurantListLiveData());
    }

    public LiveData<List<Restaurant>> getRestaurantListLiveData() {
        return restaurantListLiveData;
    }
}