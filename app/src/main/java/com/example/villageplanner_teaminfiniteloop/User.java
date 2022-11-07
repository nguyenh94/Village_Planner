package com.example.villageplanner_teaminfiniteloop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class User {
    public static int PERMISSION_ID = 44;
    private String id;
    private String email;
    private String photo;
    private String name;
    private String password;
    public ArrayList<String> reminders;
    private String location;
    public static Location currentLocation;
    public static String currentUserEmail;
    public static String currentUserName;
    public static Uri currentUserPhoto;

    public User() {
        this.id = "";
        this.email = "";
        this.photo = null;
        this.password = "";
        this.reminders = null;
        this.name = "";
        this.location = "";
    }

    public User(String id, String email, String name, String photo, String password, String location, ArrayList<String> reminders) {
        this.id = id;
        this.email = email;
        this.photo = photo;
        this.password = password;
        this.reminders = reminders;
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() { return this.password; }

    public String getPhoto() {
        return this.photo;
    }

    public String getLocation() { return this.location; }


    public void setId(String newId) {
        this.id = newId;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public void setPass(String newPassword) {
        this.password = newPassword;
    }

    public void setPhoto(String newPhoto) {
        this.photo = newPhoto;
    }

    public ArrayList<String> getReminders() {
        return this.reminders;
    }
}

