package com.startup.go4lunch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OverpassGsonObject {

    @SerializedName("elements")
    @Expose
    private List<OverpassElements> overpassElementslist;

    public List<OverpassElements> getOverpassElementslist() {
        return overpassElementslist;
    }
}