package com.example.villageplanner_teaminfiniteloop;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

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

    public void calculateQueueTimeCallBack(ArrayList<String> allUserLocation) {
        // TODO RETRIEVE THE RESTAURANT COORDINATES TO USE IN CALCULATION
        double restLat = 0;
        double resLong = 0;

        int userInRadius = 0;

        // go through all the locations and if it's within bound,

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
                    ArrayList<String> allUserLocation = new ArrayList<String>();
                    for(QueryDocumentSnapshot document: task.getResult()) {
                        User user = document.toObject(User.class);
                        String userLocation = user.getLocation();
                        allUserLocation.add(userLocation);
                    }
                    calculateQueueTimeCallBack(allUserLocation);
                }
            }
        });
    }
}
