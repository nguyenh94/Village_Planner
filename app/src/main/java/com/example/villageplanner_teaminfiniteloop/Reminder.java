package com.example.villageplanner_teaminfiniteloop;
import static android.provider.Settings.System.getString;

import android.app.AlarmManager ;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent ;
import android.content.Intent ;
import android.os.Build;
import android.os.Bundle ;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View ;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.Calendar ;
import java.sql.Time;

public class Reminder {
    //TODO
    private static final String CHANNEL_ID = "";
    private String title;
    private Time reminderTime;
    private Time travelTime;
    private Time queueTime;
    private String restaurantId;
    private String reminderId;

    public Reminder(String title, String id)
    {
        this.title = title;
        this.reminderId = id;
    }
    public void edit(String title, String id, Time travelTime, Time queueTime)
    {

    }
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//    public void registerNotification(String title, Time reminderTime)
//    {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.notification_icon)
//                .setContentTitle("Notification")
//                .setContentText("context text")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//    }

    public String getResId()
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
    public String getTitle()
    {
        return this.title;
    }
    public Time getReminderTime()
    {
        return this.reminderTime;
    }
}
