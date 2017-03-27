package com.example.havan.mytrafficmap.Map;

import android.content.Context;
import android.content.Intent;

import com.example.havan.mytrafficmap.R;
import com.example.havan.mytrafficmap.Utils;
import com.example.havan.mytrafficmap.directions.PlaceDirections;
import com.example.havan.mytrafficmap.model.GooglePlaces;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by NTT on 3/27/2017.
 */

public class Direction {
    Context context;
    GoogleMap mMap;
    Intent intent;
    double lat1;
    double lon1;
    PlaceDirections directions;
    public Direction (
            Context context,
            GoogleMap googleMap,
            Intent intent,
            double lat,
            double lon) {

        this.context = context;
        this.mMap = googleMap;
        this.intent = intent;

        lat1 = intent.getDoubleExtra("lat", 10);
        lon1 = intent.getDoubleExtra("lon", 10);
        String address = intent.getStringExtra("address");

        // draw destination
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat1, lon1))
                .title(address)
                .snippet(address)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.pin_new_green)));
        LatLng des = Utils.sDestination;
        byte way = 2;
        LatLng from = new LatLng(lat, lon);
        directions = new PlaceDirections(getApplicationContext()
                , mMap, from, des, way);

    }
}
