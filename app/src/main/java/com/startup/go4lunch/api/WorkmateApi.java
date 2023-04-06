package com.startup.go4lunch.api;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.startup.go4lunch.model.Workmate;

import java.util.List;

public interface WorkmateApi {

    @NonNull
    LiveData<List<Workmate>> getWorkmateListLiveData();

    void createWorkmate(@NonNull Workmate workmate);
}