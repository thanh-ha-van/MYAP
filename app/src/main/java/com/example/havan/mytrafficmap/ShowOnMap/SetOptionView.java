package com.example.havan.mytrafficmap.ShowOnMap;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by NTT on 3/28/2017.
 */

public class SetOptionView {

    private SharedPreferences pref;


    public SetOptionView(GoogleMap mMap, Context context)
    {
        pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        mMap.setTrafficEnabled(pref.getBoolean("show_traffic", false));
        mMap.getUiSettings().setZoomControlsEnabled(pref.getBoolean("zoom", false));
        new ShowFavorite(context, mMap, pref.getBoolean("show_fav_place", false));
        mMap.setMyLocationEnabled(true);
    }
}
