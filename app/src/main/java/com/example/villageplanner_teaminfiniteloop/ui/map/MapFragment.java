package com.example.villageplanner_teaminfiniteloop.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.villageplanner_teaminfiniteloop.DirectionsJSONParser;
import com.example.villageplanner_teaminfiniteloop.MyLocation;
import com.example.villageplanner_teaminfiniteloop.Queue;
import com.example.villageplanner_teaminfiniteloop.R;
import com.example.villageplanner_teaminfiniteloop.Restaurant;
import com.example.villageplanner_teaminfiniteloop.RestaurantDetail;
import com.example.villageplanner_teaminfiniteloop.User;
import com.example.villageplanner_teaminfiniteloop.databinding.FragmentMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView mapView;
    private GoogleMap googleMap;
    private FragmentMapBinding binding;
    private ArrayList<Restaurant> restaurants = Queue.restaurants;
    private LatLng currentLocation = new LatLng(User.currentLocation.getLatitude(), User.currentLocation.getLongitude());
    private LatLng destination;
    private String travelMode;
    MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
        @Override
        public void gotLocation(Location location){
            // store the user's location in the database
            User.currentLocation = location;
            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) view.findViewById(R.id.mapView);
        initGoogleMap(savedInstanceState);

        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(getActivity(), locationResult);

        if ( getArguments().getDouble("destinationLat") != 0.0) {    //returned from Get Direction
            double lat = getArguments().getDouble("destinationLat");
            double lon = getArguments().getDouble("destinationLong");
            travelMode = getArguments().getString("travelMode");
            System.out.println("Travel Mode is " + travelMode);
            destination = new LatLng(lat, lon);

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(currentLocation, destination);
            DownloadTask downloadTask = new DownloadTask();
            System.out.println(url);
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }

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
                    .title(r.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            newMarker.setTag(r);
        }

        googleMap.setOnInfoWindowClickListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(34.0256, -118.2850), 17.7f));
        if (!checkAtLA()) {
            showAlert();
        }

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private boolean checkAtLA(){
        //Detect city
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(getActivity(), locationResult);

        Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault()); //it is Geocoder
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);
            String city = address.get(0).getLocality();
            if (!city.equals("Los Angeles")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        // Set the message show for the Alert time
        alertBuilder.setMessage("Village Planner is built for Trojans at USC. Please go to Los Angeles to use Village Planner!");

        // Set Alert Title
        alertBuilder.setTitle("Ops! You are not at Los Angeles!");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        alertBuilder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        alertBuilder.setPositiveButton("Ok", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = alertBuilder.create();
        // Show the Alert Dialog box
        alertDialog.show();
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
        if (!checkAtLA()) {
            showAlert();
            return;
        }
        Queue queueTime = new Queue();
        Restaurant r = (Restaurant) marker.getTag();
        // calculate the queue time for this restaurant
        Queue.resCoordinate = r.getLocation();
        Queue.allUserLocation.clear();
        Queue.userInRadius = 0;
        // get the user's location from the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for(QueryDocumentSnapshot document: task.getResult()) {
                        try {
                            User user = document.toObject(User.class);
                            String userLocation = user.getLocation();
//                            System.out.println("user location line 77 queue.java is " + userLocation);
                            Queue.allUserLocation.add(userLocation);
//                            System.out.println("line79 allUserLocation is " + Queue.allUserLocation);
                        } catch (Exception e) {
//                            System.out.println("line81");
                            System.out.println(e);
                        }
                    }
                    System.out.println("allUserLocation size is " + Queue.allUserLocation.size());
//                    calculateQueueTimeCallBack();
                    // resCoordinate is retrieved in MapFragment when user chooses a restaurant
                    Location resLoc = Queue.toLocation(Queue.resCoordinate);
                    double resLat = resLoc.getLatitude();
                    double resLong = resLoc.getLongitude();

                    // go through all the locations and if it's within bound,
                    for (int i=0; i<Queue.allUserLocation.size(); i++) {
                        // if within radius of .0000# (up to 5 decimal decisions) for
                        // both long and lat then in radius
//                        System.out.println(Queue.allUserLocation);
                        Location userLoc = Queue.toLocation(Queue.allUserLocation.get(i));
                        double userLat = userLoc.getLatitude();
                        double userLong = userLoc.getLongitude();
                        double userToResDistance = Queue.calculateDistance(resLat, resLong, userLat, userLong);
                        if (userToResDistance < 0.0094697) {     // radius = 50 feet = 0.0094697 miles
                            Queue.userInRadius++;    // count how many users are in the area at the time --> n
                        }
                    }

                    // each user will take 2 minutes
                    Queue.queueTime = Queue.userInRadius;
                    Intent myIntent = new Intent(getActivity(), RestaurantDetail.class);
                    myIntent.putExtra("name", r.getName());
                    myIntent.putExtra("location", r.getLocation());
                    MapFragment.this.startActivity(myIntent);
                }
            }
        });
    }

    // MARK: Handle draw lines
    private class DownloadTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            String data = "";

            try {
                data = downloadUrl((String) objects[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ParserTask parserTask = new ParserTask();
            parserTask.execute((String) o);
        }

    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject); //List<List<HashMap<String,String>>>
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap point = path.get(j);

                    double lat = Double.parseDouble((String) point.get("lat"));
                    double lng = Double.parseDouble((String) point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.rgb(196,30,58));
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            googleMap.addPolyline(lineOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.latitude, currentLocation.longitude), 17.7f));
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode="+travelMode;

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + "key=AIzaSyAk2zt_F2hY2v76do6CwRUvjA_RZhHpM1Q";

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}