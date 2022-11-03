package com.example.villageplanner_teaminfiniteloop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RestaurantDetail extends AppCompatActivity {
    String name;
    String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        location = intent.getStringExtra("location");
        TextView restaurantLabel = (TextView) findViewById(R.id.restaurantLabel);
        restaurantLabel.setText(name);
    }
}