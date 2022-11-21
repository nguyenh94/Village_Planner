package com.example.villageplanner_teaminfiniteloop;

import static org.junit.Assert.assertNotNull;

import com.example.villageplanner_teaminfiniteloop.ui.map.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

public class WhiteBoxTest_DirectionMatrixUrl {
    MyLocation tester = new MyLocation();
    @Test
    public void testDirectionMatrixUrl() {
        String url = tester.getDirectionsUrl(new LatLng(0.0, 0.0), new LatLng(3.0, 3.0));
        assertNotNull(url);
    }
}
