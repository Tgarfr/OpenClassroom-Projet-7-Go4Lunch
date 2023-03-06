package com.startup.go4lunch.di;

import com.startup.go4lunch.api.FakeRestaurantApi;
import com.startup.go4lunch.api.OverpassRestaurantApi;
import com.startup.go4lunch.repository.RestaurantRepository;

public class InjectionRestaurantRepository {

    private static RestaurantRepository INSTANCE;

    private static RestaurantRepository createRestaurantRepository() {
        return new RestaurantRepository(new OverpassRestaurantApi());
    }

    public static RestaurantRepository getRestaurantRepository() {
        if (INSTANCE == null) {
            synchronized (RestaurantRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = createRestaurantRepository();
                }
            }
        }
        return INSTANCE;
    }
}