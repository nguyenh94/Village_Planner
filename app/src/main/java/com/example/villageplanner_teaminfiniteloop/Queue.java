package com.example.villageplanner_teaminfiniteloop;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Queue {
    static public ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    static public String resCoordinate;
    static public ArrayList<String> allUserLocation = new ArrayList<String>();
    static public int userInRadius = 0;
    static public int queueTime;

    public static double convertToRad(double deg) {
        return (deg * Math.PI) / 180;
    }

    // helper function to calculate the distance between 2 coordinates
    static public double calculateDistance(double resLat, double resLong, double userLat, double userLong) {
        // haversine formula a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
        double theta = userLong - resLong;
        double distance = Math.sin(convertToRad(resLat)) * Math.sin(convertToRad(userLat))
                + Math.cos(convertToRad(resLat)) * Math.cos(convertToRad(userLat)) * Math.cos(convertToRad(theta));
        distance = Math.acos(distance) * 3963.0;  // get distance between coordinates in miles
        System.out.println("Testing distance is " + distance);
        return distance;
    }

    // helper function to parse the coordinate string and convert them to double long and lat
    static public ArrayList<Double> stringToDouble(String coordinate) {
        ArrayList<Double> resCoordinate = new ArrayList<Double>();
        String[] split = coordinate.split(", ");
        Double latitude = Double.parseDouble(split[0]);
        Double longitude = Double.parseDouble(split[1]);
        resCoordinate.add(latitude);
        resCoordinate.add(longitude);
        return resCoordinate;
    }

    // helper function to convert coordinate string to Location
    static public Location toLocation(String coordinate){
        ArrayList<Double> arr = Queue.stringToDouble(coordinate);
        Double la = arr.get(0);
        Double lo = arr.get(1);
        Location result = new Location("");
        result.setLatitude(la);
        result.setLongitude(lo);
        return result;
    }
}
