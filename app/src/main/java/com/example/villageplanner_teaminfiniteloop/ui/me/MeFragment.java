package com.example.villageplanner_teaminfiniteloop.ui.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.villageplanner_teaminfiniteloop.R;
import com.example.villageplanner_teaminfiniteloop.User;
import com.example.villageplanner_teaminfiniteloop.databinding.FragmentMeBinding;

import org.w3c.dom.Text;

public class MeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        View view = inflater.inflate(R.layout.fragment_me, null);
        TextView userNameTextView = (TextView) view.findViewById(R.id.userName);
        TextView userEmailTextView = (TextView) view.findViewById(R.id.userEmail);

        userNameTextView.setText(User.currentUserName);
        userEmailTextView.setText(User.currentUserEmail);
        return view;
    }
}