package com.example.villageplanner_teaminfiniteloop;

import android.location.Location;
import android.media.Image;

import java.util.ArrayList;

public class User {
    public static int PERMISSION_ID = 44;
    private String id;
    private String email;
    private Image photo;
    private String name;
    private String password;
    public ArrayList<Reminder> reminders;
    private String location;
    public static Location currentLocation;
    public static String userName;
    public static String userEmail;
    public static Image userPhoto;

    public User() {
        this.id = "";
        this.email = "";
        this.photo = null;
        this.password = "";
        this.reminders = null;
        this.name = "";
        this.location = "";
    }

    public User(String id, String email, String name, Image photo, String password, String location, ArrayList<Reminder> reminders) {
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

    public String getName() { return this.name; }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() { return this.password; }

    public Image getPhoto() {
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

    public void setPhoto(Image newPhoto) {
        this.photo = newPhoto;
    }

}
