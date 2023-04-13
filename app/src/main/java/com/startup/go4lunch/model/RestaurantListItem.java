package com.startup.go4lunch.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RestaurantListItem {

    private final Restaurant restaurant;
    private int distance;
    private short numberOfWorkmate;
    private float score;

    public RestaurantListItem(@NonNull Restaurant restaurant, int distance, short numberOfWorkmate, float score) {
        this.restaurant = restaurant;
        this.distance = distance;
        this.numberOfWorkmate = numberOfWorkmate;
        this.score = score;
    }

    @NonNull
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public int getDistance() {
        return distance;
    }

    public int getNumberOfWorkmate() {
        return numberOfWorkmate;
    }

    public float getScore() {
        return score;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setNumberOfWorkmate(short numberOfWorkmate) {
        this.numberOfWorkmate = numberOfWorkmate;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RestaurantListItem restaurantListItem = (RestaurantListItem) obj;
        return restaurant.equals(restaurantListItem.getRestaurant()) && distance == restaurantListItem.getDistance();
    }
}