package com.example.villageplanner_teaminfiniteloop;

import android.location.Location;
import android.media.Image;

import java.util.ArrayList;

public class User {
    private String id;
    private String email;
    private Image photo;
    private String name;
    private String password;
    public ArrayList<Reminder> reminders;
    private Location userCoordinate;

    public User(String id, String email, String name, Image photo, String password, ArrayList<Reminder> reminders) {
        this.id = id;
        this.email = email;
        this.photo = photo;
        this.password = password;
        this.reminders = reminders;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public Image getPhoto() {
        return this.photo;
    }

    public Location getCurrLocation() {
        /*Call API to get the user's
        location and return coordinate*/
        return this.userCoordinate;
    }

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
