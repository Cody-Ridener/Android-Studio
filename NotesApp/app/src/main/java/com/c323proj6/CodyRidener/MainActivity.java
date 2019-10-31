package com.c323proj6.CodyRidener;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.location.LocationListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class MainActivity extends AppCompatActivity implements LocationListener{
    // Global variables that will be useful in later parts of the code.
    private Location mLocation;
    private Geocoder geo;
    private LocationManager mLocationManager;
    final int REQUEST_LOCATION = 1;
    final int REQUEST_WRITE = 2;
    final int REQUEST_CAMERA = 3;
    private double latitude;
    private double longitude;
    private List<Address> addresses;
    private TextView currAddress;
    private TextView textEdit;
    private sqLiteHandler sql;
    private String currColor = "#F44336";
    private Boolean writePerms;
    private Boolean cameraPerms = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Instantiating variables for the views withing the main activity as well as setting the title
        // for the toolbar as my name.
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cody Ridener");
        currAddress = findViewById(R.id.location);
        textEdit = findViewById(R.id.editText);
        FloatingActionButton fab = findViewById(R.id.fab);
        // Instantiates the sqliteHandler class that will be used for accessing and writing to the
        // sqLite database
        sql = new sqLiteHandler(this);
        // Creates a location manager that will be used to get the user's location assuming location
        // permissions are granted.
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Set's the onClickListener for the floating action button on the bottom right of the
        // main activity. It will activate a function to segue to the list view activity.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
        // Launches the check permissions command that will run most of the rest of the code.
        checkPermission();
    }

    // The function assigned to the FAB that will switch to the listActivity activity
    private void launchActivity() {
        // Declares a new intent before switching to the new activity.
        Intent intent = new Intent(this, listActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // The check permissions function that checks if the user has allowed access to their location.
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
            // This is a basic geocoder that we will use to get the user's street address, more
            // detailed information can be obtained by using Google's GeocoderAPI such as the
            // the name of any nearby buildings to the user. This geocoder merely returns the user's
            // City, street, zip, and their country.
            geo = new Geocoder(this, Locale.getDefault());
            // Try catch block to catch the IOexception that could be thrown by the getFromLocation
            // command.
            try {
                addresses = geo.getFromLocation(latitude, longitude, 1);
                String street = addresses.get(0).getAddressLine(0);
                currAddress.setText(street);
            }catch(IOException e){

            }
        }
    }
    @Override
    // This is the function that is called after the earlier Request permission call has been either
    // accepted or declined by the user. If the user granted the permission to use their location
    // then the app will use the same Geocoder class as used above to get the user's current Address
    public void onRequestPermissionsResult(int requestCode, String[] perms, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,perms,grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                try {
                    mLocation = mLocationManager.getLastKnownLocation(mLocationManager.GPS_PROVIDER);
                    longitude = mLocation.getLongitude();
                    latitude = mLocation.getLatitude();
                    geo = new Geocoder(this, Locale.getDefault());
                    addresses = geo.getFromLocation(latitude, longitude, 1);
                    String street = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    currAddress.setText(street);
                }catch (SecurityException | IOException e){

                }
            }else{
                // If the user declined the use of their location the program will exit.
                finish();
                System.exit(0);

            }
        }else if(requestCode == REQUEST_CAMERA){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraPerms = true;
                // If the user allowed for the app to use the camera the program will automatically
                // check if the app has permissions to write to external storage.
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
                }else{
                    // If the user can write to external storage already then the cameraActivity is
                    // launched.
                    writePerms = true;
                    startCameraActivity();
                }
            }
        }else if(requestCode == REQUEST_WRITE){
            //Starts the camera activity if the app has the ability to write to files.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCameraActivity();
            }
        }



    }


    @Override
    public void onLocationChanged(Location location) {
        // This is a function that will update the user's street address if they change their
        // Location. It uses the same geocoder class as the checkPermissions functtion.
        longitude = mLocation.getLongitude();
        latitude = mLocation.getLatitude();
        geo = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geo.getFromLocation(latitude, longitude, 1);
            String street = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            currAddress.setText(street);
        }
        catch(IOException e){}
    }
    // A list of set functions to se the text background color to either Red, blue, green, yellow,
    // or orange when the user clicks on the corresponding buttons. The currColor variable is also
    // updated so it can be used to store color information in the SQL database when the note is
    // added to it later.
    public void setRed(View view) {
        textEdit.setBackgroundColor(Color.parseColor("#F44336"));
        currColor = "#F44336";
    }
    public void setGreen(View view) {
        textEdit.setBackgroundColor(Color.parseColor("#009688"));
        currColor = "#009688";
    }

    public void setOrange(View view) {
        textEdit.setBackgroundColor(Color.parseColor("#FF5722"));
        currColor = "#FF5722";
    }
    public void setBlue(View view) {
        textEdit.setBackgroundColor(Color.parseColor("#2196F3"));
        currColor = "#2196F3";
    }
    public void setYellow(View view) {
        textEdit.setBackgroundColor(Color.parseColor("#FFEB3B"));
        currColor = "#FFEB3B";
    }
    // This is the function that is called when the user presses the "Add to Notes" button in the
    // content_main.xml layout. What this does is uses the sqLiteHandler that we defined earlier in
    // the code to add a new row to the database that contains the user's current address, the
    // current content of the note, the color of the note, and also the date. This function will
    // also display a Toast message informing the user that the note has been saved.
    public void addToDB(View view) {
        String content = textEdit.getText().toString();
        textEdit.setText("");
        String address = currAddress.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String date = format.format(new Date());
        Toast.makeText(this, "Note Added", Toast.LENGTH_SHORT).show();
        sql.record(content, address,date, currColor);
    }
    // OnClick action for the camFab floating action button. It will check if the proper permissions
    // have been given before transitioning to a camera view.
    public void checkCamera(View view) {
        // Checks if the app has access to the camera.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }else{
            // Automatically checks if the user has permissions to write if they can already use the camera.

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
            }else{
                // If the user can both use the camera and write to external storage the camera activity
                // will start.
                writePerms = true;
                startCameraActivity();
            }
        }

    }
    // A function to transition to the camera activity so that the user can take a picture and save
    // it to their note.
    public void startCameraActivity(){
        Toast.makeText(this, "It's working right", Toast.LENGTH_SHORT).show();
        Log.d("Working", "I worked!");
    }

}
