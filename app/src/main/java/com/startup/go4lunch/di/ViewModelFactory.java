package com.startup.go4lunch.di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.SearchRepository;
import com.startup.go4lunch.repository.WorkmateRepository;
import com.startup.go4lunch.ui.MainActivityViewModel;
import com.startup.go4lunch.ui.MapFragmentViewModel;
import com.startup.go4lunch.ui.RestaurantDetailActivityViewModel;
import com.startup.go4lunch.ui.RestaurantListFragmentViewModel;
import com.startup.go4lunch.ui.WorkmateListFragmentViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static volatile ViewModelFactory factory;
    private final RestaurantRepository restaurantRepository;
    private final LocationRepository locationRepository;
    private final SearchRepository searchRepository;
    private final WorkmateRepository workmateRepository;

    private ViewModelFactory() {
        this.restaurantRepository = Injection.getRestaurantRepository();
        this.locationRepository = new LocationRepository();
        this.searchRepository = new SearchRepository();
        this.workmateRepository = Injection.getWorkmateRepository();
    }

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory();
                }
            }
        }
        return factory;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends androidx.lifecycle.ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(restaurantRepository,locationRepository,workmateRepository,searchRepository);
        }
        if (modelClass.isAssignableFrom(MapFragmentViewModel.class)) {
            return (T) new MapFragmentViewModel(locationRepository,restaurantRepository,searchRepository);
        }
        if (modelClass.isAssignableFrom(RestaurantListFragmentViewModel.class)) {
            return (T) new RestaurantListFragmentViewModel(restaurantRepository, searchRepository, locationRepository);
        }
        if (modelClass.isAssignableFrom(WorkmateListFragmentViewModel.class)) {
            return (T) new WorkmateListFragmentViewModel(workmateRepository, restaurantRepository, searchRepository);
        }
        if (modelClass.isAssignableFrom(RestaurantDetailActivityViewModel.class)) {
            return (T) new RestaurantDetailActivityViewModel(restaurantRepository, workmateRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}