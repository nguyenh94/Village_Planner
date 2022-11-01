package com.example.villageplanner_teaminfiniteloop.ui.reminders;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RemindersFragment extends Fragment {

    private FragmentRemindersBinding binding;
    EditText textIn;
    Button buttonAdd;
    Button buttonCreateReminder;
    LinearLayout container;
    private static final String CHANNEL_ID = "channelID";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RemindersViewModel dashboardViewModel =
                new ViewModelProvider(this).get(RemindersViewModel.class);
        reminderNotification();
        binding = FragmentRemindersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        textIn = (EditText)root.findViewById(R.id.reminderDescription);
        buttonAdd = (Button)root.findViewById(R.id.createReminder);
        container = (LinearLayout)root.findViewById(R.id.container);
        buttonCreateReminder = (Button)root.findViewById(R.id.createReminder);

        ViewGroup finalContainer = container;
        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.row, null);
                TextView textOut = (TextView)addView.findViewById(R.id.textout);
                textOut.setText(textIn.getText().toString());
                Button buttonRemove = (Button)addView.findViewById(R.id.remove);
                buttonRemove.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        ((LinearLayout)addView.getParent()).removeView(addView);
                    }});
               finalContainer.addView(addView);
            }});

        buttonCreateReminder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                EditText title = (EditText) root.findViewById(R.id.reminderDescription);
                String reminderTitle = title.getText().toString();
                final TimePicker tp = (TimePicker) root.findViewById(R.id.reminderTimePicker);
                ((EditText) root.findViewById(R.id.reminderDescription)).setText("");
                Reminder createdReminder = new Reminder();
                createdReminder.setReminderTitle(reminderTitle);
                Integer Hours = tp.getCurrentHour();
                Integer Minutes = tp.getCurrentMinute();
                createdReminder.setReminderTime(Hours, Minutes);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String userEmail = "test7gmail.com";
                DocumentReference docRef = db.collection("users").document(userEmail);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {  // if email exists
//                                User user = document.toObject(User.class);
//                                ArrayList<Reminder> usersReminders = user.getReminders();
//                                usersReminders.add(createdReminder);
//                                user.setReminders(usersReminders);
                                db.collection("users").document(userEmail).update("reminders", FieldValue.arrayUnion(createdReminder));
                                Toast.makeText(root.getContext(), "Reminder Creation Successful.", Toast.LENGTH_LONG).show();
                            } else {  // if email doesn't exists how are they logged in

                            }
                        } else {
                            Log.d("getting user", "get failed with ", task.getException());
                        }
                    }
                });
                //Move back home
//                Intent intent = new Intent(LoginActivity.this, TabBarActivity.class);
//                startActivity(intent);
            }});
        return root;
    }
    public void reminderNotification()
    {
        NotificationUtils _notificationUtils = new NotificationUtils(getActivity());
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
            NotificationManager notificationManager = getActivity().getBaseContext().getSystemService(NotificationManager.class);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}