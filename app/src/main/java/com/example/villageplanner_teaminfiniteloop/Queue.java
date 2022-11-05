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
    private ArrayList<String> allUserLocation = new ArrayList<String>();
    private int queueTime;

    public void calculateQueueTimeCallBack() {
        // TODO RETRIEVE THE RESTAURANT COORDINATES TO USE IN CALCULATION
        double restLat = 0;
        double resLong = 0;

        int userInRadius = 0;

        // go through all the locations and if it's within bound,
        for (int i=0; i<allUserLocation.size(); i++) {
            // if within radius of .0000# (up to 5 decimal decisions) for
            // both long and lat then in radius
            ArrayList<Double> arr = Queue.stringToDouble(allUserLocation.get(i));
            Double la = arr.get(0);
            Double lo = arr.get(1);
            String latCutOffFormat = String.format("%.5f", la);
            String loCutOffFormat = String.format("%.5f", lo);
            Double latCutOff = Double.parseDouble(latCutOffFormat);
            Double loCutOff = Double.parseDouble(loCutOffFormat);
//            LatLng latLng = new LatLng(la, lo);

        }
        // use it to calculate queue time
        // filter the location coordinates by PRECISE radius within the restaurant coordinate
        // count how many users are in the area at the time --> n
        // queue time: n * 0.5
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
