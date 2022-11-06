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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListviewAdapter extends BaseAdapter {

    private Context context;
    private List<Integer> departureList;
    private List<String> arrivalList;
    private List<String> reminderList;
    Button submitButton;
    String userEmail = "test1@gmail.com";


    LayoutInflater mInflater;
    public ListviewAdapter(Context context, List departureList, List arrivalList, List reminderList){
        this.context = context;
        this.departureList = departureList;
        this.arrivalList = arrivalList;
        this.reminderList = reminderList;
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

//    public ArrayList<String> combineText(List<Integer> departureList, List<Integer> arrivalList, List reminderList)
//    {
//        ArrayList<String> remindersInString = new ArrayList<String>();
//        for(Integer i=0;i<reminderList.size();i++)
//        {
//            remindersInString.add(reminderList.get(i) +"?"+String.valueOf(departureList.get(i)) +"?" + String.valueOf(arrivalList.get(i)));
//        }
//        return remindersInString;
//    }

//    public void onSubmit() {
//        ArrayList<String> newReminders = combineText(hourList, minuteList, reminderList);
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference docRef = db.collection("users").document(userEmail);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {  // if email exists
//                        docRef.update("reminders", newReminders);
//                    } else {  // if email doesn't exists how are they logged in
//
//                    }
//                }
//            }
//        });
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        TextView holderArrival = null;
        TextView holderDeparture = null;
        TextView holderReminders = null;
        convertView=null;
        if (convertView == null) {
//            holderArrival = new ViewHolder();
//            holderDeparture = new ViewHolder();
//            holderReminders = new ViewHolder();

            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listview_adapter, null);
            holderArrival = (TextView) convertView
                    .findViewById(R.id.editArrival);
            holderDeparture = (TextView) convertView
                    .findViewById(R.id.editDeparture);
            holderReminders = (TextView) convertView
                    .findViewById(R.id.editReminderName);

            holderArrival.setTag(position);
            holderArrival.setText(String.valueOf(arrivalList.get(position)));
            holderDeparture.setTag(position);
            holderDeparture.setText(String.valueOf(departureList.get(position)));
            holderReminders.setTag(position);
            holderReminders.setText(String.valueOf(reminderList.get(position)));

            convertView.setTag(holderArrival);
            convertView.setTag(holderDeparture);
            convertView.setTag(holderReminders);
        }else {
//            holderArrival = (ViewHolder) convertView.getTag();
//            holderDeparture = (ViewHolder) convertView.getTag();
//            holderReminders = (ViewHolder) convertView.getTag();

        }
        int holderArrival_tag_position=(Integer) holderArrival.getTag();
        holderArrival.setId(holderArrival_tag_position);
        int holderDeparture_tag_position=(Integer) holderDeparture.getTag();
        holderDeparture.setId(holderDeparture_tag_position);
        int reminder_tag_position=(Integer) holderReminders.getTag();
        holderReminders.setId(reminder_tag_position);

//        holderArrival.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                final int position2 = holderArrival.caption.getId();
//                final EditText Caption = (EditText) holderArrival.caption;
//                if(Caption.getText().toString().length()>0){
//                    departureList.set(position2,Integer.parseInt(Caption.getText().toString()));
//
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
//        holderReminders.caption.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                final int position2 = holderReminders.caption.getId();
//                final EditText Caption = (EditText) holderReminders.caption;
//                if(Caption.getText().toString().length()>0){
//                    reminderList.set(position2,(Caption.getText().toString()));
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

        return convertView;
    }


}

class ViewHolder {
    EditText caption;
}
