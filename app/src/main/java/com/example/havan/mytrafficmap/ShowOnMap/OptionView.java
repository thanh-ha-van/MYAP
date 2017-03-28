package com.example.havan.mytrafficmap.ShowOnMap;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by NTT on 3/28/2017.
 */

public class OptionView {

    private SharedPreferences pref;


    public OptionView (GoogleMap mMap, Context context)
    {
        pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        mMap.setTrafficEnabled(pref.getBoolean("show_traffic", false));
        mMap.getUiSettings().setZoomControlsEnabled(pref.getBoolean("zoom", false));
        mMap.setMyLocationEnabled(true);
    }
}
