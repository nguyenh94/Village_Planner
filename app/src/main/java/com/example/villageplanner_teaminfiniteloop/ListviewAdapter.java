package com.example.villageplanner_teaminfiniteloop;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private List<String> departureList;
    private List<String> arrivalList;
    private List<String> reminderList;
    private List<String> queueAndTravelTime;

    Button submitButton = null;
    String userEmail = "test1@gmail.com";

    LayoutInflater mInflater;
    public ListviewAdapter(Context context, List departureList, List arrivalList, List reminderList, List queueAndTravelTime){
        this.context = context;
        this.departureList = departureList;
        this.arrivalList = arrivalList;
        this.reminderList = reminderList;
        this.queueAndTravelTime = queueAndTravelTime;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return departureList.size();
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

    public ArrayList<String> combineText(List<String> arrivalList, List<String> departureList, List<String> reminderList, List<String> queueAndTravelTime)
    {
        ArrayList<String> remindersInString = new ArrayList<String>();
        String departureFinalHoursString = "";
        String departureFinalMinutesString = "";
        try{
            for(Integer i=0;i<reminderList.size();i++)
            {
                String[] arrivalComponents = reminderList.get(i).split(":");
                Integer arrivalInMinutes = 60*Integer.parseInt(arrivalComponents[0]) + Integer.parseInt(arrivalComponents[1]);
                Integer wait = Integer.parseInt(queueAndTravelTime.get(i));
                Integer departureMinutes = arrivalInMinutes - wait;
                Integer departureFinalHours = departureMinutes % 60;
                Integer departureFinalMinutes = departureMinutes / 60;
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
                        departureFinalHoursString +":" + departureFinalMinutesString + "?"+queueAndTravelTime);
            }
            return remindersInString;
        }
        catch (Exception e) {
            return remindersInString;
        }
    }

    public void onSubmit() {
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
                    } else {  // if email doesn't exists how are they logged in

                    }
                }
            }
        });
        Toast.makeText(context, "Reminder Edit Successful.", Toast.LENGTH_LONG).show();
    }

    public boolean userInputValid(String userInput) {
        if(userInput.length() == 5 && userInput.charAt(2)==':')
        {
            return true;
        }
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        final ViewHolder holderArrival;
        TextView holderDeparture = null;
        final ViewHolder holderReminders;
        convertView=null;

        if (convertView == null) {
            holderArrival = new ViewHolder();
//            holderDeparture = new ViewHolder();
            holderReminders = new ViewHolder();

            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listview_adapter, null);
            holderArrival.caption = (EditText) convertView
                    .findViewById(R.id.editArrival);
            holderDeparture = (TextView) convertView
                    .findViewById(R.id.editDeparture);
            holderReminders.caption = (EditText) convertView
                    .findViewById(R.id.editReminderName);

            holderArrival.caption.setTag(position);
            holderArrival.caption.setText(String.valueOf(arrivalList.get(position)));
            holderDeparture.setTag(position);
            holderDeparture.setText(String.valueOf(departureList.get(position)));
            holderReminders.caption.setTag(position);
            holderReminders.caption.setText(String.valueOf(reminderList.get(position)));

            convertView.setTag(holderArrival);
            convertView.setTag(holderDeparture);
            convertView.setTag(holderReminders);
        }else {
            holderArrival = (ViewHolder) convertView.getTag();
//            holderDeparture = (ViewHolder) convertView.getTag();
            holderReminders = (ViewHolder) convertView.getTag();

        }
        int holderArrival_tag_position=(Integer) holderArrival.caption.getTag();
        holderArrival.caption.setId(holderArrival_tag_position);
        int holderDeparture_tag_position=(Integer) holderDeparture.getTag();
        holderDeparture.setId(holderDeparture_tag_position);
        int reminder_tag_position=(Integer) holderReminders.caption.getTag();
        holderReminders.caption.setId(reminder_tag_position);

        holderArrival.caption.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                final int position2 = holderArrival.caption.getId();
                final EditText Caption = (EditText) holderArrival.caption;
                String userInput = Caption.getText().toString();
                if(userInput.length()>0 && userInputValid(userInput)){
                    arrivalList.set(position2,Caption.getText().toString());
                }else{
                    Toast.makeText(context, "Please enter a value in the same format", Toast.LENGTH_SHORT).show();
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
//        holderDeparture.caption.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                final int position2 = holderDeparture.caption.getId();
//                final EditText Caption = (EditText) holderDeparture.caption;
//                if(Caption.getText().toString().length()>0){
//                    arrivalList.set(position2,Caption.getText().toString());
//                }else{
//                    Toast.makeText(context, "Please enter some value", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//
//        });
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
