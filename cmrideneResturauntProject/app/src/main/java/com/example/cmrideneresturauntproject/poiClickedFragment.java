package com.example.cmrideneresturauntproject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class poiClickedFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstances){
        return inflater.inflate(R.layout.poi_layout, viewGroup, false);
    }
}
