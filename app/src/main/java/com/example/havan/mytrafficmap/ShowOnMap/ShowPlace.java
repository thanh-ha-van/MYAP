package com.example.havan.mytrafficmap.ShowOnMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.Toast;


import com.example.havan.mytrafficmap.R;
import com.example.havan.mytrafficmap.Utils;
import com.example.havan.mytrafficmap.model.GPSTracker;
import com.example.havan.mytrafficmap.model.GooglePlaces;
import com.example.havan.mytrafficmap.model.MyPlace;
import com.example.havan.mytrafficmap.model.MyPlaces;
import com.example.havan.mytrafficmap.view.ConnectionDetector;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;



public class ShowPlace {

    public ProgressDialog pDialog;

    public GooglePlaces googlePlaces;

    public MyPlaces listPlace;

    boolean isInternet;

    ConnectionDetector detector;

    public GPSTracker gps;

    public GoogleMap mMap;

    public Activity activity;

    public Context context;

    private double lat;

    private double latTmp;

    private double lon;

    private double lonTmp;

    private static String sKeyName = "name";

    private static String sKeyId = "placeId";

    private static String sKeySite = "website";

    private ArrayList<Marker> listMaker;

    private ArrayList<HashMap<String, String>> placesListItems
            = new ArrayList<HashMap<String, String>>();

    public ShowPlace (Context context, Activity activity, GoogleMap googleMap) {

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
            Toast.makeText(
                    context,
                    "Error, Can't get the location",
                    Toast.LENGTH_SHORT
            ).show();
        }
        this.mMap = googleMap;
        this.activity = activity;
        this.context = getActivity().getApplicationContext();

        LoadPlaces loadPlaces = new LoadPlaces();
        loadPlaces.execute();

        listMaker = new ArrayList<Marker>();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                // TODO Auto-generated method stub
                Utils.sDestination = arg0.getPosition();
                Utils.sTrDestination = arg0.getTitle();
                Utils.sTrSnippet = arg0.getSnippet();
                return false;
            }
        });

    }


    class LoadPlaces extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
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
            getActivity().runOnUiThread(new Runnable() {
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
                        Toast.makeText(
                                context,
                                "Error, try to change type of place",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            });

        }
    }

    public Activity getActivity() {
        return activity;
    }
}