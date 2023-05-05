package com.startup.go4lunch.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.startup.go4lunch.api.WorkmateApi;
import com.startup.go4lunch.model.RestaurantWorkmateVote;
import com.startup.go4lunch.model.Workmate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkmateRepository {

    private final WorkmateApi workmateApi;
    private final LiveData<List<Workmate>> workmateListLiveData;
    private final LiveData<List<RestaurantWorkmateVote>> restaurantWorkmateVoteListLivedata;

    public WorkmateRepository(@NonNull WorkmateApi workmateApi) {
        this.workmateApi = workmateApi;
        workmateListLiveData = workmateApi.getWorkmateListLiveData();
        restaurantWorkmateVoteListLivedata = workmateApi.getRestaurantWorkmateVoteListLiveData();
    }

    @NonNull
    public LiveData<List<Workmate>> getWorkmateListLiveData() {
        return workmateListLiveData;
    }

    @NonNull
    public LiveData<List<RestaurantWorkmateVote>> getRestaurantWorkmateVoteListLiveData() {
        return restaurantWorkmateVoteListLivedata;
    }

    public void createWorkmate(@NonNull Workmate workmate) {
        workmateApi.createWorkmate(workmate);
    }

    @Nullable
    public Workmate getWorkmateFromUid(@NonNull String workmateUidSearched) {
        List<Workmate> workmateList = workmateListLiveData.getValue();
        if (workmateList != null) {
            for (Workmate workmate: workmateList) {
                if (workmate.getUid().equals(workmateUidSearched))
                    return workmate;
            }
        }
        return null;
    }

    @NonNull
    public List<Workmate> getWorkmateListResearchedFromString(@NonNull String string) {
        List<Workmate> workmateList = workmateListLiveData.getValue();
        List<Workmate> workmateListResearched = new ArrayList<>();
        if (workmateList != null) {
            for (Workmate workmate: workmateList) {
                if (workmate.getName().toLowerCase().contains(string.toLowerCase())) {
                    workmateListResearched.add(workmate);
                }
            }
        }
        return workmateListResearched;
    }

    @NonNull
    public List<Workmate> getWorkmateListFromRestaurant(long restaurantUid) {
        List<Workmate> workmateList = workmateListLiveData.getValue();
        List<Workmate> workmateListResearched = new ArrayList<>();
        if (workmateList != null) {
            for (Workmate workmate: workmateList) {
                if (workmate.getRestaurantSelectedUid() != null && workmate.getRestaurantSelectedUid() == restaurantUid) {
                    workmateListResearched.add(workmate);
                }
            }
        }
        return workmateListResearched;
    }

    public void setRestaurantSelectedToWorkmate(@NonNull String workmateUid,@Nullable Long restaurantSelectedUid) {
        workmateApi.setWorkmateRestaurantSelectedUid(workmateUid, restaurantSelectedUid);
    }

    public void createRestaurantWorkmateVote(@NonNull String workmateUid, long restaurantUid) {
        if (!getRestaurantWorkmateVote(workmateUid,restaurantUid)) {
            workmateApi.createRestaurantWorkmateVote(new RestaurantWorkmateVote(workmateUid, restaurantUid));
        }
    }

    public void removeRestaurantWorkmateVote(@NonNull String workmateUid, long restaurantUid) {
        if (getRestaurantWorkmateVote(workmateUid,restaurantUid)) {
            workmateApi.removeRestaurantWorkmateVote(new RestaurantWorkmateVote(workmateUid, restaurantUid));
        }
    }

    public boolean getRestaurantWorkmateVote(@NonNull String workmateUid, long restaurantUid) {
        List<RestaurantWorkmateVote> restaurantWorkmateVoteList = restaurantWorkmateVoteListLivedata.getValue();
        if (restaurantWorkmateVoteList != null) {
            for (RestaurantWorkmateVote restaurantWorkmateVote : restaurantWorkmateVoteListLivedata.getValue()) {
                if (Objects.equals(restaurantWorkmateVote.getWorkmateUid(), workmateUid) & restaurantWorkmateVote.getRestaurantUid() == restaurantUid) {
                    return true;
                }
            }
        }
        return false;
    }
}