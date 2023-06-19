package com.startup.go4lunch.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Workmate {

    private String uid;
    private String name;
    private String avatarUri;
    private Long restaurantSelectedUid;

    public Workmate() {}

    public Workmate(@NonNull String uid, @NonNull String name, @Nullable String avatarUri,@Nullable Long restaurantSelectedUid) {
        this.uid = uid;
        this.name = name;
        this.avatarUri = avatarUri;
        this.restaurantSelectedUid = restaurantSelectedUid;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getAvatarUri() {
        return avatarUri;
    }

    @Nullable
    public Long getRestaurantSelectedUid() {
        return restaurantSelectedUid;
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Workmate workmate = (Workmate) object;
        return workmate.getUid().equals(getUid()) &&
                workmate.getName().equals(getName()) &&
                Objects.equals(workmate.getAvatarUri(), getAvatarUri()) &&
                Objects.equals(workmate.getRestaurantSelectedUid(), getRestaurantSelectedUid());
    }
}