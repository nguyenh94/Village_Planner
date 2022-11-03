package com.example.villageplanner_teaminfiniteloop.ui.me;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.villageplanner_teaminfiniteloop.Reminder;
import com.example.villageplanner_teaminfiniteloop.User;
import com.example.villageplanner_teaminfiniteloop.databinding.FragmentMeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MeFragment extends Fragment {

    private FragmentMeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MeViewModel notificationsViewModel =
                new ViewModelProvider(this).get(MeViewModel.class);

        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userEmail = "test1@gmail.com";
//        final List<String>[] usersReminders = new List[0];
        final ArrayList<String>[] usersReminders = new ArrayList[]{new ArrayList<String>()};
        DocumentReference docRefSecond = db.collection("users").document(userEmail);
        docRefSecond.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {  // if email exists
                        User user = document.toObject(User.class);
                        translateReminders(user.getReminders());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}