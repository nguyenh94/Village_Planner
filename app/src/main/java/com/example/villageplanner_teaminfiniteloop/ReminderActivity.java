package com.example.villageplanner_teaminfiniteloop;
import static android.provider.Settings.System.getString;

import android.app.Activity;
import android.app.AlarmManager ;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent ;
import android.content.Context;
import android.content.Intent ;
import android.os.Build;
import android.os.Bundle ;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View ;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.villageplanner_teaminfiniteloop.databinding.ActivityMainMapBinding;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Calendar ;
import java.sql.Time;

public class ReminderActivity extends Activity{
    //TODO
    private static final String CHANNEL_ID = "channelID";
    private String title;
    private Time reminderTime;
    private Time travelTime;
    private Time queueTime;
    private String restaurantId;
    private String reminderId;

    EditText textIn;
    Button buttonAdd;
    LinearLayout container;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
//        createNotificationChannel();
//        registerNotification("notif1", reminderTime ,this);
       //TODO https://stackoverflow.com/questions/34517520/how-to-give-notifications-on-android-on-specific-time
       reminderNotification();
       setContentView(R.layout.activity_main);
       //TODO http://android-er.blogspot.com/2013/05/add-and-remove-view-dynamically.html
       textIn = (EditText)findViewById(R.id.textin);
       buttonAdd = (Button)findViewById(R.id.add);
       container = (LinearLayout)findViewById(R.id.container);

       buttonAdd.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View arg0) {
               LayoutInflater layoutInflater =
                       (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               final View addView = layoutInflater.inflate(R.layout.row, null);
               TextView textOut = (TextView)addView.findViewById(R.id.textout);
               textOut.setText(textIn.getText().toString());
               Button buttonRemove = (Button)addView.findViewById(R.id.remove);
               buttonRemove.setOnClickListener(new View.OnClickListener(){

                   @Override
                   public void onClick(View v) {
                       ((LinearLayout)addView.getParent()).removeView(addView);
                   }});

               container.addView(addView);
           }});
   }
    public void reminderNotification()
    {
        NotificationUtils _notificationUtils = new NotificationUtils(this);
        long _currentTime = System.currentTimeMillis();
        long tenSeconds = 1000 * 10;
        long _triggerReminder = _currentTime + tenSeconds; //triggers a reminder after 10 seconds.
        _notificationUtils.setReminder(_triggerReminder);
    }

    public ReminderActivity(String title, String id)
    {
        this.title = title;
        this.reminderId = id;
    }
    public void edit(String title, String id, Time travelTime, Time queueTime)
    {

    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void registerNotification(String title, Time reminderTime, Activity activity)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText("context text")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

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
    public String getReminderTitle()
    {
        return this.title;
    }
    public Time getReminderTime()
    {
        return this.reminderTime;
    }
}
