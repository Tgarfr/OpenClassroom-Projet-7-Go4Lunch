package com.startup.go4lunch.di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.SearchRepository;
import com.startup.go4lunch.ui.MainActivityViewModel;
import com.startup.go4lunch.ui.MapFragmentViewModel;
import com.startup.go4lunch.ui.RestaurantListFragmentViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;
    private final RestaurantRepository restaurantRepository;
    private final LocationRepository locationRepository;
    private final SearchRepository searchRepository;

    private ViewModelFactory() {
        this.restaurantRepository = Injection.getRestaurantRepository();
        this.locationRepository = new LocationRepository();
        this.searchRepository = new SearchRepository();
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
    public <T extends androidx.lifecycle.ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantListFragmentViewModel.class)) {
            return (T) new RestaurantListFragmentViewModel(restaurantRepository,searchRepository);
        }
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(restaurantRepository,locationRepository,searchRepository);
        }
        if (modelClass.isAssignableFrom(MapFragmentViewModel.class)) {
            return (T) new MapFragmentViewModel(locationRepository,restaurantRepository,searchRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}