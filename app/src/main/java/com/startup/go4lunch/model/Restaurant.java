package com.startup.go4lunch.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Restaurant {

    private final long id;
    private final String name;
    private final String type;
    private final float latitude;
    private final float longitude;
    private final String address;
    private final String openingTime;


    public Restaurant(Long id,@NonNull String name,@Nullable String type, float latitude, float longitude,@Nullable String address,@Nullable String openingTime) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.openingTime = openingTime;
    }

    public Long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getType() {
        return type;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    @Nullable
    public String getOpeningTime() {
        return openingTime;
    }

    @Nullable
    public static Restaurant from(@NonNull OverpassElements overpassElements) {
        if (overpassElements.getTags().getName() != null) {
            long id = overpassElements.getId();
            String name = overpassElements.getTags().getName();
            String type = overpassElements.getTags().getCuisine();
            float latitude = overpassElements.getLat();
            float longitude = overpassElements.getLon();

            String address = null;
            if (overpassElements.getTags().getHouseNumber() != null) {
                address = overpassElements.getTags().getHouseNumber()+" ";
            }
            if (overpassElements.getTags().getStreet() != null) {
                address = address + overpassElements.getTags().getStreet()+" ";
            }
            if (overpassElements.getTags().getPostCode() != null) {
                address = address + overpassElements.getTags().getPostCode()+" ";
            }
            if (overpassElements.getTags().getCity() != null) {
                address = address + overpassElements.getTags().getCity();
            }
            if (address == null) {
                address = "Address not provided";
            }

            String openingTime;
            if (overpassElements.getTags().getOpeningHours() != null) {
                openingTime = overpassElements.getTags().getOpeningHours();
            } else {
                openingTime = "Opening time not provided";
            }

            return new Restaurant(id, name, type, latitude, longitude, address, openingTime);
        } else {
            return null;
        }
    }
}