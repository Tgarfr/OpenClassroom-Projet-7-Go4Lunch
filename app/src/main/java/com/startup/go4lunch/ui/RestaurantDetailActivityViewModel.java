package com.startup.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.RestaurantWorkmateVote;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.WorkmateRepository;

import java.util.List;

public class RestaurantDetailActivityViewModel extends ViewModel {

    RestaurantRepository restaurantRepository;
    WorkmateRepository workmateRepository;

    public RestaurantDetailActivityViewModel(@NonNull RestaurantRepository restaurantRepository,@NonNull WorkmateRepository workmateRepository) {
        this.restaurantRepository = restaurantRepository;
        this.workmateRepository = workmateRepository;
    }

    public Restaurant getRestaurantFromId(Long idRestaurant) {
        return restaurantRepository.getRestaurantFromId(idRestaurant);
    }

    @Nullable
    public Workmate getWorkmateFromUid(@NonNull String uid) {
        return workmateRepository.getWorkmateFromUid(uid);
    }

    public void setRestaurantWorkmateVote(@NonNull String workmateUid, long restaurantUid) {
        workmateRepository.createRestaurantWorkmateVote(workmateUid, restaurantUid);
    }

    public void removeRestaurantWorkmateVote(@NonNull String workmateUid, long restaurantUid) {
        workmateRepository.removeRestaurantWorkmateVote(workmateUid, restaurantUid);
    }


    public boolean getRestaurantWorkmateVote(@NonNull String workmateUid, long restaurantUid) {
        return workmateRepository.getRestaurantWorkmateVote(workmateUid, restaurantUid);
    }

    @NonNull
    public LiveData<List<RestaurantWorkmateVote>> getRestaurantWorkmateVoteLiveData() {
        return workmateRepository.getRestaurantWorkmateVoteListLiveData();
    }
}