package com.startup.go4lunch.di;

import com.startup.go4lunch.api.FakeRestaurantApi;
import com.startup.go4lunch.repository.RestaurantRepository;

public class Injection {

    public static RestaurantRepository createRestaurantRepository() {
        return RestaurantRepository.createInstance(new FakeRestaurantApi());
    }
}