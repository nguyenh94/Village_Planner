package com.example.villageplanner_teaminfiniteloop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.villageplanner_teaminfiniteloop.ui.me.MeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    static String coordinate;

    MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
        @Override
        public void gotLocation(Location location){
            // store the user's location in the database
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            coordinate = String.format("%f, %f", latitude, longitude);
            User.currentLocation = location;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkLocationPermission();

        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("restaurants").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for(QueryDocumentSnapshot document: task.getResult()) {
                        Restaurant restaurant = document.toObject(Restaurant.class);
                        Queue.restaurants.add(restaurant);
                    }
                }
            }
        });
    }

    public void goToRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void loginCallBack(View view, boolean emailValid, String name, String photo, String email, String password) {
        TextView passwordEntered = (TextView) findViewById(R.id.password);
        String unhashedPass = passwordEntered.getText().toString();

        if (emailValid) {
            Login_Registration login_helper = new Login_Registration();

            // check password by hashing current password and compare to user's stored password
            String hashedPass = login_helper.hashPassword(unhashedPass);
            if (password.equals(hashedPass)) {
                // update the user's location whenever logs in
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference users = db.collection("users");
                users.document(email).update("location", coordinate);

                Toast.makeText(view.getContext(), "Login Successful.", Toast.LENGTH_LONG).show();
                User.currentUserEmail = email;
                User.currentUserName = name;
//                User.currentUserPhoto = photo;

                //Move to home
                Intent intent = new Intent(LoginActivity.this, TabBarActivity.class);
                startActivity(intent);
            } else {  // if password is incorrect
                Toast.makeText(view.getContext(), "Incorrect password.", Toast.LENGTH_LONG).show();
            }
        } else {  // if email is not registered
            Toast.makeText(view.getContext(), "Email is not registered.", Toast.LENGTH_LONG).show();
        }
    }

    public void loginButtonPressed(View view) {
        TextView passwordEntered = (TextView) findViewById(R.id.password);
        TextView emailEntered = (TextView) findViewById(R.id.email);

        String password = passwordEntered.getText().toString();
        String userEmail = emailEntered.getText().toString();

        // check if email is valid format
        boolean validEmailFormat = Login_Registration.checkEmailFormat(userEmail);
        if (!validEmailFormat) {
            Toast.makeText(view.getContext(), "Please enter a valid email.", Toast.LENGTH_LONG).show();
            return;
        }

        // check if query to the database to get email and password
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("users").document(userEmail);
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDocument = task.getResult();
                    if (userDocument.exists()) {  // if email exists
                        User user = userDocument.toObject(User.class);
                        loginCallBack(view, true, user.getName(), user.getPhoto(), user.getEmail(), user.getPassword());
                    } else {  // if email doesn't exists
                        loginCallBack(view, false, null, null, userEmail, password);
                    }
                } else {
                    Log.d("getting user", "get failed with ", task.getException());
                }
            }
        });
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Access Required")
                        .setMessage("Village Planner needs your location data to perform map based activities.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LoginActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        MyLocation myLocation = new MyLocation();
                        myLocation.getLocation(this, locationResult);

                    }

                } else {

                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }

        }
    }

}