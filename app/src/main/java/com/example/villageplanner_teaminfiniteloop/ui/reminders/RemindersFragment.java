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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.villageplanner_teaminfiniteloop.ListviewAdapter;
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
import com.google.firebase.firestore.Source;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemindersFragment extends Fragment {

    private FragmentRemindersBinding binding;
    private static final String CHANNEL_ID = "channelID";
    private ListView listView;
    private List hourList;
    private List minuteList;
    private List reminderList;
    Button submitButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //TODO Adapt this tutorial to our data
        RemindersViewModel dashboardViewModel =
                new ViewModelProvider(this).get(RemindersViewModel.class);
        binding = FragmentRemindersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listView = (ListView) root.findViewById(R.id.listView1);
        listView.setItemsCanFocus(true);
        hourList = new ArrayList<Integer>();
        minuteList = new ArrayList<Integer>();
        reminderList = new ArrayList<String>();

        submitButton = (Button)root.findViewById(R.id.editAReminder);
        String userEmail = User.currentUserEmail;
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ArrayList<String> newReminders = combineText(hourList, minuteList, reminderList);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("users").document(userEmail);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {  // if email exists
                                docRef.update("reminders", newReminders);
                            } else {  // if email doesn't exists how are they logged in

                            }
                        }
                    }
                });
            }}
        );

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRefSecond = db.collection("users").document(userEmail);
        docRefSecond.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {  // if email exists
                        User user = document.toObject(User.class);

                        hourList = translateReminderHours(user.getReminders());
                        minuteList = translateReminderMinutes(user.getReminders());
                        reminderList = translateReminderTitles(user.getReminders());

                        ListviewAdapter adpter = new ListviewAdapter(root.getContext(), hourList, minuteList, reminderList);
                        listView.setAdapter(adpter);
                    }
                } else {
                    Log.d("getting user", "get failed with ", task.getException());
                }
            }
        });
        return root;
    }


    public ArrayList<String> combineText(List<Integer> hourList, List<Integer> minuteList, List reminderList)
    {
        ArrayList<String> remindersInString = new ArrayList<String>();
        for(Integer i=0;i<reminderList.size();i++)
        {
            remindersInString.add(reminderList.get(i) +"?"+String.valueOf(hourList.get(i)) +"?" + String.valueOf(minuteList.get(i)));
        }
        return remindersInString;
    }

    public List<Integer> translateReminderHours(List<String> reminders) {
        ArrayList<Integer> usersHours = new ArrayList<Integer>();
        try{
            for (String reminder : reminders) {
                String[] reminderComponents = reminder.split("\\?");
                usersHours.add(Integer.parseInt(reminderComponents[1]));
            }
        }
        catch (Exception e) {
            return usersHours;
        }
        return usersHours;
    }
    public List<Integer> translateReminderMinutes(List<String> reminders) {
        ArrayList<Integer> usersMinutes = new ArrayList<Integer>();
        try {
            for (String reminder : reminders) {
                String[] reminderComponents = reminder.split("\\?");
                usersMinutes.add(Integer.parseInt(reminderComponents[2]));
            }
        }
        catch (Exception e) {
            return usersMinutes;
        }
        return usersMinutes;
    }
    public List<String> translateReminderTitles(List<String> reminders) {
        ArrayList<String> usersReminders = new ArrayList<String>();
        try{
            for (String reminder : reminders) {
                String[] reminderComponents = reminder.split("\\?");
                usersReminders.add(reminderComponents[0]);
            }
        }
        catch(Exception e) {
            return usersReminders;
        }
        return usersReminders;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}