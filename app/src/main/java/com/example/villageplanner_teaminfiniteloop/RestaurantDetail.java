package com.example.villageplanner_teaminfiniteloop;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;

public class RestaurantDetail extends AppCompatActivity {
    String name;
    String location;
    String travelTime;
    Location currentLocation = User.currentLocation;
    Location destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        location = intent.getStringExtra("location");
        destination = Queue.toLocation(location);
        TextView restaurantLabel = (TextView) findViewById(R.id.restaurantLabel);
        restaurantLabel.setText(name);

        AndroidNetworking.initialize(getApplicationContext());
        getEstimatedTravelTime();
    }

    private void getEstimatedTravelTime(){
        String origin = currentLocation.getLatitude() + "%2C" + currentLocation.getLongitude();
        String dest = destination.getLatitude() + "%2C" + destination.getLongitude();
        AndroidNetworking.get("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origin +"&destinations="+dest)
                .addQueryParameter("mode", "walking")
                .addQueryParameter("key", "AIzaSyAk2zt_F2hY2v76do6CwRUvjA_RZhHpM1Q")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response);
                        //System.out.println("Success!");
                        // do anything with response
                    }
                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                        //System.out.println("Failed!");
                        // handle error
                    }
                });
    }

}