package com.example.villageplanner_teaminfiniteloop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.villageplanner_teaminfiniteloop.ui.map.MapFragment;
import com.example.villageplanner_teaminfiniteloop.ui.me.MeFragment;
import com.example.villageplanner_teaminfiniteloop.ui.reminders.RemindersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.villageplanner_teaminfiniteloop.databinding.ActivityTabBarBinding;
import com.google.android.material.navigation.NavigationBarView;

public class TabBarActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

    private ActivityTabBarBinding binding;
    double lat = 0.0;
    double lon = 0.0;
    String mode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTabBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);

        Intent intent = getIntent();
        lat = intent.getDoubleExtra("destinationLat", 0.0);
        lon = intent.getDoubleExtra("destinationLong", 0.0);
        mode = intent.getStringExtra("travelMode");
        Bundle bundle = new Bundle();
        bundle.putDouble("destinationLat", lat);
        bundle.putDouble("destinationLong", lon);
        bundle.putString("travelMode", mode);
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(bundle);

        //loading the default fragment
        loadFragment(mapFragment);

        //getting bottom navigation view and attaching the listener
        navView.setOnItemSelectedListener(this);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_tab_bar);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                Bundle bundle = new Bundle();
                bundle.putDouble("destinationLat", lat);
                bundle.putDouble("destinationLong", lon);
                bundle.putString("travelMode", mode);
                MapFragment mapFragment = new MapFragment();
                mapFragment.setArguments(bundle);
                fragment = mapFragment;
                break;

            case R.id.navigation_dashboard:
                fragment = new RemindersFragment();
                break;

            case R.id.navigation_notifications:
                fragment = new MeFragment();
                break;
        }

        return loadFragment(fragment);
    }
}