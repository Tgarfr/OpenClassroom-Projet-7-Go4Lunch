package com.startup.go4lunch.api;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.startup.go4lunch.api.utilApi.OverpassCalls;
import com.startup.go4lunch.api.utilApi.OverpassElements;
import com.startup.go4lunch.api.utilApi.OverpassGsonObject;
import com.startup.go4lunch.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class OverpassRestaurantApi implements RestaurantApi, OverpassCalls.Callback {

    MutableLiveData<List<Restaurant>> restaurantListLiveData = new MutableLiveData<>();

    public OverpassRestaurantApi() {
        OverpassCalls.fetchRestaurantList(this);
    }


    @Override
    public LiveData<List<Restaurant>> getRestaurantListLiveData() {
        return this.restaurantListLiveData;
    }

    @Override
    public void onResponse(@Nullable OverpassGsonObject overpassGsonObject) {
        if (overpassGsonObject != null) {
            this.restaurantListLiveData.setValue(overpassGsonObjectToRestaurantList(overpassGsonObject));
        }
    }

    @Override
    public void onFailure() {

    }

    private List<Restaurant> overpassGsonObjectToRestaurantList(OverpassGsonObject overpassGsonObject) {

        List<Restaurant> restaurantList = new ArrayList<>();
        List<OverpassElements> overpassElementslist = overpassGsonObject.getOverpassElementslist();

        for (int i=0;i<overpassElementslist.size();i++) {
            OverpassElements overpassElements = overpassElementslist.get(i);

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

                String openingTime = null;
                if (overpassElements.getTags().getOpeningHours() != null) {
                    openingTime = overpassElements.getTags().getOpeningHours();
                } else {
                    openingTime = "Opening time not provided";
                }

                restaurantList.add(new Restaurant(id, name, type, latitude, longitude, address, openingTime));
            }
        }
        return restaurantList;
    }
}