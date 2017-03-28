package com.example.havan.mytrafficmap.UI;

import android.content.Context;
import android.widget.Toast;

import com.example.havan.mytrafficmap.model.GPSTracker;
import com.example.havan.mytrafficmap.view.AlertDialogManager;
import com.example.havan.mytrafficmap.view.ConnectionDetector;

/**
 * Created by NTT on 3/28/2017.
 */

public class CheckConnection {


    private AlertDialogManager alert;
    private boolean isInternet = false;
    private ConnectionDetector detector;
    private GPSTracker gps;
    private double lat;
    private double lon;

    public CheckConnection (Context context, double lat, double lon) {

        this.lat = lat;
        this.lon = lon;

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
    }

}
