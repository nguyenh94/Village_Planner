package com.example.villageplanner_teaminfiniteloop;

import android.app.Activity;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.os.Build;
import android.os.Bundle ;

//import android.view.LayoutInflater;
//import android.view.View ;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
//import android.widget.TextView;

import java.sql.Time;

public class Reminder{
    //TODO
    private static final String CHANNEL_ID = "channelID";
    private String title;
    private ReminderTime reminderTime;
    private Time travelTime;
    private Time queueTime;
    private String restaurantId;
    private String reminderId;

    public Reminder(String title, String id)
    {
        this.title = title;
        this.reminderId = id;
    }
    public Reminder()
    {
        this.title = "";
        this.reminderId = "";
        this.reminderTime = new ReminderTime();
    }

    public String getRestaurantIdId()
    {
        return this.restaurantId;
    }
    public String getReminderId()
    {
        return this.reminderId;
    }
    public Time getQueueTime()
    {
        return this.queueTime;
    }
    public Time getTravelTime()
    {
        return this.travelTime;
    }
    public String getReminderTitle()
    {
        return this.title;
    }
    public ReminderTime getReminderTime()
    {
        return this.reminderTime;
    }
    public void setReminderTitle(String Title)
    {
        this.title=Title;
    }
    public void setReminderTime(Integer hours, Integer minutes)
    {
        this.reminderTime.setHours(hours);
        this.reminderTime.setMinutes(minutes);
    }
}
