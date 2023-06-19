package com.startup.go4lunch.model;

import androidx.annotation.NonNull;

public class RestaurantWorkmateVote {

    private String workmateUid;
    private long restaurantUid;

    @SuppressWarnings("unused")
    public RestaurantWorkmateVote() {}

    public RestaurantWorkmateVote(@NonNull String workmateUid, long restaurantUid) {
        this.workmateUid = workmateUid;
        this.restaurantUid = restaurantUid;
    }

    public String getWorkmateUid() {
        return workmateUid;
    }

    public long getRestaurantUid() {
        return restaurantUid;
    }
}
