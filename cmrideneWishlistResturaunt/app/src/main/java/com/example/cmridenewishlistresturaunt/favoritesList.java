package com.example.cmridenewishlistresturaunt;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class favoritesList extends Fragment implements OnSuccessListener<FetchPlaceResponse>, OnFailureListener, AdapterView.OnItemClickListener {
    List<String> favoriteViews = new ArrayList<>();
    View root;

    Map<String, ?> favorites;
    SharedPreferences pref;
    String[] favoritesList;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        Places.initialize(this.getContext(),"AIzaSyAfWS-LwzPOqgCTO5siNW7S4nL50y5M5Vw");
        pref = this.getContext().getSharedPreferences("Favorites", this.getContext().MODE_PRIVATE);
        if(root != null) {
            generateViews(pref);
        }
    }
    @Override
    public View onCreateView(LayoutInflater in, ViewGroup vg, Bundle savedInstances){
        root = in.inflate(R.layout.favorites_list_layout, vg,false);
        pref = this.getContext().getSharedPreferences("Favorites", root.getContext().MODE_PRIVATE);
        generateViews(pref);
        return root;
    }
    public void generateViews(SharedPreferences pref){

        favorites = pref.getAll();
        if(favorites!=null) {
            Log.d("Hello", favorites.toString());
        }
        favoritesList = new String[favorites.size()];
        int k = 0;
        for(Map.Entry<String,?> entry : favorites.entrySet()){
            favoritesList[k] = (String) entry.getValue();
        }
        PlacesClient placesClient = Places.createClient(this.getContext());
        List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.RATING,Place.Field.PHONE_NUMBER,
                Place.Field.WEBSITE_URI,Place.Field.PHOTO_METADATAS,Place.Field.TYPES);
        for(int i = 0; i<favoritesList.length;i++) {
            placesClient.fetchPlace(FetchPlaceRequest.newInstance(favoritesList[i], fields)).addOnSuccessListener(this).addOnFailureListener(this);
        }
        ListView lv = root.findViewById(R.id.favoriteList);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1,favoritesList);
        lv.setOnItemClickListener(this);
        lv.setAdapter(aa);
    }

    @Override
    public void onSuccess(FetchPlaceResponse fr) {
        Place place = fr.getPlace();
        favoriteViews.add(place.getName());

    }

    @Override
    public void onFailure(@NonNull Exception e) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences.Editor edit = pref.edit();
        edit.remove(favoritesList[position]);
        edit.commit();
        edit.clear();
        generateViews(pref);
    }
}
