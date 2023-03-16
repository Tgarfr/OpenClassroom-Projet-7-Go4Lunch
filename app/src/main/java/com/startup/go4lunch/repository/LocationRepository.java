package com.startup.go4lunch.repository;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class LocationRepository {

    private final MutableLiveData<Location> locationLiveData;

    public LocationRepository() {
        locationLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Location> getLocationLiveData() {
        return locationLiveData;
    }

    public void setLocation(@NonNull Location location) {
        this.locationLiveData.setValue(location);
    }

    public static Location getCompanyLocation() {
        Location location = new Location("Company Location");
        location.setLatitude(48.85834269238794);
        location.setLongitude(2.294392951104722);
        return location;
    }
}