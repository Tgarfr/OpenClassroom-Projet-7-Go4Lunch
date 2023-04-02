package com.startup.go4lunch.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Workmate {

    private String uid;
    private String name;

    private String avatarUri;
    private long restaurantUid;

    public Workmate() {};

    public Workmate(@NonNull String uid, @NonNull String name, @Nullable String avatarUri,@Nullable long restaurantUid) {
        this.uid = uid;
        this.name = name;
        this.avatarUri = avatarUri;
        this.restaurantUid = restaurantUid;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public long getRestaurantUid() {
        return restaurantUid;
    }
}