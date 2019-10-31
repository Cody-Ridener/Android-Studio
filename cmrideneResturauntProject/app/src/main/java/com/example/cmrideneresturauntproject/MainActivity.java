package com.example.cmrideneresturauntproject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cmrideneresturauntproject.ui.main.SectionsPagerAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import static java.security.AccessController.getContext;

public class MainActivity extends FragmentActivity implements GoogleMap.OnPoiClickListener {
    boolean mLocationPermissionGranted;
    Context thisCon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thisCon = this;
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        getLocationPermission();

    }

    //@Override
    //public void onMapReady(GoogleMap googleMap) {
        //Sets the marker to the user's current location on the map.
        //googleMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("Marker"));
        //googleMap.setOnPoiClickListener((GoogleMap.OnPoiClickListener) this);
    //}
    // A method that checks if the user has provided permission to use their location.
    private boolean getLocationPermission(){
        //Checks if the app has permission to access the device's location. If it does it sets the
        // permission value to true. If it doesn't then it will prompt the user
        // for permission.
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //generateMap();
            return mLocationPermissionGranted;

        }else{


            //Calls the request permissions command to prompt the user for permission to use
            //their fine location.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
             return mLocationPermissionGranted;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 100:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Sets the location permission to true then generates the google map.
                    mLocationPermissionGranted = true;
                    //generateMap();
                    break;
                }else{
                    // Displays a message saying the app needs location data to run then closes the
                    //  app if permission is denied.

                    // Runs on a UI thread so that the toast will display properly.
                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(thisCon, "App needs location data to find resturaunts" +
                                    " in your area. App Closing", Toast.LENGTH_LONG).show();
                        }
                    });
                    // Sleeps long enough for the user to read the toast before the app closes itself.
                    try {
                        Thread.sleep(3500);
                    } catch(InterruptedException e){}
                    finish();
                    System.exit(0);
                    break;

                }

            }
        }

    }
    //public void generateMap(){
        //Creates an instance of the map fragment so that the application can display the map.
       // MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
       // mapFragment.getMapAsync(this);

   // }


    @Override
    public void onPoiClick(PointOfInterest poi) {

    }
}
