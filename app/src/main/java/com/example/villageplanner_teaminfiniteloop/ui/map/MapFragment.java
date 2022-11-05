package com.example.villageplanner_teaminfiniteloop.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView mapView;
    private GoogleMap googleMap;
    private FragmentMapBinding binding;
    private ArrayList<Restaurant> restaurants = Queue.restaurants;
    private LatLng currentLocation = new LatLng(User.currentLocation.getLatitude(), User.currentLocation.getLongitude());
    private LatLng destination;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) view.findViewById(R.id.mapView);
        initGoogleMap(savedInstanceState);

        if ( getArguments().getDouble("destinationLat") != 0.0) {    //returned from Get Direction
            double lat = getArguments().getDouble("destinationLat");
            double lon = getArguments().getDouble("destinationLong");
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
                    .title(r.getName()));
            newMarker.setTag(r);
        }

        googleMap.setOnInfoWindowClickListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(34.0256, -118.2850), 17.7f));
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
        Queue.resCoordinate = r.getLocation();
        MapFragment.this.startActivity(myIntent);
    }

    // MARK: Handle draw lines
    private class DownloadTask extends AsyncTask {

//        @Override
//        protected String doInBackground(String... url) {
//
//            String data = "";
//
//            try {
//                data = downloadUrl(url[0]);
//            } catch (Exception e) {
//                Log.d("Background Task", e.toString());
//            }
//            return data;
//        }

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

//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            ParserTask parserTask = new ParserTask();
//
//
//            parserTask.execute(result);
//
//        }
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
                lineOptions.color(Color.RED);
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
        String mode = "mode=walking";

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