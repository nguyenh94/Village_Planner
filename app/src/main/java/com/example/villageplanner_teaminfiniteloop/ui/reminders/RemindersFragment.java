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
    EditText textIn;
    Button buttonAdd;
    Button buttonCreateReminder;
    LinearLayout container;
    private static final String CHANNEL_ID = "channelID";
    private ListView listView;
    private List hourList;
    private List minuteList;
    private List reminderList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //TODO Adapt this tutorial to our data
        RemindersViewModel dashboardViewModel =
                new ViewModelProvider(this).get(RemindersViewModel.class);
        reminderNotification();
        binding = FragmentRemindersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ListView lv = (ListView) root.findViewById(R.id.listView1);
        lv.setItemsCanFocus(true);
        hourList = new ArrayList<Integer>();
        minuteList = new ArrayList<Integer>();
        reminderList = new ArrayList<String>();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userEmail = "test1@gmail.com";
        final ArrayList<String>[] usersReminders = new ArrayList[]{new ArrayList<String>()};
        DocumentReference docRefSecond = db.collection("users").document(userEmail);
        docRefSecond.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {  // if email exists
                        User user = document.toObject(User.class);
//                        adpter=new ListviewAdapter(this,translateReminders(user.getReminders()));
//                        listView.setAdapter(adpter);

                        hourList = translateReminderHours(user.getReminders());
                        minuteList = translateReminderMinutes(user.getReminders());
                        reminderList = translateReminderTitles(user.getReminders());

                        ListviewAdapter adpter = new ListviewAdapter(root.getContext(), hourList, minuteList, reminderList);
                        lv.setAdapter(adpter);
                    }
                } else {
                    Log.d("getting user", "get failed with ", task.getException());
                }
            }
        });
//        ArrayList<Reminder> allReminders = translateReminders(usersReminders[0]);
        return root;
    }


    public ArrayList<Reminder> translateReminders(List<String> reminders) {
        ArrayList<Reminder> usersReminders = new ArrayList<Reminder>();
        for (String reminder : reminders) {
            String[] reminderComponents = reminder.split("\\?");
            Reminder newReminder = new Reminder();
            newReminder.setReminderTitle(reminderComponents[0]);
            newReminder.setReminderTime(Integer.parseInt(reminderComponents[1]),Integer.parseInt(reminderComponents[2]));
            usersReminders.add(newReminder);
        }
        return usersReminders;
    }

    public List<Integer> translateReminderHours(List<String> reminders) {
        ArrayList<Integer> usersHours = new ArrayList<Integer>();
        for (String reminder : reminders) {
            String[] reminderComponents = reminder.split("\\?");
//            Reminder newReminder = new Reminder();
//            newReminder.setReminderTitle(reminderComponents[0]);
//            newReminder.setReminderTime(Integer.parseInt(reminderComponents[1]),Integer.parseInt(reminderComponents[2]));
            usersHours.add(Integer.parseInt(reminderComponents[1]));
        }
        return usersHours;
    }
    public List<Integer> translateReminderMinutes(List<String> reminders) {
        ArrayList<Integer> usersMinutes = new ArrayList<Integer>();
        for (String reminder : reminders) {
            String[] reminderComponents = reminder.split("\\?");
            usersMinutes.add(Integer.parseInt(reminderComponents[2]));
        }
        return usersMinutes;
    }
    public List<String> translateReminderTitles(List<String> reminders) {
        ArrayList<String> usersReminders = new ArrayList<String>();
        for (String reminder : reminders) {
            String[] reminderComponents = reminder.split("\\?");
            usersReminders.add(reminderComponents[0]);
        }
        return usersReminders;
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