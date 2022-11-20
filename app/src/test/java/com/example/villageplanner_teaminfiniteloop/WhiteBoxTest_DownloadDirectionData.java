package com.example.villageplanner_teaminfiniteloop;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.io.IOException;

public class WhiteBoxTest_DownloadDirectionData {
    MyLocation tester = new MyLocation();
    //
    @Test
    public void downloadDirectionData() throws IOException {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=34.02057333333333,-118.28614333333334&destination=34.02501264425152,-118.28534442947347&sensor=false&mode=driving&key=AIzaSyAk2zt_F2hY2v76do6CwRUvjA_RZhHpM1Q";
        String data = tester.downloadUrl(url);
        assertNotNull(data);
    }
}
