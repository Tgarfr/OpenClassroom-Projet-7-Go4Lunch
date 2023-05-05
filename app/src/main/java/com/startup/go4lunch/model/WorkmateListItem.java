package com.startup.go4lunch.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WorkmateListItem {

    private final Workmate workmate;

    private final Restaurant restaurantChoice;
    private final int displayTextType;
    public static final int DISPLAY_TEXT_EATING = 1;
    public static final int DISPLAY_TEXT_NOT_DECIDED = 2;
    public static final int DISPLAY_TEXT_JOINING = 3;

    public WorkmateListItem(@NonNull Workmate workmate, @Nullable Restaurant restaurantChoice, int displayTextType) {
        this.workmate = workmate;
        this.restaurantChoice = restaurantChoice;
        this.displayTextType = displayTextType;
    }

    @NonNull
    public Workmate getWorkmate() {
        return workmate;
    }

    @Nullable
    public Restaurant getRestaurantChoice() {
        return restaurantChoice;
    }

    public int getDisplayTextType() {
        return displayTextType;
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