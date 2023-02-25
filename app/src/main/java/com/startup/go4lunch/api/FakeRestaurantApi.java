package com.startup.go4lunch.api;

import com.startup.go4lunch.model.Restaurant;

import java.util.List;

public class FakeRestaurantApi implements RestaurantApi {

    private List<Restaurant> restaurantList;

    public FakeRestaurantApi() {
        restaurantList = FakeRestaurantList.getFakeRestaurantList();
    }

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }
}