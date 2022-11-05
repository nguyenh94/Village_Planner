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
    private ArrayList<String> allUserLocation = new ArrayList<String>();
    private int userInRadius = 0;
    private int queueTime;

    public void calculateQueueTimeCallBack() {
        Location resLoc = toLocation(resCoordinate);
        double resLat = resLoc.getLatitude();
        double resLong = resLoc.getLongitude();

        // go through all the locations and if it's within bound,
        for (int i=0; i<allUserLocation.size(); i++) {
            // if within radius of .0000# (up to 5 decimal decisions) for
            // both long and lat then in radius
            Location userLoc = toLocation(allUserLocation.get(i));
            double userLat = userLoc.getLatitude();
            double userLong = userLoc.getLongitude();
            double userToResDistance = calculateDistance(resLat, resLong, userLat, userLong);
            if (userToResDistance < 0.0094697) {     // radius = 50 feet = 0.0094697 miles
                userInRadius++;    // count how many users are in the area at the time --> n
            }
        }
    }

    public double convertToRad(double deg) {
        return (deg * Math.PI) / 1800;
    }

    public double calculateDistance(double resLat, double resLong, double userLat, double userLong) {
        // haversine formula a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
        double theta = userLong - resLong;
        double distance = Math.sin(convertToRad(resLat)) * Math.sin(convertToRad(userLat))
                + Math.cos(convertToRad(resLat)) * Math.cos(convertToRad(userLat)) * Math.cos(convertToRad(theta));
        distance = Math.acos(distance) * 3963.0;  // get distance between coordinates in miles
        System.out.println("Testing distance is " + distance);
        return distance;
    }

    public void calculateQueueTime(String restaurantId)
    {
        // get the user's location from the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for(QueryDocumentSnapshot document: task.getResult()) {
                        User user = document.toObject(User.class);
                        String userLocation = user.getLocation();
                        allUserLocation.add(userLocation);
                    }
                    calculateQueueTimeCallBack();
                }
            }
        });
    }

    public int getQueueTime() { return this.queueTime; }

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
