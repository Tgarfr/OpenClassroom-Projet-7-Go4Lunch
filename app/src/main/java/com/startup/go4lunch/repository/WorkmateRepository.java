package com.startup.go4lunch.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.startup.go4lunch.model.Workmate;

public class WorkmateRepository {

    private static final String COLLECTION_NAME = "workmates";
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private final CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);

    public Query getWorkmateListQuery() {
        return collectionReference.orderBy("name");
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