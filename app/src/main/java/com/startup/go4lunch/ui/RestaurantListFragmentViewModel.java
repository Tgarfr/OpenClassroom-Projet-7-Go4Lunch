package com.startup.go4lunch.ui;

import android.location.Location;

import androidx.lifecycle.LiveData;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.RestaurantListItem;
import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestaurantListFragmentViewModel extends androidx.lifecycle.ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final LocationRepository locationRepository;
    private final List<RestaurantListItem> restaurantListItem;
    int sortMethod = 0;

    public RestaurantListFragmentViewModel(RestaurantRepository restaurantRepository, LocationRepository locationRepository) {
        this.restaurantRepository = restaurantRepository;
        this.locationRepository = locationRepository;
        restaurantListItem = new ArrayList<>();
    }

    public LiveData<List<Restaurant>> getRestaurantListLiveData() {
        return restaurantRepository.getRestaurantListLiveData();
    }

    public List<RestaurantListItem> getItemRestaurantList() {
        List<Restaurant> restaurantList = restaurantRepository.getRestaurantListLiveData().getValue();
        Location location = locationRepository.getLocationLiveData().getValue();
        if (restaurantList != null) {
            for (Restaurant restaurant : restaurantList) {
                Location restaurantLocation = new Location(restaurant.getName());
                restaurantLocation.setLatitude(restaurant.getLatitude());
                restaurantLocation.setLongitude(restaurant.getLongitude());
                int distance = 0;
                if (location != null) {
                    distance = (int) location.distanceTo(restaurantLocation);
                }
                short numberOfWorkmate = 0; // TODO
                float score = 0; // TODO
                restaurantListItem.add(new RestaurantListItem(restaurant, distance, numberOfWorkmate, score));
            }
        }
        sortList(this.sortMethod);
        return restaurantListItem;
    }

    public void sortList(int sortMethod) {
        switch (sortMethod) {
            case 0:
                Collections.sort(restaurantListItem, new RestaurantListItem.RestaurantListItemNameComparator());
                break;
            case 1:
                Collections.sort(restaurantListItem, new RestaurantListItem.RestaurantListItemLocationComparator());
                break;
            case 2:
                Collections.sort(restaurantListItem, new RestaurantListItem.RestaurantListItemTypeComparator());
                break;
            case 3:
                Collections.sort(restaurantListItem, new RestaurantListItem.RestaurantListItemRateComparator());
                break;
        }
    }
}