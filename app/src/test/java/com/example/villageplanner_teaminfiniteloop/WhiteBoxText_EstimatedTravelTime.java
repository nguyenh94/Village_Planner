package com.example.villageplanner_teaminfiniteloop;

import static org.junit.Assert.assertNotNull;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

public class WhiteBoxText_EstimatedTravelTime {
    MyLocation tester = new MyLocation();

    @Test
    public void EstimatedTravelTime() {
        LatLng origin = new LatLng(34.02057333333333, -118.28614333333334);
        LatLng destination = new LatLng(34.02501264425152, -118.28534442947347);
        String travelTime = tester.getEstimatedTravelTime(origin, destination);
        assertNotNull(travelTime);
    }
}
