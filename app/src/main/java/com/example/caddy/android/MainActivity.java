package com.example.caddy.android;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.instantapps.ActivityCompat;
import com.google.android.gms.instantapps.PackageManagerCompat;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Permission;

public class MainActivity extends Activity {

    // Constant for defining latitude and longitude
    static final LatLng CaddyPos = new LatLng(40, -79);

    // GoogleMap class
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // verify we can interact with the Google Map
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
}