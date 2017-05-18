package com.example.havan.mytrafficmap.Route;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.widget.Toast;

import com.example.havan.mytrafficmap.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;

public class DrawRouteOffline {

    Context mContext;

    Intent mIntent;
    Double lat;
    Double lon;
    String name;
    String address;
    String jsonback;

    PolylineOptions polyLineOptions = null;

    public DrawRouteOffline(Context context, GoogleMap googleMap, Intent data) {

        this.mContext = context;
        this.mIntent = data;
        this.name = data.getStringExtra("name");
        this.address = data.getStringExtra("address");
        this.lat = data.getDoubleExtra("lat", 10);
        this.lon = data.getDoubleExtra("lon", 10);
        this.jsonback = data.getStringExtra("value");

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title(name)
                .snippet(address)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.pin_flag_red)));


        polyLineOptions = new PolylineOptions();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<LatLng>>() {
        }.getType();
        ArrayList<LatLng> routesList = gson.fromJson(jsonback, type);

        polyLineOptions.addAll(routesList);
        polyLineOptions.width(6);
        polyLineOptions.clickable(true);
        polyLineOptions.color(Color.parseColor("#26A1C3"));

        if (polyLineOptions == null) {

            Toast toast = Toast.makeText(context,
                    "Got error drawing your route", Toast.LENGTH_SHORT);
            toast.show();
        }
        else googleMap.addPolyline(polyLineOptions);


        googleMap.addMarker(new MarkerOptions()
                .position(routesList.get(0))
                .title("Start")
                .snippet("This is your start place of the route")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.steppin)));
        Toast toast = Toast.makeText(context,
                "Complete the action", Toast.LENGTH_SHORT);
        toast.show();
    }

}
