package com.startup.go4lunch.model;

public class Restaurant {

    private Long id;
    private String name;
    private String address;
    private String openingTime;

    public Restaurant(Long id, String name, String address, String openingTime) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.openingTime = openingTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setAddresse(String address) {
        this.address = address;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }
}