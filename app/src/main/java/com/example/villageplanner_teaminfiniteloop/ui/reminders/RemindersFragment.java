package com.example.villageplanner_teaminfiniteloop.ui.reminders;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;


import com.example.villageplanner_teaminfiniteloop.ListviewAdapter;
import com.example.villageplanner_teaminfiniteloop.LoginActivity;
import com.example.villageplanner_teaminfiniteloop.R;
import com.example.villageplanner_teaminfiniteloop.Reminder;
import com.example.villageplanner_teaminfiniteloop.ReminderCreatorActivity;
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

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    private List departureList;
    private List arrivalList;
    private List reminderList;
    private List queueAndTravelTime;

    Button submitButton;
    ScrollView scrollView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //TODO Adapt this tutorial to our data
        RemindersViewModel dashboardViewModel =
                new ViewModelProvider(this).get(RemindersViewModel.class);
        binding = FragmentRemindersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listView = (ListView) root.findViewById(R.id.listViewReminders);
        scrollView = (ScrollView) root.findViewById(R.id.scrollView);
        listView.setItemsCanFocus(true);
        departureList = new ArrayList<String>();
        arrivalList = new ArrayList<Integer>();
        reminderList = new ArrayList<String>();
        queueAndTravelTime = new ArrayList<String>();

        submitButton = (Button)root.findViewById(R.id.editAReminder);
        String userEmail = User.currentUserEmail;
        listView.setFocusable(false);
        scrollView.smoothScrollTo(0,0);
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ArrayList<String> newReminders = combineText(arrivalList, departureList, reminderList, queueAndTravelTime);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("users").document(userEmail);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {  // if email exists
                                docRef.update("reminders", newReminders);
                                Toast.makeText(getContext(), "Reminder Edit Successful.", Toast.LENGTH_LONG).show();
                            } else {  // if email doesn't exists how are they logged in

                            }
                        }
                    }
                    //Switch to the reminder page
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    listView.invalidateViews();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

                        departureList = translateReminderDeparture(user.getReminders());
                        arrivalList = translateReminderArrival(user.getReminders());
                        reminderList = translateReminderTitles(user.getReminders());
                        queueAndTravelTime = translateWaitTimes(user.getReminders());

                        ListviewAdapter adpter = new ListviewAdapter(root.getContext(), departureList, arrivalList, reminderList, queueAndTravelTime);
                        listView.setAdapter(adpter);
                    }
                } else {
                    Log.d("getting user", "get failed with ", task.getException());
                }
            }
        });
        return root;
    }

    public ArrayList<String> combineText(List<String> arrivalList, List<String> departureList, List<String> reminderList, List<String> queueAndTravelTime)
    {
        ArrayList<String> remindersInString = new ArrayList<String>();
        String departureFinalHoursString = "";
        String departureFinalMinutesString = "";
        try{
            for(Integer i=0;i<reminderList.size();i++)
            {
                String[] arrivalComponents = arrivalList.get(i).split(":");
                Integer arrivalInMinutes = 60*Integer.parseInt(arrivalComponents[0]) + Integer.parseInt(arrivalComponents[1]);
                Integer wait = Integer.parseInt(queueAndTravelTime.get(i));
                Integer departureMinutes = arrivalInMinutes - wait;
                Integer departureFinalMinutes = departureMinutes % 60;
                Integer departureFinalHours = departureMinutes / 60;
                if(String.valueOf(departureFinalHours).length()<2){
                    departureFinalHoursString = "0" + String.valueOf(departureFinalHours);
                }
                else{
                    departureFinalHoursString = String.valueOf(departureFinalHours);
                }
                if(String.valueOf(departureFinalMinutes).length()<2){
                    departureFinalMinutesString = "0" + String.valueOf(departureFinalMinutes);
                }
                else{
                    departureFinalMinutesString = String.valueOf(departureFinalMinutes);
                }
                if(departureFinalHoursString == "00") {
                    departureFinalHoursString = "12";
                }
                remindersInString.add(reminderList.get(i) +"?"+String.valueOf(arrivalList.get(i)) +"?" +
                        departureFinalHoursString +":" + departureFinalMinutesString + "?"+queueAndTravelTime.get(i));
            }
            return remindersInString;
        }
        catch (Exception e) {
            return remindersInString;
        }
    }

    public List<String> translateReminderDeparture(List<String> reminders) {
        ArrayList<String> departure = new ArrayList<String>();
        try{
            for (String reminder : reminders) {
                String[] reminderComponents = reminder.split("\\?");
                departure.add(reminderComponents[2]);
            }
        }
        catch (Exception e) {
            return departure;
        }
        return departure;
    }
    public List<String> translateReminderArrival(List<String> reminders) {
        ArrayList<String> reminderArrivals = new ArrayList<String>();
        try {
            for (String reminder : reminders) {
                String[] reminderComponents = reminder.split("\\?");
                reminderArrivals.add(reminderComponents[1]);
            }
        }
        catch (Exception e) {
            return reminderArrivals;
        }
        return reminderArrivals;
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

    public List<String> translateWaitTimes(List<String> reminders) {
        ArrayList<String> waitTimes = new ArrayList<String>();
        try{
            for (String reminder : reminders) {
                String[] reminderComponents = reminder.split("\\?");
                waitTimes.add(reminderComponents[3]);
            }
        }
        catch(Exception e) {
            return waitTimes;
        }
        return waitTimes;
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}