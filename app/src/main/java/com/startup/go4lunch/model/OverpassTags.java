package com.startup.go4lunch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OverpassTags {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("cuisine")
    @Expose
    private String cuisine;

    @SerializedName("addr:housenumber")
    @Expose
    private String houseNumber;

    @SerializedName("addr:street")
    @Expose
    private String street;

    @SerializedName("addr:postcode")
    @Expose
    private String postCode;

    @SerializedName("addr:city")
    @Expose
    private String city;

    @SerializedName("opening_hours")
    @Expose
    private String openingHours;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("website")
    @Expose
    private String website;

    public String getName() {
        return name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getCity() {
        return city;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }
}