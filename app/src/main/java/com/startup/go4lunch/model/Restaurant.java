package com.startup.go4lunch.model;

public class Restaurant {

    private Long id;
    private String name;
    private String type;
    private float latitude;
    private float longitude;
    private String address;
    private String openingTime;

    public Restaurant(Long id, String name, String type, float latitude, float longitude, String address, String openingTime) {
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

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getOpeningTime() {
        return openingTime;
    }
}