package com.startup.go4lunch.ui;

import androidx.lifecycle.ViewModel;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.repository.RestaurantRepository;

public class RestaurantDetailActivityViewModel extends ViewModel {

    RestaurantRepository restaurantRepository;

    public RestaurantDetailActivityViewModel(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant getRestaurantFromId(Long idRestaurant) {
        return restaurantRepository.getRestaurantFromId(idRestaurant);
    }
}