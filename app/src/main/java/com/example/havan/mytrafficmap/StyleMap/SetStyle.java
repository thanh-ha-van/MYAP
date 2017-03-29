package com.example.havan.mytrafficmap.StyleMap;

/**
 * Created by NTT on 3/28/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.example.havan.mytrafficmap.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MapStyleOptions;


public class SetStyle {

    private Context context;

    private GoogleMap mMap;

    private SharedPreferences pref;


    public SetStyle (Context context, GoogleMap googleMap) {
        this.context = context;
        pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        this.context = context;
        this.mMap = googleMap;
        int mapStyle = pref.getInt("style", 0);
        switch (mapStyle) {
            case 1:
                googleMap.setMapStyle(null);
                break;
            case 2:
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                        this.context, R.raw.mapstyle_silver));
                break;
            case 3:
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                        this.context, R.raw.mapstyle_retro));
                break;

            case 4:
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                        this.context, R.raw.mapstyle_night));
                break;
            case 5:
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                        this.context, R.raw.mapstyle_dark));
                break;
            case 6:
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                        this.context, R.raw.mapstyle_aubergine));
                break;
            case 7:
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                        this.context, R.raw.mapstyle_grey));
                break;
            case 8:
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                        this.context, R.raw.mapstyle_assassin));
                break;
            default:
                googleMap.setMapStyle(null);
        }

    }
}