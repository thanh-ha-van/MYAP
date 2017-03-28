package com.example.havan.mytrafficmap.ShowOnMap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by NTT on 3/28/2017.
 */

public class Movecamera {
    public Movecamera (GoogleMap googleMap, double lat, double lon) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lon))
                .zoom(15)                   // Sets the zoom
                .bearing(0)                // Sets to east
                .tilt(30)                   // Sets to 30 degrees
                .build();                   // Creates a CameraPosition
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        // Need user permisson (above)
    }
}
