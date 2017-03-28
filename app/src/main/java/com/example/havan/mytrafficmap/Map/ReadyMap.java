package com.example.havan.mytrafficmap.Map;

import android.content.Context;
import android.widget.Toast;

import com.example.havan.mytrafficmap.model.GPSTracker;
import com.example.havan.mytrafficmap.view.ConnectionDetector;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


public class ReadyMap implements GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    private Context context;

    private boolean isInternet;

    private ConnectionDetector detector;

    private GPSTracker gps;

    public ReadyMap (Context context, GoogleMap googleMap, double lat, double lon) {


        // check internet
        detector = new ConnectionDetector(context);
        isInternet = detector.isConnectingToInternet();
        if (!isInternet) {
            // Internet Connection is not present
            Toast.makeText(
                    context,
                    "Error, No internet connection",
                    Toast.LENGTH_SHORT
            ).show();
        }

        // check able of gps
        gps = new GPSTracker(context);
        if (gps.canGetLocation()) {

            lat = gps.getLatitude();
            lon = gps.getLongitude();

        } else {
            // Can't get user's current location
            Toast.makeText(
                    context,
                    "Error, Can't get the location",
                    Toast.LENGTH_SHORT
            ).show();
        }

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
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        Toast.makeText(
                context,
                "Name:" + marker.getTitle() + "\nAddress: " + marker.getSnippet(),
                Toast.LENGTH_SHORT
        ).show();

    }


}
