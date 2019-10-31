package com.example.cmridenewishlistresturaunt;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.cmridenewishlistresturaunt.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    //Variables we're going to use to create our map later.
    private MapView mapView;
    private GoogleMap map;
    private Location mLocation;
    private double latitude;
    private double longitude;
    private LocationManager mLocationManager;
    final int REQUEST_LOCATION = 1;

    Bundle info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = savedInstanceState;
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        setupAdapter(sectionsPagerAdapter);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        mapView = findViewById(R.id.mapView);
        if(mapView !=null) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }


    }

    private void centerMap() {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
            map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("You"));
    }

    private void setupAdapter(SectionsPagerAdapter sp){
        sp.addFragment(new MapPoiFrag(), "Map");
        sp.addFragment(new favoritesList(), "Favorites");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Map Ready", "Alright");
        map = googleMap;
        checkPermission();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                centerMap();
            }
        });
    }

    public void addToFavorites(View view) {
        SharedPreferences pref = this.getSharedPreferences("Current", this.MODE_PRIVATE);
        addToPreferences(pref.getString("Current","None"));
        Button btn = this.findViewById(R.id.btnFav);
        btn.setText("Favorited");


    }
    public void addToPreferences(String s){
        SharedPreferences pref = this.getSharedPreferences("Favorites", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(s,s);
        editor.commit();
    }
    public void checkPermission() {
        // Checks if the permission to use FINE_LOCATION hasn't already been granted. If it hasn't,
        // then the app will launch a dailogue box that will request permission to use the User's
        // location.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            // In the case that the app had already obtained permission to use the user's location
            // The all will use the location manager to get the user's current latitude and
            // longitude.
            mLocation = mLocationManager.getLastKnownLocation(mLocationManager.GPS_PROVIDER);
            longitude = mLocation.getLongitude();
            latitude = mLocation.getLatitude();
            centerMap();
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] perms, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, perms, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                try {
                    mLocation = mLocationManager.getLastKnownLocation(mLocationManager.GPS_PROVIDER);
                    longitude = mLocation.getLongitude();
                    latitude = mLocation.getLatitude();
                } catch (SecurityException e) {
                }
                centerMap();
            } else {
                latitude = 0;
                longitude = 0;
            }
        }
    }
}