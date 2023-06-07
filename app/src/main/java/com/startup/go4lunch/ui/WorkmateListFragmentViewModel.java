package com.startup.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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
    private final MutableLiveData<List<WorkmateListItem>> workmateListItemListLiveData = new MutableLiveData<>();
    private final Observer<List<Workmate>> workmateListObserver;
    private final Observer<String> searchObserver;

    public WorkmateListFragmentViewModel(@NonNull WorkmateRepository workmateRepository,@NonNull RestaurantRepository restaurantRepository,@NonNull SearchRepository searchRepository) {
        this.restaurantRepository = restaurantRepository;
        this.workmateRepository = workmateRepository;
        this.searchRepository = searchRepository;

        workmateListObserver = this::getWorkmateListItemList;
        searchObserver = string -> {
            if (string != null) {
                getWorkmateListItemList(workmateRepository.getWorkmateListResearchedFromString(string));
            } else {
                getWorkmateListItemList(workmateRepository.getWorkmateListLiveData().getValue());
            }
        };

        workmateRepository.getWorkmateListLiveData().observeForever(workmateListObserver);
        searchRepository.getWorkmateListFragmentSearchLiveData().observeForever(searchObserver);
    }

    @Override
    protected void onCleared() {
        workmateRepository.getWorkmateListLiveData().removeObserver(workmateListObserver);
        searchRepository.getWorkmateListFragmentSearchLiveData().removeObserver(searchObserver);
        super.onCleared();
    }

    @NonNull
    public LiveData<List<WorkmateListItem>> getWorkmateListLiveData() {
        return workmateListItemListLiveData;
    }

    public void getWorkmateListItemList(@Nullable List<Workmate> workmateList) {
        if (workmateList != null) {
            List<WorkmateListItem> workmateListItemList = new ArrayList<>();
            for (Workmate workmate: workmateList) {
                int displayTextType;
                if (workmate.getRestaurantSelectedUid() == null) {
                    displayTextType = WorkmateListItem.DISPLAY_TEXT_NOT_DECIDED;
                    workmateListItemList.add(new WorkmateListItem(workmate,null, displayTextType));
                } else {
                    displayTextType = WorkmateListItem.DISPLAY_TEXT_EATING;
                    workmateListItemList.add(new WorkmateListItem(workmate,restaurantRepository.getRestaurantFromId(workmate.getRestaurantSelectedUid()), displayTextType));
                }
            }
            workmateListItemListLiveData.setValue(workmateListItemList);
        }
    }
}