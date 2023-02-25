package com.startup.go4lunch.api;

import com.startup.go4lunch.model.Restaurant;

import java.util.List;

public interface RestaurantApi {

    List<Restaurant> getRestaurantList();
}