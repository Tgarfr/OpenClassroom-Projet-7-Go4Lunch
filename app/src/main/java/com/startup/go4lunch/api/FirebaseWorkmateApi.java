package com.startup.go4lunch.api;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.startup.go4lunch.model.Workmate;

import java.util.ArrayList;
import java.util.List;

public class FirebaseWorkmateApi implements WorkmateApi {

    private static final String COLLECTION_NAME = "workmates";
    private final CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    private final MutableLiveData<List<Workmate>> workmateListLiveData = new MutableLiveData<>();

    @NonNull
    @Override
    public LiveData<List<Workmate>> getWorkmateListLiveData() {
        fetchWorkmatesList();
        return workmateListLiveData;
    }

    public void fetchWorkmatesList() {
        collectionReference.orderBy("name").addSnapshotListener((value, error) -> {
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

    @Override
    public void createWorkmate(@NonNull Workmate workmate) {
        collectionReference.document(workmate.getUid()).set(workmate);
    }
}
