package com.example.villageplanner_teaminfiniteloop;

import static com.google.android.gms.common.util.CollectionUtils.mapOf;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.List;

public class ReminderCreatorActivity extends AppCompatActivity {

    EditText textIn;
    Button buttonAdd;
    Button buttonCreateReminder;
    LinearLayout container;
    TextView restaurantNameTV;
    private static final String CHANNEL_ID = "channelID";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_reminder);
        textIn = (EditText)findViewById(R.id.reminderDescription);
        container = (LinearLayout)findViewById(R.id.container);
        buttonCreateReminder = (Button)findViewById(R.id.createReminder);
        restaurantNameTV = (TextView) findViewById(R.id.restaurantName);

        Bundle bundle = getIntent().getExtras();
        String restaurantName = bundle.getString("restaurantName");
        String travelTime = String.valueOf(bundle.getString("travelTime"));
        String waitingTime = String.valueOf(bundle.getString("waitingTime"));
        ViewGroup finalContainer = container;
        restaurantNameTV.setText(restaurantName);
        buttonCreateReminder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                EditText title = (EditText) findViewById(R.id.reminderDescription);
                String reminderTitle = title.getText().toString();
                final TimePicker tp = (TimePicker) findViewById(R.id.reminderTimePicker);
                Integer Hours = tp.getCurrentHour();
                Integer Minutes = tp.getCurrentMinute();
                String finalHours = "";
                String finalMinutes = "";
                if(String.valueOf(Hours).length()<2){
                    finalHours = "0" + String.valueOf(Hours);
                }
                else {
                    finalHours = String.valueOf(Hours);
                }
                if(String.valueOf(Minutes).length()<2){
                    finalMinutes = "0" + String.valueOf(Minutes);
                }
                else {
                    finalMinutes = String.valueOf(Minutes);
                }
                //notification time == departure time
                String notificationTime = getCorrectNotificationTime(travelTime, waitingTime, Hours, Minutes);
                String createdReminder = reminderTitle + "?" + finalHours + ":" + finalMinutes + "?" + notificationTime;
                //TODO CREATE NOTIFICATION
                reminderNotification();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String userEmail = User.currentUserEmail;
                final List<String>[] usersReminders = new List[0];
                DocumentReference docRef = db.collection("users").document(userEmail);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {  // if email exists
                                docRef.update("reminders", FieldValue.arrayUnion(createdReminder));
                                Toast.makeText(getApplicationContext(), "Reminder Creation Successful.", Toast.LENGTH_LONG).show();
                            } else {  // if email doesn't exists how are they logged in

                            }
                        } else {
                            Log.d("getting user", "get failed with ", task.getException());
                        }
                    }
                });

                Intent intent = new Intent(ReminderCreatorActivity.this, TabBarActivity.class);
                startActivity(intent);
            }});
    }

    public String getCorrectNotificationTime(String travelTime, String waitingTime, Integer reminderHours, Integer reminderMinutes) {
        Integer waitingTimeConverted = 0;
        Integer travelTimeConverted;
        String finalHours = "";
        String finalMinutes = "";
        try {
            waitingTime.split(" ");
        }
        catch (Exception e) {
            waitingTimeConverted = 0;
        }
        try {
            String[] travelTimeList = travelTime.split(" ");
            travelTimeConverted = Integer.parseInt(travelTimeList[0]) * 60 + Integer.parseInt(travelTimeList[2]);
        }
        catch (Exception e) {
            travelTimeConverted = 0;
        }
        Integer notificationTimeinMinutes = (reminderHours * 60 + reminderMinutes) - travelTimeConverted - waitingTimeConverted;
        Integer notificationMinutes = notificationTimeinMinutes % 60;
        Integer notificationHours = notificationTimeinMinutes / 60;
        if(String.valueOf(notificationHours).length()<2){
            finalHours = "0" + String.valueOf(notificationHours);
        }
        if(String.valueOf(notificationMinutes).length()<2){
            finalMinutes = "0" + String.valueOf(notificationHours);
        }
        return finalHours + ":" + finalMinutes;
    }
    public void reminderNotification()
    {
        NotificationUtils _notificationUtils = new NotificationUtils(this);
        long _currentTime = System.currentTimeMillis();
        long tenSeconds = 1000 * 10;
        long _triggerReminder = _currentTime + tenSeconds; //triggers a reminder after 10 seconds.
        _notificationUtils.setReminder(_triggerReminder);
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
            NotificationManager notificationManager = getBaseContext().getSystemService(NotificationManager.class);
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

}