package com.example.villageplanner_teaminfiniteloop;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.villageplanner_teaminfiniteloop.ui.map.MapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RestaurantDetail extends AppCompatActivity {
    String name;
    String location;
    String travelTime;
    Location currentLocation = User.currentLocation;
    Location destination;
    TravelMode travelMode = TravelMode.Drive;

    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView travelTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        location = intent.getStringExtra("location");
        destination = Restaurant.toLocation(location);
        TextView restaurantLabel = (TextView) findViewById(R.id.restaurantLabel);
        restaurantLabel.setText(name);

        radioGroup = findViewById(R.id.radioGroup);
        travelTimeTextView = findViewById(R.id.text_view_travelTimeLabel);

        Button getDirectionButton = findViewById(R.id.button_directionButton);
        getDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                Intent myIntent = new Intent(RestaurantDetail.this, TabBarActivity.class);
                myIntent.putExtra("destinationLat", destination.getLatitude());
                myIntent.putExtra("destinationLong", destination.getLongitude());
                switch (travelMode) {
                    case Drive:
                        myIntent.putExtra("travelMode", "driving");
                        break;
                    case Walking:
                        myIntent.putExtra("travelMode", "walking");
                        break;
                    case Cycling:
                        myIntent.putExtra("travelMode", "bicycling");
                        break;
                    case Transit:
                        myIntent.putExtra("travelMode", "transit");
                        break;
                }
                RestaurantDetail.this.startActivity(myIntent);
            }
        });

        AndroidNetworking.initialize(getApplicationContext());
        getEstimatedTravelTime();
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        switch (radioButton.getText().toString()) {
            case "Driving":
                travelMode = TravelMode.Drive;
                break;
            case "Walking":
                travelMode = TravelMode.Walking;
                break;
            case "Bicycling":
                travelMode = TravelMode.Cycling;
                break;
            case "Transit":
                travelMode = TravelMode.Transit;
                break;
            default:
                return;
        }
        getEstimatedTravelTime();
    }

    public void setReminderButtonClicked(View v) {
        Intent myIntent = new Intent(this, ReminderCreatorActivity.class);
        myIntent.putExtra("restaurantName", name);
        myIntent.putExtra("travelTime", travelTime);
        myIntent.putExtra("waitingTime", 0);
        this.startActivity(myIntent);
    }

    public void getDirectionButtonClicked(View v){

    }


    private void getEstimatedTravelTime(){
        String mode = "driving";
        switch (travelMode) {
            case Drive:
                mode = "driving";
                break;
            case Walking:
                mode = "walking";
                break;
            case Cycling:
                mode = "bicycling";
                break;
            case Transit:
                mode = "transit";
                break;
        }
        String origin = currentLocation.getLatitude() + "%2C" + currentLocation.getLongitude();
        String dest = destination.getLatitude() + "%2C" + destination.getLongitude();
        AndroidNetworking.get("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origin +"&destinations="+dest)
                .addQueryParameter("mode", mode)
                .addQueryParameter("key", "AIzaSyAk2zt_F2hY2v76do6CwRUvjA_RZhHpM1Q")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            JSONArray a = response.getJSONArray("rows");
                            JSONObject b = a.getJSONObject(0);
                            JSONArray c = b.getJSONArray("elements");
                            JSONObject d = c.getJSONObject(0);
                            JSONObject e = d.getJSONObject("duration");
                            travelTime = e.getString("text");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        travelTimeTextView.setText("Estimated Travel Time: " + travelTime);
                        System.out.println(travelTime);
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                    }
                });
    }

    enum TravelMode {
        Drive,
        Walking,
        Cycling,
        Transit
    }

}