package com.startup.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.model.WorkmateListItem;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.WorkmateRepository;

import java.util.ArrayList;
import java.util.List;

public class WorkmateListFragmentViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final LiveData<List<Workmate>> workmateListLiveData;

    public WorkmateListFragmentViewModel(@NonNull WorkmateRepository workmateRepository,@NonNull RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
        workmateListLiveData = workmateRepository.getWorkmateListLiveData();
    }

    @NonNull
    public LiveData<List<Workmate>> getWorkmateListLiveData() {
        return workmateListLiveData;
    }

    @NonNull
    public List<WorkmateListItem> getWorkmateListItemList() {
        List<Workmate> workmateList = workmateListLiveData.getValue();
        List<WorkmateListItem> workmateListItem = new ArrayList<>();
        if (workmateList != null) {
            for (Workmate workmate: workmateList) {
                workmateListItem.add(new WorkmateListItem(workmate,restaurantRepository.getRestaurantFromId(workmate.getRestaurantUid())));
            }
        }
        return workmateListItem;
    }
}