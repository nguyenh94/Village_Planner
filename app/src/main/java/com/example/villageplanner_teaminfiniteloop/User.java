package com.example.villageplanner_teaminfiniteloop;

import android.location.Location;
import android.media.Image;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class User {
    public static int PERMISSION_ID = 44;
    private String id;
    private String email;
    private Image photo;
    private String name;
    private String password;
    public ArrayList<Reminder> reminders;
    private Location userCoordinate;

    public User() {
        this.id = "";
        this.email = "";
        this.photo = null;
        this.password = "";
        this.reminders = new ArrayList<Reminder>();
        this.name = "";
    }

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

    public String getPassword() { return this.password; }

    public Image getPhoto() {
        return this.photo;
    }

    public ArrayList<Reminder> getReminders() {
        return this.reminders;
    }


    public void setReminders(ArrayList<Reminder> reminders) {
        this.reminders = reminders;
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
