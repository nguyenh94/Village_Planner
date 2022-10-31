package com.example.villageplanner_teaminfiniteloop;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Queue {

    public void calculateQueueTime(String restaurantId)
    {
        // get the User's email to be used as ID to look up User's location
        User user = new User();
        String userEmail = user.getEmail();

        // get the user's location from the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

            }
        });
        final Object[] userData = new Object[1];

        // retrieve the restaurant coordinate
        // filter the location coordinates by PRECISE radius within the restaurant coordinate
        // count how many users are in the area at the time --> n
        // queue time: n * 0.5
    }
}
