package com.startup.go4lunch.api;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.startup.go4lunch.model.OverpassElements;
import com.startup.go4lunch.model.OverpassGsonObject;
import com.startup.go4lunch.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OverpassRestaurantApi implements RestaurantApi, Callback<OverpassGsonObject> {

    MutableLiveData<List<Restaurant>> restaurantListLiveData = new MutableLiveData<>();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://overpass-api.de/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @NonNull
    @Override
    public MutableLiveData<List<Restaurant>> getRestaurantListLiveData() {
        return restaurantListLiveData;
    }

    @Override
    public void fetchLocationNearLocation(@NonNull Location location) {
        fetchRestaurantList(location);
    }

    @Override
    public void onResponse(@NonNull Call<OverpassGsonObject> call, Response<OverpassGsonObject> response) {
        if (response.body() != null) {
            this.restaurantListLiveData.setValue(overpassGsonObjectToRestaurantList(response.body()));
        }
    }

    @Override
    public void onFailure(@NonNull Call<OverpassGsonObject> call, @NonNull Throwable throwable) {

    }

    private void fetchRestaurantList(@NonNull Location location) {
        OverpassService overpassService = retrofit.create(OverpassService.class);
        final String overpassSetting = "[out:json][timeout:100];nwr['amenity'='restaurant']("+ generateLocationZoneString(location)+");out body;";
        Call<OverpassGsonObject> call = overpassService.getRestaurantList(overpassSetting);
        call.enqueue(this);
    }

    private static String generateLocationZoneString(@NonNull Location location) {
        String lowLatitude = String.valueOf(location.getLatitude()-.02);
        String highLatitude = String.valueOf(location.getLatitude()+.02);
        String lowLongitude = String.valueOf(location.getLongitude()-.02);
        String highLongitude = String.valueOf(location.getLongitude()+.02);
        return lowLatitude+','+lowLongitude+','+highLatitude+','+highLongitude;
    }

    private List<Restaurant> overpassGsonObjectToRestaurantList(OverpassGsonObject overpassGsonObject) {
        List<OverpassElements> overpassElementslist = overpassGsonObject.getOverpassElementslist();
        List<Restaurant> restaurantList = new ArrayList<>();
        for (OverpassElements overpassElements : overpassElementslist) {
            Restaurant restaurant = Restaurant.from(overpassElements);
            if (restaurant != null) {
                restaurantList.add(restaurant);
            }
        }
        return restaurantList;
    }
}