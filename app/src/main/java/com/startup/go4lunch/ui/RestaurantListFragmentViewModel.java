package com.startup.go4lunch.ui;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.RestaurantListItem;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.model.RestaurantWorkmateVote;
import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.SearchRepository;
import com.startup.go4lunch.repository.WorkmateRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestaurantListFragmentViewModel extends androidx.lifecycle.ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final SearchRepository searchRepository;
    private final LocationRepository locationRepository;
    private final WorkmateRepository workmateRepository;
    private final MutableLiveData<List<RestaurantListItem>> restaurantListItemLiveData = new MutableLiveData<>();
    int sortMethod = RestaurantListItem.SORT_BY_NAME;

    public RestaurantListFragmentViewModel(@NonNull RestaurantRepository restaurantRepository, @NonNull SearchRepository searchRepository, @NonNull LocationRepository locationRepository, @NonNull WorkmateRepository workmateRepository) {
        this.restaurantRepository = restaurantRepository;
        this.searchRepository = searchRepository;
        this.locationRepository = locationRepository;
        this.workmateRepository = workmateRepository;
        getRestaurantListItem();
    }

    public void setLiveDataObserver(LifecycleOwner lifecycleOwner) {
        restaurantRepository.getRestaurantListLiveData().observe(lifecycleOwner, restaurantList -> getRestaurantListItem());
        searchRepository.getRestaurantListFragmentSearchLiveData().observe(lifecycleOwner, string -> getRestaurantListItem());
        workmateRepository.getWorkmateListLiveData().observe(lifecycleOwner, workmateList -> getRestaurantListItem());
        workmateRepository.getRestaurantWorkmateVoteListLiveData().observe(lifecycleOwner, restaurantWorkmateVotes -> getRestaurantListItem());
    }

    public LiveData<List<RestaurantListItem>> getRestaurantListItemLiveData() {
        return restaurantListItemLiveData;
    }

    public void getRestaurantListItem() {
        List<Restaurant> restaurantList = restaurantRepository.getRestaurantListResearchedFromString(searchRepository.getRestaurantListFragmentSearchLiveData().getValue());
        Location location = locationRepository.getLocationLiveData().getValue();
        List<RestaurantListItem> restaurantListItemList = new ArrayList<>();
        for (Restaurant restaurant : restaurantList) {
            Location restaurantLocation = new Location(restaurant.getName());
            restaurantLocation.setLatitude(restaurant.getLatitude());
            restaurantLocation.setLongitude(restaurant.getLongitude());
            int distance = 0;
            if (location != null) {
                distance = (int) location.distanceTo(restaurantLocation);
            }
            int numberOfWorkmate = getNumberOfWorkmate(restaurant.getId());
            int score = getRestaurantScore(restaurant.getId());
            restaurantListItemList.add(new RestaurantListItem(restaurant, distance, numberOfWorkmate, score));
        }
        restaurantListItemLiveData.setValue(restaurantListItemList);
        sortRestaurantListItemListLiveData(this.sortMethod);
    }

    public void sortRestaurantListItemListLiveData(int sortMethod) {
        List<RestaurantListItem> restaurantListItemList = restaurantListItemLiveData.getValue();
        if (restaurantListItemList != null) {
            switch (sortMethod) {
                case RestaurantListItem.SORT_BY_NAME:
                    Collections.sort(restaurantListItemList, new RestaurantListItem.RestaurantListItemNameComparator());
                    break;
                case RestaurantListItem.SORT_BY_DISTANCE:
                    Collections.sort(restaurantListItemList, new RestaurantListItem.RestaurantListItemDistanceComparator());
                    break;
                case RestaurantListItem.SORT_BY_TYPE:
                    Collections.sort(restaurantListItemList, new RestaurantListItem.RestaurantListItemTypeComparator());
                    break;
                case RestaurantListItem.SORT_BY_NOTE:
                    Collections.sort(restaurantListItemList, new RestaurantListItem.RestaurantListItemRateComparator());
                    break;
            }
            restaurantListItemLiveData.setValue(restaurantListItemList);
        }
    }

    private int getRestaurantScore(long restaurantUid) {
        List<RestaurantWorkmateVote> restaurantWorkmateVoteList = workmateRepository.getRestaurantWorkmateVoteListLiveData().getValue();
        int score = 0;
        if (restaurantWorkmateVoteList != null) {
            for ( RestaurantWorkmateVote restaurantWorkmateVote : restaurantWorkmateVoteList) {
                if (restaurantWorkmateVote.getRestaurantUid() == restaurantUid) {
                    score++;
                }
            }
        }
        return score;
    }

    private int getNumberOfWorkmate(long restaurantUid) {
        List<Workmate> workmateList = workmateRepository.getWorkmateListLiveData().getValue();
        int numberOfWorkmate = 0;
        if (workmateList != null) {
            for (Workmate workmate : workmateList) {
                if (workmate.getRestaurantSelectedUid() != null) {
                    if (workmate.getRestaurantSelectedUid() == restaurantUid) {
                        numberOfWorkmate++;
                    }
                }
            }
        }
        return numberOfWorkmate;
    }
}