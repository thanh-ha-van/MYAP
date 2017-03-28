package com.example.havan.mytrafficmap.UI;

import android.content.Context;

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

    public CheckConnection (Context context, double lat, double lon) {

        // check internet
        detector = new ConnectionDetector(context);
        isInternet = detector.isConnectingToInternet();
        if (!isInternet) {
            // Internet Connection is not present
            alert.showAlertDialog(context,
                    "Internet Connection Error",
                    "Please connect to working Internet connection",
                    2);
            // stop executing code by return
            return;
        }

        // check able of gps
        gps = new GPSTracker(context);
        if (gps.canGetLocation()) {

            lat = gps.getLatitude();
            lon = gps.getLongitude();

        } else {
            // Can't get user's current location
            alert.showAlertDialog( context, "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    2);
        }
    }

}
