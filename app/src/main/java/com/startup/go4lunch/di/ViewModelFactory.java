package com.startup.go4lunch.di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.WorkmateRepository;
import com.startup.go4lunch.ui.MainActivityViewModel;
import com.startup.go4lunch.ui.MapFragmentViewModel;
import com.startup.go4lunch.ui.RestaurantListFragmentViewModel;
import com.startup.go4lunch.ui.WorkmateFragmentViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;
    private final RestaurantRepository restaurantRepository;
    private final LocationRepository locationRepository;
    private final WorkmateRepository workmateRepository;

    private ViewModelFactory() {
        this.restaurantRepository = Injection.getRestaurantRepository();
        this.locationRepository = new LocationRepository();
        this.workmateRepository = new WorkmateRepository();
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
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(restaurantRepository,locationRepository,workmateRepository);
        }
        if (modelClass.isAssignableFrom(RestaurantListFragmentViewModel.class)) {
            return (T) new RestaurantListFragmentViewModel(restaurantRepository,locationRepository);
        }
        if (modelClass.isAssignableFrom(WorkmateFragmentViewModel.class)) {
            return (T) new WorkmateFragmentViewModel(workmateRepository, restaurantRepository);
        }
        if (modelClass.isAssignableFrom(MapFragmentViewModel.class)) {
            return (T) new MapFragmentViewModel(locationRepository,restaurantRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}