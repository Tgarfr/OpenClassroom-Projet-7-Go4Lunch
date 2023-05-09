package com.startup.go4lunch.model;

import androidx.annotation.NonNull;

public class RestaurantMapMarker {

    private final Restaurant restaurant;
    private final boolean workmateLunchOnRestaurant;

    public RestaurantMapMarker(@NonNull Restaurant restaurant, boolean workmateLunchInRestaurant) {
         this.restaurant = restaurant;
         this.workmateLunchOnRestaurant = workmateLunchInRestaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public boolean getWorkmateLunchOnRestaurant() {
        return workmateLunchOnRestaurant;
    }
}