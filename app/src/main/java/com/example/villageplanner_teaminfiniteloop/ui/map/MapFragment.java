package com.example.villageplanner_teaminfiniteloop.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.villageplanner_teaminfiniteloop.Queue;
import com.example.villageplanner_teaminfiniteloop.R;
import com.example.villageplanner_teaminfiniteloop.Restaurant;
import com.example.villageplanner_teaminfiniteloop.RestaurantDetail;
import com.example.villageplanner_teaminfiniteloop.databinding.FragmentMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView mapView;
    private GoogleMap googleMap;
    private FragmentMapBinding binding;
    private ArrayList<Restaurant> restaurants = Queue.restaurants;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) view.findViewById(R.id.mapView);
        initGoogleMap(savedInstanceState);
        return view;
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        // draw map routes: https://www.digitalocean.com/community/tutorials/android-google-map-drawing-route-two-points
        googleMap.setMinZoomPreference(14.0f);
        googleMap.setMaxZoomPreference(20.0f);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        for (Restaurant r: restaurants) {
            String rLocation = r.getLocation();
            ArrayList<Double> arr = Queue.stringToDouble(rLocation);
            Double la = arr.get(0);
            Double lo = arr.get(1);
            LatLng latLng = new LatLng(la, lo);
            Marker newMarker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(r.getName()));
            newMarker.setTag(r);
        }

        googleMap.setOnInfoWindowClickListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(34.0256, -118.2850)));
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Restaurant r = (Restaurant) marker.getTag();
        Intent myIntent = new Intent(getActivity(), RestaurantDetail.class);
        myIntent.putExtra("name", r.getName());
        myIntent.putExtra("location", r.getLocation());
        MapFragment.this.startActivity(myIntent);
    }
}