package com.example.havan.mytrafficmap.ShowOnMap;

import com.example.havan.mytrafficmap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by NTT on 3/28/2017.
 */

public class Movecamera {
    public Movecamera (GoogleMap googleMap, double lat, double lon) {

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lon))
                .zoom(15)                   // Sets the zoom
                .bearing(0)                // Sets to east
                .tilt(0)                   // Sets to 30 degrees
                .build();                   // Creates a CameraPosition
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        // Need user permisson (above)

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title("You!")
                .snippet("This is your current location")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.pin_flag_yellow)));
    }
}
