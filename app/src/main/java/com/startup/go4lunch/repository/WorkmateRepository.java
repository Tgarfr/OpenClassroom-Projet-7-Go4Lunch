package com.startup.go4lunch.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.startup.go4lunch.api.WorkmateApi;
import com.startup.go4lunch.model.Workmate;

import java.util.List;

public class WorkmateRepository {

    WorkmateApi workmateApi;

    public WorkmateRepository(WorkmateApi workmateApi) {
        this.workmateApi = workmateApi;
    }

    @NonNull
    public LiveData<List<Workmate>> getWorkmateListLiveData() {
        return workmateApi.getWorkmateListLiveData();
    }

    public void createWorkmate(@NonNull Workmate workmate) {
        workmateApi.createWorkmate(workmate);
    }
}