package com.startup.go4lunch.di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.ui.RestaurantListFragmentViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;
    private final RestaurantRepository restaurantRepository;
    private final Executor executor;

    private ViewModelFactory() {
        this.restaurantRepository = InjectionRestaurantRepository.getRestaurantRepository();
        this.executor = Executors.newSingleThreadExecutor();
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
            return (T) new RestaurantListFragmentViewModel(restaurantRepository, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}