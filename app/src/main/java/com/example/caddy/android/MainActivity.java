package com.example.caddy.android;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.instantapps.ActivityCompat;
import com.google.android.gms.instantapps.PackageManagerCompat;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;

public class MainActivity extends Activity {

    EditText addressEditText, finalAddressEditText;

    // Constant for defining latitude and longitude
    static final LatLng CaddyPos = new LatLng(40, -79);

    // Stores latitude and longitude data for addresses
    LatLng addressPos, finalAddressPos;

    // Used to place Marker on the map
    Marker addressMarker;
    // Used to utilize map capabilities
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize EditTexts
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        finalAddressEditText = (EditText) findViewById(R.id.finalAddressEditText);

        // Initialize Google Map
        try {
            if (googleMap == null) {
                ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        // Show a satellite map with roads
                        /* MAP_TYPE_NORMAL: Basic map with roads.
                        MAP_TYPE_SATELLITE: Satellite view with roads.
                        MAP_TYPE_TERRAIN: Terrain view without roads.
                         */
                        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                        // Place a dot on current location
                        // googleMap.setMyLocationEnabled(true);

                        // Turns traffic layer on
                        googleMap.setTrafficEnabled(true);

                        // Enables indoor maps
                        googleMap.setIndoorEnabled(true);

                        // Turns on 3D buildings
                        googleMap.setBuildingsEnabled(true);

                        // Show Zoom buttons
                        googleMap.getUiSettings().setZoomControlsEnabled(true);

                        // Create a marker in the map  at a given position with a title
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(CaddyPos).title("Hello"));
                    }
                });
            }
            // Show a satellite map with roads
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // Called when getAddressButton is clicked
    public void showAddressMarker(View view) {

        // Get the street address entered
        String newAddress = addressEditText.getText().toString();

        if (newAddress != null) {

            // Call for the AsyncTask to place a marker
            new PlaceAMarker().execute(newAddress);
        }
    }

    // Called when getFirectionsButton is clicked
    public void getDirections(View view) {

        // Get the start and ending address
        String startingAddress = addressEditText.getText().toString();
        String finalAddress = finalAddressEditText.getText().toString();

        // Verify that they aren't empty
        if ((startingAddress.equals("")) || (finalAddress.equals("")) ) {
            Toast.makeText(this, "Enter a Starting & Ending Address", Toast.LENGTH_SHORT).show();
        } else {

            // Get the GetDirections AsyncTask to call for the directions
            new GetDirections().execute(startingAddress, finalAddress);
        }
    }

    class PlaceAMarker extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            // Get the 1st address passed
            String startAddress = params[0];

            // Replace the spaces with %20
            startAddress = startAddress.replaceAll(" ", "%20");

            // Call for the latitude and longitude and pass in that we don't want directions
            getLatLong(startAddress, false);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Draw the marker on the screen
            addressMarker = googleMap.addMarker(new MarkerOptions().position(addressPos).title("Address"));
        }
    }

    // The AsyncTask that gets directions
    class GetDirections extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            // Get the starting address
            String startAddress = params[0];

            // Replace the spaces with %20
            startAddress = startAddress.replaceAll(" ", "%20");

            // Get the lat and long for our address
            getLatLong(startAddress, false);

            // Get the destination address
            String endingAddress = params[1];

            // Replace the spaces with %20
            endingAddress = endingAddress.replaceAll(" ", "%20");

            // Get lat and long for the destination address and pass true
            getLatLong(endingAddress, true);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Create the URL for Google Maps to get the directions
            String geoUriString = "http://maps.google.com/maps?saddr=" + addressPos.latitude + "," + addressPos.longitude + "&daddr=" + finalAddressPos.latitude + "," + finalAddressPos.longitude;

            // Call for Google Maps to open
            Intent mapCall = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUriString));

            startActivity(mapCall);
        }
    }

    protected void getLatLong(String address, boolean setDestination) {

        // Define the uri that is used to get lat and long for our address
        String uri = "http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false";

        // Use the get method to retrieve our data
        HttpGet httpGet = new HttpGet(uri);

        // Acts as the client which executes HTTP requests
        HttpClient client = new DefaultHttpClient();

        // Receives the response from our HTTP request
        HttpResponse response;

        // Will hold the data received
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // Get the response of our query
            response = client.execute(httpGet);

            // Receive the entity information sent with teh HTTP message
            HttpEntity entity = response.getEntity();

            // Holds the sent bytes of data
            InputStream stream = entity.getContent();
            int byteData;

            // Continue reading data while available
            while ((byteData = stream.read()) != -1) {
                stringBuilder.append((char) byteData);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        double lat = 0.0, lng = 0.0;

        // Holds key value mappings
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            // Get the returned latitude and longitude
            lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

            lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");

            // Change the lat and long depending on if we want to set the starting or ending destination
            if (setDestination) {
                finalAddressPos = new LatLng(lat, lng);
            } else {
                addressPos = new LatLng(lat, lng);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}