package com.startup.go4lunch.repository;

import com.startup.go4lunch.api.RestaurantApi;
import com.startup.go4lunch.model.Restaurant;

import java.util.List;

public class RestaurantRepository {

    private static RestaurantRepository INSTANCE;
    private List<Restaurant> restaurantList;

    private RestaurantRepository(RestaurantApi apiService) {
        this.restaurantList = apiService.getRestaurantList();
    }

    public static RestaurantRepository createInstance(RestaurantApi apiService) {
        if (INSTANCE == null) {
            synchronized (RestaurantRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RestaurantRepository(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static RestaurantRepository getInstance() {
        return INSTANCE;
    }

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurantList.add(restaurant);
    }

    public void deleteRestaurant(Restaurant restaurant) {
        restaurantList.remove(restaurant);
    }

    public int countRestaurant() {
        return restaurantList.size();
    }
}
