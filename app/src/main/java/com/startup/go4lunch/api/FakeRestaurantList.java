package com.startup.go4lunch.api;

import com.startup.go4lunch.model.Restaurant;

import java.util.Arrays;
import java.util.List;

public class FakeRestaurantList {

    static List<Restaurant> getFakeRestaurantList() {
        return FAKE_RESTAURANTS_LIST;
    }

    private static List<Restaurant> FAKE_RESTAURANTS_LIST =Arrays.asList(
            new Restaurant(0L,"Restaurant 0","1 chemin de la Paix, 75002 Paris", "0h00"),
            new Restaurant(1L,"Restaurant 1","1 rue de la Paix, 75002 Paris", "1h00"),
            new Restaurant(2L,"Restaurant 2","2 rue de la Paix, 75002 Paris", "2h00"),
            new Restaurant(3L,"Restaurant 3","3 rue de la Paix, 75002 Paris", "3h00"),
            new Restaurant(4L,"Restaurant 4","4 rue de la Paix, 75002 Paris", "4h00"),
            new Restaurant(5L,"Restaurant 5","5 rue de la Paix, 75002 Paris", "5h00")
            );
}
