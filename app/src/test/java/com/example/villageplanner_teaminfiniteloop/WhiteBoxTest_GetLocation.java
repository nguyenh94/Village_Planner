package com.example.villageplanner_teaminfiniteloop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.location.Location;
import android.location.LocationManager;

import org.junit.Test;
import org.junit.runner.RunWith;

public class WhiteBoxTest_GetLocation {
    Location testing = new Location(LocationManager.NETWORK_PROVIDER);

    @Test
    public void testCheckingLocation() {
       assertNotNull(testing);
    }

}
