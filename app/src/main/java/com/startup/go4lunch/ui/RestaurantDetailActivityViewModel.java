package com.startup.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.model.WorkmateListItem;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.WorkmateRepository;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailActivityViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final WorkmateRepository workmateRepository;

    public RestaurantDetailActivityViewModel(@NonNull RestaurantRepository restaurantRepository,@NonNull WorkmateRepository workmateRepository) {
        this.restaurantRepository = restaurantRepository;
        this.workmateRepository = workmateRepository;
    }

    @NonNull
    public LiveData<Restaurant> getRestaurantLiveData(long idRestaurant) {
        return Transformations.map(restaurantRepository.getRestaurantListLiveData(),
                input -> restaurantRepository.getRestaurantFromId(idRestaurant) );
    }

    @NonNull
    public LiveData<Workmate> getUserWorkmateLiveData(String workmateUid) {
        return Transformations.map(workmateRepository.getWorkmateListLiveData(),
                input -> workmateRepository.getWorkmateFromUid(workmateUid));
    }

    @NonNull
    public LiveData<List<WorkmateListItem>> getWorkmateListItemLiveData(long restaurantUid) {
        return Transformations.map(workmateRepository.getWorkmateListLiveData(), input -> {
                    List<Workmate> workmateList = workmateRepository.getWorkmateListFromRestaurant(restaurantUid);
                    List<WorkmateListItem> workmateListItemList = new ArrayList<>();
                    for (Workmate workmate : workmateList) {
                        workmateListItemList.add(new WorkmateListItem(workmate, null, WorkmateListItem.DISPLAY_TEXT_JOINING));
                    }
                    return workmateListItemList;
                });
    }

    @NonNull
    public LiveData<Boolean> getRestaurantWorkmateVoteLiveData(@NonNull String workmateUid, long restaurantUid) {
        return Transformations.map(workmateRepository.getRestaurantWorkmateVoteListLiveData(), input ->
                    workmateRepository.getRestaurantWorkmateVote(workmateUid, restaurantUid) );
    }

    public void setRestaurantWorkmateVote(@NonNull String workmateUid, long restaurantUid) {
        workmateRepository.createRestaurantWorkmateVote(workmateUid, restaurantUid);
    }

    public void removeRestaurantWorkmateVote(@NonNull String workmateUid, long restaurantUid) {
        workmateRepository.removeRestaurantWorkmateVote(workmateUid, restaurantUid);
    }

    public void setRestaurantSelected(@NonNull String workmateUid,@Nullable Long restaurantId) {
        workmateRepository.setRestaurantSelectedToWorkmate(workmateUid, restaurantId);
    }
}