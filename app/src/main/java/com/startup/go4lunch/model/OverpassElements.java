package com.startup.go4lunch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OverpassElements {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("lat")
    @Expose
    private float lat;

    @SerializedName("lon")
    @Expose
    private float lon;

    @SerializedName("tags")
    @Expose
    private OverpassTags tags;


    public Long getId() {
        return id;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public OverpassTags getTags() {
        return tags;
    }
}