package com.startup.go4lunch.api;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.startup.go4lunch.model.RestaurantWorkmateVote;
import com.startup.go4lunch.model.Workmate;

import java.util.List;

public interface WorkmateApi {

    @NonNull
    LiveData<List<Workmate>> getWorkmateListLiveData();

    void createWorkmate(@NonNull Workmate workmate);

    @NonNull
    LiveData<List<RestaurantWorkmateVote>> getRestaurantWorkmateVoteListLiveData();

    void createRestaurantWorkmateVote(@NonNull RestaurantWorkmateVote restaurantWorkmateVote);

    void removeRestaurantWorkmateVote(@NonNull RestaurantWorkmateVote restaurantWorkmateVote);
}