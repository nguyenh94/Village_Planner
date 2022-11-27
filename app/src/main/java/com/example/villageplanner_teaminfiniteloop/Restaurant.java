package com.example.villageplanner_teaminfiniteloop;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Restaurant {
//    private String id;
    private String location;
    private String name;
    private String image;
    private Integer rating;

    public Restaurant(){
        this.location = "";
        this.name = "";
        this.image = "";
        this.rating = null;
    }

    public Restaurant(String name, String coordinate, String image, Integer rating)
    {
//        this.id = resId;
        this.location = coordinate;
        this.name = name;
        this.image = image;
        this.rating = rating;
    }

    public void findDirectionToStore(Location userLocation, Location resLocation)
    {

    }

//    public String getId()
//    {
//        return this.id;
//    }

    public String getName()
    {
        return this.name;
    }

    public String getLocation() { return this.location; }

    public String getImage() {return this.image;}

    public Integer getRating() { return this.rating;}

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    //    public static void getAllResLocation()
//    {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("restaurants").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    ArrayList<String> allResLocation = new ArrayList<String>();
//                    ArrayList<String> allResNames = new ArrayList<String>();
//                    for(QueryDocumentSnapshot document: task.getResult()) {
//                        Restaurant restaurant = document.toObject(Restaurant.class);
//                        String resLocation = restaurant.getLocation();
//                        String resName = restaurant.getResName();
//                        allResLocation.add(resLocation);
//                        allResNames.add(resName);
//                    }
//                    restaurantLocationCallBack(allResLocation);
//                    restaurantNameCallBack(allResNames);
//                }
//            }
//        });
//    }
//
//    public static ArrayList<String> restaurantLocationCallBack(ArrayList<String> allResLocation) {
//        return allResLocation;
//    }
//
//    public static ArrayList<String> restaurantNameCallBack(ArrayList<String> allResNames) {
//        return allResNames;
//    }
}
