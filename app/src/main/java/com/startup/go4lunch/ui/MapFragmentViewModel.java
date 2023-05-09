package com.startup.go4lunch.ui;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.RestaurantMapMarker;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.SearchRepository;
import com.startup.go4lunch.repository.WorkmateRepository;

import java.util.ArrayList;
import java.util.List;

public class MapFragmentViewModel extends ViewModel {

    private final LocationRepository locationRepository;
    private final RestaurantRepository restaurantRepository;
    private final SearchRepository searchRepository;
    private final WorkmateRepository workmateRepository;
    private final MutableLiveData<List<RestaurantMapMarker>> restaurantMapMarkerListLiveData = new MutableLiveData<>();

    public MapFragmentViewModel(@NonNull LocationRepository locationRepository, @NonNull RestaurantRepository restaurantRepository, @NonNull SearchRepository searchRepository,@NonNull WorkmateRepository workmateRepository) {
        this.locationRepository = locationRepository;
        this.restaurantRepository = restaurantRepository;
        this.searchRepository = searchRepository;
        this.workmateRepository = workmateRepository;
        getRestaurantMapMarkerList();
    }

    public void setLiveDataObserver(LifecycleOwner lifecycleOwner) {
        restaurantRepository.getRestaurantListLiveData().observe(lifecycleOwner, restaurants -> getRestaurantMapMarkerList());
        workmateRepository.getWorkmateListLiveData().observe(lifecycleOwner, workmateList -> getRestaurantMapMarkerList() );
    }

    @NonNull
    public LiveData<Location> getLocationLiveData() {
        return locationRepository.getLocationLiveData();
    }

    @NonNull
    public LiveData<List<RestaurantMapMarker>> getRestaurantMapMarkerListLiveData() {
        return restaurantMapMarkerListLiveData;
    }

    @NonNull
    public LiveData<String> getSearchStringLivedata() {
        return searchRepository.getMapFragmentSearchLivedata();
    }

    @Nullable
    public Restaurant getRestaurantFromString(@NonNull String string) {
        return restaurantRepository.getRestaurantResearchedFromString(string);
    }

    public void setEndSearch() {
        searchRepository.setMapFragmentSearch(null);
    }

    private void getRestaurantMapMarkerList() {
        List<Restaurant> restaurantList = restaurantRepository.getRestaurantListLiveData().getValue();
        List<RestaurantMapMarker> restaurantMapMarkerList = new ArrayList<>();
        if (restaurantList != null) {
            for (Restaurant restaurant : restaurantList) {
                restaurantMapMarkerList.add(new RestaurantMapMarker(restaurant, workmateLunchOnRestaurant(restaurant.getId())));
            }
        }
        restaurantMapMarkerListLiveData.setValue(restaurantMapMarkerList);
    }

    private boolean workmateLunchOnRestaurant(long restaurantUid) {
        List<Workmate> workmateList = workmateRepository.getWorkmateListLiveData().getValue();
        if (workmateList != null) {
            for (Workmate workmate : workmateList) {
                if (workmate.getRestaurantSelectedUid() != null) {
                    if (workmate.getRestaurantSelectedUid() == restaurantUid) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
 }