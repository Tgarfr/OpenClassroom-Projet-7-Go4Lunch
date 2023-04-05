package com.startup.go4lunch.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.startup.go4lunch.model.Workmate;

import java.util.List;

public class WorkmateRepository {

    private static final String COLLECTION_NAME = "workmates";
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private final CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    private final MutableLiveData<List<DocumentSnapshot>> documentsSnapshotListLiveData;

    public WorkmateRepository() {
        documentsSnapshotListLiveData = new MutableLiveData<>();
        fetchWorkmatesDocuments();
    }

    @NonNull
    public LiveData<List<DocumentSnapshot>> getWorkmateDocumentsSnapshotListLiveData() {
        return documentsSnapshotListLiveData;
    }

    public void fetchWorkmatesDocuments() {
        collectionReference.orderBy("name").addSnapshotListener((value, error) -> {
            if (value != null) {
                documentsSnapshotListLiveData.setValue(value.getDocuments());
            }
        });
    }

    public void createWorkmate() {
        if (currentUser != null) {
            String name = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Name";
            String urlAvatar = currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : null;
            Workmate workmate = new Workmate(currentUser.getUid(), name,urlAvatar, 0);
            collectionReference.document(currentUser.getUid()).set(workmate);
        }
    }
}