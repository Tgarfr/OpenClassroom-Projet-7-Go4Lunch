package com.startup.go4lunch.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WorkmateListItem {

    private final Workmate workmate;

    private final Restaurant restaurantChoice;

    public WorkmateListItem(@NonNull Workmate workmate, @Nullable Restaurant restaurantChoice) {
        this.workmate = workmate;
        this.restaurantChoice = restaurantChoice;
    }

    @NonNull
    public Workmate getWorkmate() {
        return workmate;
    }

    @Nullable
    public Restaurant getRestaurantChoice() {
        return restaurantChoice;
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        WorkmateListItem workmateListItem = (WorkmateListItem) object;
        if (!workmateListItem.getWorkmate().equals(getWorkmate())) {
            return false;
        }
        if (workmateListItem.getRestaurantChoice() == null)  {
            return getRestaurantChoice() == null;
        }
        return workmateListItem.getRestaurantChoice().equals(getRestaurantChoice());
    }
}