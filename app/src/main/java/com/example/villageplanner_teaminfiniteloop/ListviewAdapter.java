package com.example.villageplanner_teaminfiniteloop;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ListviewAdapter extends BaseAdapter {

    private Context context;
    private List<Integer> hourList;
    private List<Integer> minuteList;
    private List<String> reminderList;
    Button submitButton;
    String userEmail = "test1@gmail.com";


    LayoutInflater mInflater;
    public ListviewAdapter(Context context, List hourList, List minuteList, List reminderList){
        this.context = context;
        this.hourList = hourList;
        this.minuteList = minuteList;
        this.reminderList = reminderList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return hourList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public ArrayList<String> combineText(List<Integer> hourList, List<Integer> minuteList, List reminderList)
    {
        ArrayList<String> remindersInString = new ArrayList<String>();
        for(Integer i=0;i<reminderList.size();i++)
        {
            remindersInString.add(reminderList.get(0) +"?"+String.valueOf(hourList.get(1))+String.valueOf(minuteList.get(1)));
        }
        return remindersInString;
    }

    public void onSubmit() {
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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        final ViewHolder holderHours;
        final ViewHolder holderMinutes;
        final ViewHolder holderReminders;
        convertView=null;
        if (convertView == null) {
            holderHours = new ViewHolder();
            holderMinutes = new ViewHolder();
            holderReminders = new ViewHolder();

            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listview_adapter, null);
            holderHours.caption = (EditText) convertView
                    .findViewById(R.id.editHours);
            holderMinutes.caption = (EditText) convertView
                    .findViewById(R.id.editMinutes);
            holderReminders.caption = (EditText) convertView
                    .findViewById(R.id.editReminderName);

            holderHours.caption.setTag(position);
            holderHours.caption.setText(hourList.get(position).toString());
            holderMinutes.caption.setTag(position);
            holderMinutes.caption.setText(minuteList.get(position).toString());
            holderReminders.caption.setTag(position);
            holderReminders.caption.setText(reminderList.get(position).toString());

            convertView.setTag(holderHours);
            convertView.setTag(holderMinutes);
            convertView.setTag(holderReminders);
        }else {
            holderHours = (ViewHolder) convertView.getTag();
            holderMinutes = (ViewHolder) convertView.getTag();
            holderReminders = (ViewHolder) convertView.getTag();

        }
        int hour_tag_position=(Integer) holderHours.caption.getTag();
        holderHours.caption.setId(hour_tag_position);
        int minute_tag_position=(Integer) holderMinutes.caption.getTag();
        holderMinutes.caption.setId(minute_tag_position);
        int reminder_tag_position=(Integer) holderReminders.caption.getTag();
        holderReminders.caption.setId(minute_tag_position);

        holderHours.caption.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                final int position2 = holderHours.caption.getId();
                final EditText Caption = (EditText) holderHours.caption;
                if(Caption.getText().toString().length()>0){
                    hourList.set(position2,Integer.parseInt(Caption.getText().toString()));

                }else{
                    Toast.makeText(context, "Please enter some value", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });
        holderMinutes.caption.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                final int position2 = holderMinutes.caption.getId();
                final EditText Caption = (EditText) holderMinutes.caption;
                if(Caption.getText().toString().length()>0){
                    minuteList.set(position2,Integer.parseInt(Caption.getText().toString()));
                }else{
                    Toast.makeText(context, "Please enter some value", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });
        holderReminders.caption.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                final int position2 = holderReminders.caption.getId();
                final EditText Caption = (EditText) holderReminders.caption;
                if(Caption.getText().toString().length()>0){
                    reminderList.set(position2,(Caption.getText().toString()));
                }else{
                    Toast.makeText(context, "Please enter some value", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        return convertView;
    }


}

class ViewHolder {
    EditText caption;
}
