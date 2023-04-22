package com.startup.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.RestaurantWorkmateVote;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.model.WorkmateListItem;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.WorkmateRepository;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailActivityViewModel extends ViewModel {

    RestaurantRepository restaurantRepository;
    WorkmateRepository workmateRepository;

    public RestaurantDetailActivityViewModel(@NonNull RestaurantRepository restaurantRepository,@NonNull WorkmateRepository workmateRepository) {
        this.restaurantRepository = restaurantRepository;
        this.workmateRepository = workmateRepository;
    }

    @Nullable
    public Restaurant getRestaurantFromId(long idRestaurant) {
        return restaurantRepository.getRestaurantFromId(idRestaurant);
    }

    @Nullable
    public Workmate getWorkmateFromUid(@NonNull String uid) {
        return workmateRepository.getWorkmateFromUid(uid);
    }

    @NonNull
    public LiveData<List<RestaurantWorkmateVote>> getRestaurantWorkmateVoteLiveData() {
        return workmateRepository.getRestaurantWorkmateVoteListLiveData();
    }

    public boolean getRestaurantWorkmateVote(@NonNull String workmateUid, long restaurantUid) {
        return workmateRepository.getRestaurantWorkmateVote(workmateUid, restaurantUid);
    }

    public void setRestaurantWorkmateVote(@NonNull String workmateUid, long restaurantUid) {
        workmateRepository.createRestaurantWorkmateVote(workmateUid, restaurantUid);
    }

    public void removeRestaurantWorkmateVote(@NonNull String workmateUid, long restaurantUid) {
        workmateRepository.removeRestaurantWorkmateVote(workmateUid, restaurantUid);
    }

    public void setRestaurantSelected(@NonNull String workmateUid,Long restaurantId) {
        workmateRepository.setRestaurantSelectedToWorkmate(workmateUid, restaurantId);
    }

    @NonNull
    public LiveData<List<Workmate>> getWorkmateListLiveData() {
        return workmateRepository.getWorkmateListLiveData();
    }

    @NonNull
    public List<WorkmateListItem> getWorkmateListItemList(@NonNull Restaurant restaurant) {
        List<Workmate> workmateList = workmateRepository.getWorkmateListFromRestaurant(restaurant.getId());
        List<WorkmateListItem> workmateListItemList = new ArrayList<>();
        for (Workmate workmate: workmateList) {
            workmateListItemList.add(new WorkmateListItem(workmate, null, WorkmateListItem.DISPLAY_TEXTE_JOINING));
        }
        return workmateListItemList;
    }

    @NonNull
    public String setCapitalFistChar(@NonNull String string) {
        char[] char_table = string.toCharArray();
        char_table[0] = Character.toUpperCase(char_table[0]);
        return new String(char_table);
    }
}