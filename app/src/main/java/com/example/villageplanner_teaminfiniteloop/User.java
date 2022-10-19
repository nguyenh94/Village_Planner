package com.example.villageplanner_teaminfiniteloop;

import android.location.Location;
import android.media.Image;

import java.util.ArrayList;

public class User {
    private String id;
    private String email;
    private Image photo;
    private String password;
    public ArrayList<Reminder> reminders;
    private Location userCoordinate;

    public User(String initId, String initEmail, Image initPhoto, String initPass, ArrayList<Reminder> initReminders) {
        id = initId;
        email = initEmail;
        photo = initPhoto;
        password = initPass;
        reminders = initReminders;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Image getPhoto() {
        return photo;
    }

    public Location getCurrLocation() {
        /*Call API to get the user's
        location and return coordinate*/
        return userCoordinate;
    }

    public void setId(String newId) {
        id = newId;
    }

    public void setEmail(String newEmail) {
        email = newEmail;
    }

    public void setPass(String newPassword) {
        password = newPassword;
    }

    public void setPhoto(Image newPhoto) {
        photo = newPhoto;
    }

}
