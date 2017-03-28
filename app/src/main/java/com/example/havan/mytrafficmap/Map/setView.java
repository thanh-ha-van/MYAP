package com.example.havan.mytrafficmap.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by HaVan on 3/28/2017.
 */
public class setView {

    private SharedPreferences pref;

    public setView (Context context, GoogleMap googleMap) {

        pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        googleMap.setTrafficEnabled(pref.getBoolean("show_traffic", false));

        googleMap.getUiSettings().setZoomControlsEnabled(pref.getBoolean("zoom", false));

        googleMap.setMyLocationEnabled(true);
    }
}
