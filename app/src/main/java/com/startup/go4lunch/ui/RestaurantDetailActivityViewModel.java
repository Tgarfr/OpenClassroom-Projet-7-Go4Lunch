package com.startup.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.RestaurantWorkmateVote;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.model.WorkmateListItem;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.WorkmateRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantDetailActivityViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final WorkmateRepository workmateRepository;
    private final FirebaseUser firebaseUser;

    public RestaurantDetailActivityViewModel(@NonNull RestaurantRepository restaurantRepository,@NonNull WorkmateRepository workmateRepository) {
        this.restaurantRepository = restaurantRepository;
        this.workmateRepository = workmateRepository;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    public LiveData<Restaurant> getRestaurantLiveData(long idRestaurant) {
        return Transformations.map(restaurantRepository.getRestaurantListLiveData(), restaurantList -> {
            if (restaurantList != null) {
                for (Restaurant restaurant: restaurantList) {
                    if (restaurant.getId() == idRestaurant) {
                        return restaurant;
                    }
                }
            }
            return null;
        });
    }

    @NonNull
    public LiveData<Workmate> getUserWorkmateLiveData() {
        return Transformations.map(workmateRepository.getWorkmateListLiveData(), workmateList -> {
            if (workmateList != null && firebaseUser != null) {
                for (Workmate workmate : workmateList) {
                    if (workmate.getUid().equals(firebaseUser.getUid())) {
                        return workmate;
                    }
                }
            }
            return null;
        });
    }

    @NonNull
    public LiveData<List<WorkmateListItem>> getWorkmateListItemLiveData(long restaurantUid) {
        return Transformations.map(workmateRepository.getWorkmateListLiveData(), workmateList -> {
            List<WorkmateListItem> workmateListItemList = new ArrayList<>();
            if (workmateList != null) {
                for (Workmate workmate: workmateList) {
                    if (workmate.getRestaurantSelectedUid() != null && workmate.getRestaurantSelectedUid() == restaurantUid) {
                        workmateListItemList.add(new WorkmateListItem(workmate, null, WorkmateListItem.DISPLAY_TEXT_JOINING));
                    }
                }
            }
            return workmateListItemList;
        });
    }

    @NonNull
    public LiveData<Boolean> getRestaurantWorkmateVoteLiveData(@NonNull String workmateUid, long restaurantUid) {
        return Transformations.map(workmateRepository.getRestaurantWorkmateVoteListLiveData(), restaurantWorkmateVoteList -> {
            if (restaurantWorkmateVoteList != null) {
                for (RestaurantWorkmateVote restaurantWorkmateVote : restaurantWorkmateVoteList) {
                    if (Objects.equals(restaurantWorkmateVote.getWorkmateUid(), workmateUid) & restaurantWorkmateVote.getRestaurantUid() == restaurantUid) {
                        return true;
                    }
                }
            }
            return false;
        });
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