package com.startup.go4lunch.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Comparator;

public class RestaurantListItem {

    private final Restaurant restaurant;
    private int distance;
    private int numberOfWorkmate;
    private int score;
    public static final int SORT_BY_NAME = 1;
    public static final int SORT_BY_DISTANCE = 2;
    public static final int SORT_BY_TYPE = 3;
    public static final int SORT_BY_NOTE = 4;

    public RestaurantListItem(@NonNull Restaurant restaurant, int distance, int numberOfWorkmate, int score) {
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

    public int getScore() {
        return score;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setNumberOfWorkmate(int numberOfWorkmate) {
        this.numberOfWorkmate = numberOfWorkmate;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RestaurantListItem restaurantListItem = (RestaurantListItem) obj;
        return restaurant.equals(restaurantListItem.getRestaurant()) &&
                distance == restaurantListItem.getDistance() &&
                numberOfWorkmate == restaurantListItem.getNumberOfWorkmate() &&
                score == restaurantListItem.getScore();
    }

    public static class RestaurantListItemNameComparator implements Comparator<RestaurantListItem> {
        @Override
        public int compare(RestaurantListItem o1, RestaurantListItem o2) {
            return o1.getRestaurant().getName().compareTo(o2.getRestaurant().getName());
        }
    }

    public static class RestaurantListItemDistanceComparator implements Comparator<RestaurantListItem> {
        @Override
        public int compare(RestaurantListItem o1, RestaurantListItem o2) {
            return o1.getDistance() - o2.getDistance();
        }
    }

    public static class RestaurantListItemTypeComparator implements Comparator<RestaurantListItem> {
        @Override
        public int compare(RestaurantListItem o1, RestaurantListItem o2) {
            if (o1.getRestaurant().getType() == null & o2.getRestaurant().getType() == null) {
                return 0;
            }
            if (o1.getRestaurant().getType() == null) {
                return 1;
            }
            if (o2.getRestaurant().getType() == null) {
                return -1;
            }
            return o1.getRestaurant().getType().compareTo(o2.getRestaurant().getType());
        }
    }

    public static class RestaurantListItemRateComparator implements Comparator<RestaurantListItem> {
        @Override
        public int compare(RestaurantListItem o1, RestaurantListItem o2) {
            return (int) (o1.getScore() - o2.getScore());
        }
    }
}