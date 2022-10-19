package com.example.villageplanner_teaminfiniteloop;

import android.location.Location;

public class Restaurant {
    private String id;
    private String name;
    private Location resCoordinate;

    public Restaurant(String resId, String name, Location coordinate)
    {
        this.id = resId;
        this.name = name;
        this.resCoordinate = coordinate;
    }

    public void findDirectiontoStore(Location userLocation, Location resLocation)
    {

    }

    public String getId()
    {
        return this.id;
    }

    public String getResName()
    {
        return this.name;
    }

    public Location getCoordinates()
    {
        return this.resCoordinate;
    }
}
