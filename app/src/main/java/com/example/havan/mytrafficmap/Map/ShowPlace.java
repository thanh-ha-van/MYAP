package com.example.havan.mytrafficmap.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;

import com.example.havan.mytrafficmap.MainActivity;
import com.example.havan.mytrafficmap.R;
import com.example.havan.mytrafficmap.Utils;
import com.example.havan.mytrafficmap.model.GPSTracker;
import com.example.havan.mytrafficmap.model.GooglePlaces;
import com.example.havan.mytrafficmap.model.MyPlace;
import com.example.havan.mytrafficmap.model.MyPlaces;
import com.example.havan.mytrafficmap.view.AlertDialogManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by NTT on 3/27/2017.
 */

public class ShowPlace {

    ProgressDialog pDialog;

    GooglePlaces googlePlaces;
    MyPlaces listPlace;
    GPSTracker gps;
    AlertDialogManager alert;
    GoogleMap mMap;

    Context context;

    Activity activity;

    double lat, latTmp;
    double lon, lonTmp;
    private static String sKeyName = "name";

    private static String sKeyId = "placeId";

    private static String sKeySite = "website";

    private ArrayList<Marker> listMaker;

    private ArrayList<HashMap<String, String>> placesListItems
            = new ArrayList<HashMap<String, String>>();

    public ShowPlace (Context context, Activity activity, GoogleMap googleMap) {
        this.context = context;
        this.mMap = googleMap;
        this.activity = activity;
        new LoadPlaces().execute();
    }

    class LoadPlaces extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading nearby Places..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            googlePlaces = new GooglePlaces();

            try {
                String types = Utils.sKeyPlace;
                double radius = 3000;
                listPlace = googlePlaces.search(gps.getLatitude(),
                        gps.getLongitude(), radius, types);


            } catch (Exception e) {
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            activity.runOnUiThread(new Runnable() {
                public void run() {

                    // Get json response status
                    String status = listPlace.status;

                    // Check for all possible status
                    if (status.equals("OK")) {
                        // Successfully got places details
                        if (listPlace.results != null) {
                            // loop through each place
                            for (MyPlace p : listPlace.results) {
                                HashMap<String, String> map = new HashMap<String, String>();

                                // Place name
                                map.put(sKeyName, p.name);
                                map.put(sKeyId, p.place_id);
                                map.put(sKeySite, p.website);
                                // adding HashMap to ArrayList
                                placesListItems.add(map);
                            }
                        }
                        // draw my position
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lon))
                                .title("Me")
                                .snippet("Local of me")
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.pin_new_blue)));

                        if (listPlace.results != null) {
                            // loop through all the places
                            for (MyPlace place : listPlace.results) {
                                latTmp = place.geometry.location.lat; // latitude
                                lonTmp = place.geometry.location.lng; // longitude

                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latTmp, lonTmp))
                                        .title(place.name)
                                        .snippet(place.vicinity
                                                + "\nID: "
                                                + place.place_id
                                        )
                                        .icon(BitmapDescriptorFactory
                                                .fromResource(R.drawable.pin_new_red)));

                                listMaker.add(marker);
                            }
                        }
                    } else {
                        alert.showAlertDialog(activity, "ERROR",
                                "Sorry, cant not find nearby places. Try to change the type",
                                2);
                    }
                }
            });

        }
    }
}
