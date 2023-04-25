package com.startup.go4lunch.ui;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.RestaurantListItem;
import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.SearchRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestaurantListFragmentViewModel extends androidx.lifecycle.ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final SearchRepository searchRepository;
    private final LocationRepository locationRepository;
    private List<RestaurantListItem> restaurantListItem;
    int sortMethod = RestaurantListItem.SORT_BY_NAME;

    public RestaurantListFragmentViewModel(@NonNull RestaurantRepository restaurantRepository, @NonNull SearchRepository searchRepository, @NonNull LocationRepository locationRepository) {
        this.restaurantRepository = restaurantRepository;
        this.searchRepository = searchRepository;
        this.locationRepository = locationRepository;
        this.restaurantListItem = new ArrayList<>();
    }

    @NonNull
    public LiveData<List<Restaurant>> getRestaurantListLiveData() {
        return restaurantRepository.getRestaurantListLiveData();
    }

    @NonNull
    public LiveData<String> getRestaurantListSearchString() {
        return searchRepository.getRestaurantListFragmentSearchLiveData();
    }

    public List<RestaurantListItem> getListItemRestaurant() {
        List<Restaurant> restaurantList = restaurantRepository.getRestaurantListResearchedByString(searchRepository.getRestaurantListFragmentSearchLiveData().getValue());
        Location location = locationRepository.getLocationLiveData().getValue();
        restaurantListItem = new ArrayList<>();
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
        sortList(this.sortMethod);
        return restaurantListItem;
    }

    public void sortList(int sortMethod) {
        switch (sortMethod) {
            case RestaurantListItem.SORT_BY_NAME:
                Collections.sort(restaurantListItem, new RestaurantListItem.RestaurantListItemNameComparator());
                break;
            case RestaurantListItem.SORT_BY_DISTANCE:
                Collections.sort(restaurantListItem, new RestaurantListItem.RestaurantListItemDistanceComparator());
                break;
            case RestaurantListItem.SORT_BY_TYPE:
                Collections.sort(restaurantListItem, new RestaurantListItem.RestaurantListItemTypeComparator());
                break;
            case RestaurantListItem.SORT_BY_NOTE:
                Collections.sort(restaurantListItem, new RestaurantListItem.RestaurantListItemRateComparator());
                break;
        }
    }
}