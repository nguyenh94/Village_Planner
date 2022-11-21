package com.example.villageplanner_teaminfiniteloop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

public class WhiteBoxTest_CheckAtLA {
    MyLocation tester = new MyLocation();

    @Test
    public void CheckAtLA() {
        LatLng point = new LatLng(0.02057333333333, -11.28614333333334);    //Some place not at LA
        assertFalse(tester.checkAtLA(point));   //Should return false
    }
}
