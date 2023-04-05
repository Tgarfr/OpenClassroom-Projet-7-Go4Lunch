package com.startup.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.model.WorkmateListItem;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.WorkmateRepository;

import java.util.ArrayList;
import java.util.List;

public class WorkmateListFragmentViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    LiveData<List<DocumentSnapshot>> workmateDocumentSnapshotListLiveData;

    public WorkmateListFragmentViewModel(@NonNull WorkmateRepository workmateRepository, RestaurantRepository restaurantRepository) {
        workmateDocumentSnapshotListLiveData = workmateRepository.getWorkmateDocumentsSnapshotListLiveData();
        this.restaurantRepository = restaurantRepository;
    }

    public LiveData<List<DocumentSnapshot>> getWorkmateDocumentSnapshotListLiveData() {
        return workmateDocumentSnapshotListLiveData;
    }

    public List<WorkmateListItem> getWorkmateListItemList() {
        List<DocumentSnapshot> documentsList = workmateDocumentSnapshotListLiveData.getValue();
        List<WorkmateListItem> workmateListItem = new ArrayList<>();
        if (documentsList != null) {
            for (DocumentSnapshot documentSnapshot: documentsList) {
                Workmate workmate = documentSnapshot.toObject(Workmate.class);
                if (workmate != null) {
                    workmateListItem.add(new WorkmateListItem(workmate,restaurantRepository.getRestaurantFromId(workmate.getRestaurantUid())));
                }
            }
        }
        return workmateListItem;
    }
}