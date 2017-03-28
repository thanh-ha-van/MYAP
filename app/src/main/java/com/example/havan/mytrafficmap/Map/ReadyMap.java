package com.example.havan.mytrafficmap.Map;

import android.content.Context;

import com.example.havan.mytrafficmap.view.AlertDialogManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


public class ReadyMap implements GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    private Context context;

    private AlertDialogManager alert;

    public ReadyMap (Context context, GoogleMap googleMap, double lat, double lon) {

        // Get the google map object
        this.context = context;
        this.mMap = googleMap;

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lon))
                // Sets the center of the map to location user
                .zoom(15)                   // Sets the zoom
                .bearing(0)                // Sets to east
                .tilt(40)                   // Sets to 30 degrees
                .build();                   // Creates a CameraPosition
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        googleMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        alert.showAlertDialog( context, "Information",
                marker.getTitle() + "\n" + marker.getSnippet(), 3);

    }


}
