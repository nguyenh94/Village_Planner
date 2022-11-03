package com.example.villageplanner_teaminfiniteloop;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class ListviewAdapter extends BaseAdapter {

    private Context context;
    private List list;

    LayoutInflater mInflater;
    public ListviewAdapter(Context context,List list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
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

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        final ViewHolder holderHours;
        convertView=null;
        if (convertView == null) {
            holderHours = new ViewHolder();
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listview_adapter, null);
            holderHours.caption = (EditText) convertView
                    .findViewById(R.id.number);
            holderHours.caption.setTag(position);
            holderHours.caption.setText(list.get(position).toString());
            convertView.setTag(holderHours);
        }else {
            holderHours = (ViewHolder) convertView.getTag();
        }
        int tag_position=(Integer) holderHours.caption.getTag();
        holderHours.caption.setId(tag_position);

        holderHours.caption.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                final int position2 = holderHours.caption.getId();
                final EditText Caption = (EditText) holderHours.caption;
                if(Caption.getText().toString().length()>0){
                    list.set(position2,Integer.parseInt(Caption.getText().toString()));
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
