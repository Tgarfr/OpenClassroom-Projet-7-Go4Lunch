package com.startup.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.model.WorkmateListItem;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.SearchRepository;
import com.startup.go4lunch.repository.WorkmateRepository;

import java.util.ArrayList;
import java.util.List;

public class WorkmateListFragmentViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final WorkmateRepository workmateRepository;
    private final SearchRepository searchRepository;

    public WorkmateListFragmentViewModel(@NonNull WorkmateRepository workmateRepository,@NonNull RestaurantRepository restaurantRepository,@NonNull SearchRepository searchRepository) {
        this.restaurantRepository = restaurantRepository;
        this.workmateRepository = workmateRepository;
        this.searchRepository = searchRepository;
    }

    @NonNull
    public LiveData<List<Workmate>> getWorkmateListLiveData() {
        return workmateRepository.getWorkmateListLiveData();
    }

    @NonNull
    public List<WorkmateListItem> getWorkmateListItemList() {
        List<Workmate> workmateList;
        if (searchRepository.getWorkmateListFragmentSearchLiveData().getValue() == null) {
            workmateList = workmateRepository.getWorkmateListLiveData().getValue();
        } else {
            workmateList = workmateRepository.getWorkmateListResearchedFromString(searchRepository.getWorkmateListFragmentSearchLiveData().getValue());
        }
        List<WorkmateListItem> workmateListItemList = new ArrayList<>();
        if (workmateList != null) {
            for (Workmate workmate: workmateList) {
                int displayTextType;
                if (workmate.getRestaurantSelectedUid() == 0) {
                    displayTextType = WorkmateListItem.DISPLAY_TEXTE_NOT_DECIDED;
                } else {
                    displayTextType = WorkmateListItem.DISPLAY_TEXTE_EATING;
                }
                workmateListItemList.add(new WorkmateListItem(workmate,restaurantRepository.getRestaurantFromId(workmate.getRestaurantSelectedUid()), displayTextType));
            }
        }
        return workmateListItemList;
    }

    @NonNull
    public LiveData<String> getWorkmateListSearchStringLiveData() {
        return searchRepository.getWorkmateListFragmentSearchLiveData();
    }
}