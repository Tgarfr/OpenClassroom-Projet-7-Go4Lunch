package com.startup.go4lunch.api;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.startup.go4lunch.model.RestaurantWorkmateVote;
import com.startup.go4lunch.model.Workmate;

import java.util.ArrayList;
import java.util.List;

public class FirebaseWorkmateApi implements WorkmateApi {

    private static final String COLLECTION_WORKMATES = "workmates";
    private static final String COLLECTION_RESTAURANT_WORKMATE_VOTE = "restaurant_workmate_votes";
    private final CollectionReference collectionWorkmates = FirebaseFirestore.getInstance().collection(COLLECTION_WORKMATES);
    private final CollectionReference collectionRestaurantWorkmateVote = FirebaseFirestore.getInstance().collection(COLLECTION_RESTAURANT_WORKMATE_VOTE);
    private final MutableLiveData<List<Workmate>> workmateListLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<RestaurantWorkmateVote>> restaurantWorkmateVoteListLiveData = new MutableLiveData<>();

    @NonNull
    @Override
    public LiveData<List<Workmate>> getWorkmateListLiveData() {
        fetchWorkmatesList();
        return workmateListLiveData;
    }

    @Override
    public void createWorkmate(@NonNull Workmate workmate) {
        collectionWorkmates.document(workmate.getUid()).set(workmate, SetOptions.mergeFields("name","avatarUri"));
    }

    @Override
    public void setWorkmateRestaurantSelectedUid(@NonNull String workmateUid, long restaurantUid) {
        collectionWorkmates.document(workmateUid).update("restaurantSelectedUid",restaurantUid);
    }

    @NonNull
    @Override
    public LiveData<List<RestaurantWorkmateVote>> getRestaurantWorkmateVoteListLiveData() {
        fetchRestaurantWorkmateVote();
        return restaurantWorkmateVoteListLiveData;
    }

    @Override
    public void createRestaurantWorkmateVote(@NonNull RestaurantWorkmateVote restaurantWorkmateVote) {
        collectionRestaurantWorkmateVote.document().set(restaurantWorkmateVote)
                .addOnSuccessListener(unused -> fetchRestaurantWorkmateVote());
    }

    @Override
    public void removeRestaurantWorkmateVote(@NonNull RestaurantWorkmateVote restaurantWorkmateVote) {
        collectionRestaurantWorkmateVote
                .whereEqualTo("workmateUid",restaurantWorkmateVote.getWorkmateUid())
                .whereEqualTo("restaurantUid",restaurantWorkmateVote.getRestaurantUid())
                .get().addOnSuccessListener(queryDocumentSnapshots ->
                        collectionRestaurantWorkmateVote
                        .document(queryDocumentSnapshots.getDocuments().get(0).getId())
                        .delete()
                        .addOnCompleteListener(taskComplete -> fetchRestaurantWorkmateVote()));
    }

    public void fetchWorkmatesList() {
        collectionWorkmates.orderBy("name").addSnapshotListener((value, error) -> {
            if (value != null) {
                List<DocumentSnapshot> documentsList = value.getDocuments();
                List<Workmate> workmateList = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot: documentsList) {
                    workmateList.add(documentSnapshot.toObject(Workmate.class));
                }
                workmateListLiveData.setValue(workmateList);
            }
        });
    }

    public void fetchRestaurantWorkmateVote() {
        collectionRestaurantWorkmateVote.orderBy("workmateUid").addSnapshotListener((value, error) -> {
            if (value != null) {
                List<DocumentSnapshot> documentsList = value.getDocuments();
                List<RestaurantWorkmateVote> restaurantWorkmateVoteList = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot: documentsList) {
                    restaurantWorkmateVoteList.add(documentSnapshot.toObject(RestaurantWorkmateVote.class));
                }
                restaurantWorkmateVoteListLiveData.setValue(restaurantWorkmateVoteList);
            }
        });
    }
}
