package com.startup.go4lunch.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.startup.go4lunch.api.WorkmateApi;
import com.startup.go4lunch.model.Workmate;

import java.util.ArrayList;
import java.util.List;

public class WorkmateRepository {

    WorkmateApi workmateApi;

    public WorkmateRepository(@NonNull WorkmateApi workmateApi) {
        this.workmateApi = workmateApi;
    }

    @NonNull
    public LiveData<List<Workmate>> getWorkmateListLiveData() {
        return workmateApi.getWorkmateListLiveData();
    }

    public void createWorkmate(@NonNull Workmate workmate) {
        workmateApi.createWorkmate(workmate);
    }

    @NonNull
    public List<Workmate> getWorkmateListResearchedByString(@NonNull String string) {
        List<Workmate> workmateList = workmateApi.getWorkmateListLiveData().getValue();
        List<Workmate> workmateListResearched = new ArrayList<>();
        if (workmateList != null) {
            for (Workmate workmate: workmateList) {
                if (workmate.getName().toLowerCase().contains(string.toLowerCase())) {
                    workmateListResearched.add(workmate);
                }
            }
        }
        return workmateListResearched;
    }
}