package com.example.villageplanner_teaminfiniteloop;

import java.sql.Time;

public class Reminder {
    String title;
    Time reminderTime;
    Time travelTime;
    Time queueTime;
    String restaurantId;
    String reminderId;

    public Reminder(String title, String id)
    {
        this.title = title;
        this.reminderId = id;
    }
    public void edit(String title, String id, Time travelTime, Time queueTime)
    {

    }
    public void registerNotification(String title, Time reminderTime)
    {

    }

    public String getResId()
    {
        return this.restaurantId;
    }
    public Time getQueueTime()
    {
        return this.queueTime;
    }
    public Time getTravelTime()
    {
        return this.travelTime;
    }
    public String getTitle()
    {
        return this.title;
    }
    public Time getReminderTime()
    {
        return this.reminderTime;
    }
}
