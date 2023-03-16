package com.startup.go4lunch.di;

import com.startup.go4lunch.api.OverpassRestaurantApi;
import com.startup.go4lunch.repository.RestaurantRepository;

public class Injection {

    private static RestaurantRepository restaurantRepository;

    private static RestaurantRepository createRestaurantRepository() {
        return new RestaurantRepository(new OverpassRestaurantApi());
    }

    public static RestaurantRepository getRestaurantRepository() {
        if (restaurantRepository == null) {
            synchronized (RestaurantRepository.class) {
                if (restaurantRepository == null) {
                    restaurantRepository = createRestaurantRepository();
                }
            }
        }
        return restaurantRepository;
    }
}