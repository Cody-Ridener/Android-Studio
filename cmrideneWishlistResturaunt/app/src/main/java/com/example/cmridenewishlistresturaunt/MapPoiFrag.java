package com.example.cmridenewishlistresturaunt;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cmridenewishlistresturaunt.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;;import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class  MapPoiFrag extends Fragment implements GoogleMap.OnPoiClickListener, OnSuccessListener, OnFailureListener, OnMapReadyCallback{
    MapView mapView;
    GoogleMap map;
    PlacesClient placesClient;
    Place place;
    View root;
    Boolean add = true;
    Bundle info = new Bundle();
    LocationManager mLocationManager;
    boolean mLocationPermissionGranted = false;
    final List<String> favoritePlaces = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        info = savedInstances;
        mLocationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
    }
    @Override
    public View onCreateView(LayoutInflater in, ViewGroup vg, Bundle savedInstances){
        info = savedInstances;
        View rootView = in.inflate(R.layout.fragment_main, vg,false);
        Places.initialize(rootView.getContext(), "AIzaSyAfWS-LwzPOqgCTO5siNW7S4nL50y5M5Vw");
        placesClient = Places.createClient(rootView.getContext());
        root = rootView;
        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstances);
        mapView.getMapAsync(this);
        // Creates a location manager that will be used to get the user's location assuming location
        // permissions are granted.
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);

        return rootView;
    }

    @Override
    public void onPoiClick(PointOfInterest poi) {
        List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.RATING,Place.Field.PHONE_NUMBER,
                Place.Field.WEBSITE_URI,Place.Field.PHOTO_METADATAS,Place.Field.TYPES);
        placesClient.fetchPlace(FetchPlaceRequest.newInstance(poi.placeId, fields)).addOnSuccessListener(this).addOnFailureListener(this);

    }

    @Override
    public void onFailure(@NonNull Exception e) {




    }

    @Override
    public void onSuccess(Object o) {
        FetchPlaceResponse res = (FetchPlaceResponse) o;
        place =  res.getPlace();
        populateViews(place);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map =googleMap;
        map.addMarker(new MarkerOptions().position(new LatLng(-86.52643,39.1653)).title("Help me"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-86.52643,39.1653),15));
        map.setOnPoiClickListener(this);
        ((MainActivity) getActivity()).onMapReady(map);
        mapView.onResume();
        map.setOnPoiClickListener(this);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                View lay = root.findViewById(R.id.popupLayout);
                lay.setVisibility(View.INVISIBLE);
                Button btn = root.findViewById(R.id.btnFav);
                btn.setText("Favorite");
            }
        });
    }
    private boolean getLocationPermission(){
        //Checks if the app has permission to access the device's location. If it does it sets the
        // permission value to true. If it doesn't then it will prompt the user
        // for permission.
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //generateMap();
            return mLocationPermissionGranted;

        }else{


            //Calls the request permissions command to prompt the user for permission to use
            //their fine location.
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return mLocationPermissionGranted;
        }

    }
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(root.getContext(), "App needs location data to find resturaunts" +
                                    " in your area. App Closing", Toast.LENGTH_LONG).show();
                        }
                    });
                    // Sleeps long enough for the user to read the toast before the app closes itself.
                    try {
                        Thread.sleep(3500);
                    } catch(InterruptedException e){}

                    System.exit(0);
                    break;

                }

            }
        }

    }
    public void populateViews(Place place){
        if(place.getTypes()!=null) {
            if (place.getTypes().contains(Place.Type.RESTAURANT)) {
                add = true;
                for (int i = 0; i < favoritePlaces.size(); i++) {
                    if (favoritePlaces.get(i) == place.getId()) {
                        add = false;
                    }
                }
                if (add == false) {
                    Button btn = root.findViewById(R.id.btnFav);
                    btn.setText("Favorited");
                }
                TextView webView = (TextView) root.findViewById(R.id.txtWeb);
                TextView nameView = (TextView) root.findViewById(R.id.txtName);
                final ImageView logoView = (ImageView) root.findViewById(R.id.imgRest);
                TextView phoneView = (TextView) root.findViewById(R.id.txtPhone);
                TextView rateView = (TextView) root.findViewById(R.id.txtRating);
                View popupView = root.findViewById(R.id.popupLayout);
                if (place.getWebsiteUri() != null) {
                    webView.setText(place.getWebsiteUri().toString());
                } else {
                    webView.setText("Not available");
                }
                if (place.getName() != null) {
                    nameView.setText(place.getName());
                } else {
                    nameView.setText("Not Available");
                }
                if (place.getPhotoMetadatas() != null) {
                    PhotoMetadata pM = place.getPhotoMetadatas().get(0);
                    String att = pM.getAttributions();
                    FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(pM).setMaxHeight(400).setMaxWidth(400).build();
                    placesClient.fetchPhoto(photoRequest).addOnSuccessListener(new OnSuccessListener<FetchPhotoResponse>() {
                        @Override
                        public void onSuccess(FetchPhotoResponse fetchPhotoResponse) {
                            Bitmap bm = fetchPhotoResponse.getBitmap();
                            logoView.setImageBitmap(bm);
                        }
                    });

                } else {
                }
                if (place.getPhoneNumber() != null) {
                    phoneView.setText(place.getPhoneNumber());
                } else {
                    phoneView.setText("Not Available");
                }
                if (place.getRating() != null) {
                    rateView.setText(place.getRating().toString());
                } else {
                    rateView.setText("Not Available");
                }
                popupView.setVisibility(View.VISIBLE);
                checkPreferences(place.getId());
                addToPreferences(place.getId());
            } else {
            }
        }

    }
    public void addToPreferences(String s){
        SharedPreferences pref = root.getContext().getSharedPreferences("Current", root.getContext().MODE_APPEND);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Current",s);
        editor.commit();
        editor.clear();
    }
    public void checkPreferences(String s){
        SharedPreferences pref = root.getContext().getSharedPreferences("Favorites", root.getContext().MODE_PRIVATE);
        if(pref.getString(s, "0").equals(s)){
            Button btn = root.findViewById(R.id.btnFav);
            btn.setText("Favorited");
        }

    }


}
