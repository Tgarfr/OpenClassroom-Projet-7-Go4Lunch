package com.startup.go4lunch.ui;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.RestaurantListItem;
import com.startup.go4lunch.model.RestaurantWorkmateVote;
import com.startup.go4lunch.model.Workmate;
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
    private final MutableLiveData<List<RestaurantListItem>> restaurantListItemListLiveData = new MutableLiveData<>();
    private final Observer<List<Restaurant>> restaurantListObserver;
    private final Observer<Location> locationObserver;
    private final Observer<List<Workmate>> workmateListObserver;
    private final Observer<List<RestaurantWorkmateVote>> restaurantWorkmateVoteListObserver;
    private final Observer<String> searchObserver;
    int sortMethod = RestaurantListItem.SORT_BY_NAME;

    public RestaurantListFragmentViewModel(@NonNull RestaurantRepository restaurantRepository, @NonNull SearchRepository searchRepository, @NonNull LocationRepository locationRepository, @NonNull WorkmateRepository workmateRepository) {
        this.restaurantRepository = restaurantRepository;
        this.searchRepository = searchRepository;
        this.locationRepository = locationRepository;
        this.workmateRepository = workmateRepository;

        restaurantListObserver = this::getRestaurantListItem;
        locationObserver = this::updateDistance;
        workmateListObserver = this::updateNumberOfWorkmatesLunchInRestaurant;
        restaurantWorkmateVoteListObserver = this::updateRestaurantScore;
        searchObserver = string -> {
            if (string != null) {
                getRestaurantListItem(restaurantRepository.getRestaurantListResearchedFromString(string));
            } else {
                getRestaurantListItem(restaurantRepository.getRestaurantListLiveData().getValue());
            }
        };

        restaurantRepository.getRestaurantListLiveData().observeForever(restaurantListObserver);
        locationRepository.getLocationLiveData().observeForever(locationObserver);
        workmateRepository.getWorkmateListLiveData().observeForever(workmateListObserver);
        workmateRepository.getRestaurantWorkmateVoteListLiveData().observeForever(restaurantWorkmateVoteListObserver);
        searchRepository.getRestaurantListFragmentSearchLiveData().observeForever(searchObserver);
    }

    public LiveData<List<RestaurantListItem>> getRestaurantListItemListLiveData() {
        return restaurantListItemListLiveData;
    }

    public void sortRestaurantListItemListLiveData(int sortMethod) {
        this.sortMethod = sortMethod;
        List<RestaurantListItem> restaurantListItemList = restaurantListItemListLiveData.getValue();
        if (restaurantListItemList != null) {
            restaurantListItemListLiveData.setValue(getRestaurantListItemListSorted(sortMethod, new ArrayList<>(restaurantListItemList)));
        }
    }

    @Override
    protected void onCleared() {
        restaurantRepository.getRestaurantListLiveData().removeObserver(restaurantListObserver);
        locationRepository.getLocationLiveData().removeObserver(locationObserver);
        searchRepository.getMapFragmentSearchLivedata().removeObserver(searchObserver);
        workmateRepository.getWorkmateListLiveData().removeObserver(workmateListObserver);
        workmateRepository.getRestaurantWorkmateVoteListLiveData().removeObserver(restaurantWorkmateVoteListObserver);
        super.onCleared();
    }

    private void getRestaurantListItem(@Nullable List<Restaurant> restaurantList) {
        List<RestaurantListItem> restaurantListItemList = new ArrayList<>();
        if (restaurantList != null) {
            Location location = locationRepository.getLocationLiveData().getValue();
            List<Workmate> workmateList = workmateRepository.getWorkmateListLiveData().getValue();
            List<RestaurantWorkmateVote> restaurantWorkmateVoteList = workmateRepository.getRestaurantWorkmateVoteListLiveData().getValue();
            for (Restaurant restaurant : restaurantList) {
                int distance = getDistanceBetweenLocationAndRestaurantToInt(location, restaurant);
                int numberOfWorkmatesLunchToRestaurant = getNumberOfWorkmatesLunchInRestaurant(restaurant, workmateList);
                int score = getRestaurantScore(restaurant, restaurantWorkmateVoteList);
                restaurantListItemList.add(new RestaurantListItem(restaurant, distance, numberOfWorkmatesLunchToRestaurant, score));
            }
        }
        restaurantListItemListLiveData.setValue(getRestaurantListItemListSorted(this.sortMethod, restaurantListItemList));
    }

    private void updateDistance(@Nullable Location location) {
        if (location != null) {
            List<RestaurantListItem> restaurantListItemList = restaurantListItemListLiveData.getValue();
            if (restaurantListItemList != null) {
                List<RestaurantListItem> restaurantListItemListUpdated = new ArrayList<>();
                for (RestaurantListItem restaurantListItem : restaurantListItemList) {
                    restaurantListItemListUpdated.add(new RestaurantListItem(restaurantListItem.getRestaurant(),
                            getDistanceBetweenLocationAndRestaurantToInt(location, restaurantListItem.getRestaurant()),
                            restaurantListItem.getNumberOfWorkmate(),
                            restaurantListItem.getScore()));
                }
                if (sortMethod == RestaurantListItem.SORT_BY_DISTANCE) {
                    getRestaurantListItemListSorted(RestaurantListItem.SORT_BY_DISTANCE, restaurantListItemListUpdated);
                }
                restaurantListItemListLiveData.setValue(restaurantListItemListUpdated);
            }
        }
    }

    private void updateNumberOfWorkmatesLunchInRestaurant(@Nullable List<Workmate> workmateList) {
        if (workmateList != null) {
            List<RestaurantListItem> restaurantListItemList = restaurantListItemListLiveData.getValue();
            if (restaurantListItemList != null) {
                List<RestaurantListItem> restaurantListItemListUpdated = new ArrayList<>();
                for (RestaurantListItem restaurantListItem : restaurantListItemList) {
                    restaurantListItemListUpdated.add(new RestaurantListItem(restaurantListItem.getRestaurant(),
                            restaurantListItem.getDistance(),
                            getNumberOfWorkmatesLunchInRestaurant(restaurantListItem.getRestaurant(), workmateList),
                            restaurantListItem.getScore()));
                }
                restaurantListItemListLiveData.setValue(restaurantListItemListUpdated);
            }
        }
    }

    private void updateRestaurantScore(@Nullable List<RestaurantWorkmateVote> restaurantWorkmateVoteList) {
        if (restaurantWorkmateVoteList != null) {
            List<RestaurantListItem> restaurantListItemList = restaurantListItemListLiveData.getValue();
            if (restaurantListItemList != null) {
                List<RestaurantListItem> restaurantListItemListUpdated = new ArrayList<>();
                for (RestaurantListItem restaurantListItem : restaurantListItemList) {
                    restaurantListItemListUpdated.add(new RestaurantListItem(restaurantListItem.getRestaurant(),
                            restaurantListItem.getDistance(),
                            restaurantListItem.getNumberOfWorkmate(),
                            getRestaurantScore(restaurantListItem.getRestaurant(), restaurantWorkmateVoteList)));
                }
                if (sortMethod == RestaurantListItem.SORT_BY_SCORE) {
                    restaurantListItemListLiveData.setValue(getRestaurantListItemListSorted(RestaurantListItem.SORT_BY_SCORE, restaurantListItemListUpdated));
                } else {
                    restaurantListItemListLiveData.setValue(restaurantListItemListUpdated);
                }
            }
        }
    }

    private int getDistanceBetweenLocationAndRestaurantToInt(@Nullable Location location, @NonNull Restaurant restaurant) {
        if (location == null) {
            return 0;
        }
        Location restaurantLocation = new Location(restaurant.getName());
        restaurantLocation.setLatitude(restaurant.getLatitude());
        restaurantLocation.setLongitude(restaurant.getLongitude());
        return (int) location.distanceTo(restaurantLocation);
    }

    private int getNumberOfWorkmatesLunchInRestaurant(@NonNull Restaurant restaurant, @Nullable List<Workmate> workmateList) {
        if (workmateList == null) {
            return 0;
        }
        int numberOfWorkmate = 0;
        for (Workmate workmate : workmateList) {
            if (workmate.getRestaurantSelectedUid() != null && workmate.getRestaurantSelectedUid() == restaurant.getId()) {
                numberOfWorkmate++;
            }
        }
        return numberOfWorkmate;
    }

    private int getRestaurantScore(@NonNull Restaurant restaurant, @Nullable List<RestaurantWorkmateVote> restaurantWorkmateVoteList) {
        if (restaurantWorkmateVoteList == null) {
            return 0;
        }
        int score = 0;
        for (RestaurantWorkmateVote restaurantWorkmateVote : restaurantWorkmateVoteList) {
            if (restaurantWorkmateVote.getRestaurantUid() == restaurant.getId()) {
                score++;
            }
        }
        return score;
    }

    @NonNull
    private List<RestaurantListItem> getRestaurantListItemListSorted(int sortMethod, @NonNull List<RestaurantListItem> restaurantListItemList) {
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
            case RestaurantListItem.SORT_BY_SCORE:
                Collections.sort(restaurantListItemList, new RestaurantListItem.RestaurantListItemScoreComparator());
                break;
        }
        return restaurantListItemList;
    }
}