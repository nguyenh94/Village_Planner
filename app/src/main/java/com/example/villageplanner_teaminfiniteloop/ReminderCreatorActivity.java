package com.example.villageplanner_teaminfiniteloop;

import static com.google.android.gms.common.util.CollectionUtils.mapOf;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.villageplanner_teaminfiniteloop.LoginActivity;
import com.example.villageplanner_teaminfiniteloop.NotificationUtils;
import com.example.villageplanner_teaminfiniteloop.R;
import com.example.villageplanner_teaminfiniteloop.Reminder;
import com.example.villageplanner_teaminfiniteloop.TabBarActivity;
import com.example.villageplanner_teaminfiniteloop.User;
import com.example.villageplanner_teaminfiniteloop.databinding.FragmentRemindersBinding;
import com.example.villageplanner_teaminfiniteloop.ui.reminders.RemindersViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReminderCreatorActivity extends AppCompatActivity {

    private FragmentRemindersBinding binding;
    EditText textIn;
    Button buttonAdd;
    Button buttonCreateReminder;
    LinearLayout container;
    private static final String CHANNEL_ID = "channelID";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_reminder);
        textIn = (EditText)findViewById(R.id.reminderDescription);
        container = (LinearLayout)findViewById(R.id.container);
        buttonCreateReminder = (Button)findViewById(R.id.createReminder);

        ViewGroup finalContainer = container;

        buttonCreateReminder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                EditText title = (EditText) findViewById(R.id.reminderDescription);
                String reminderTitle = title.getText().toString();
                final TimePicker tp = (TimePicker) findViewById(R.id.reminderTimePicker);
                ((EditText) findViewById(R.id.reminderDescription)).setText("");
                Integer Hours = tp.getCurrentHour();
                Integer Minutes = tp.getCurrentMinute();
                String createdReminder = reminderTitle + "?" + Hours + "?" + Minutes;
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

}