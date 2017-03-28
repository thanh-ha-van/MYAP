package com.example.havan.mytrafficmap.Map;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.example.havan.mytrafficmap.Utils;
import com.example.havan.mytrafficmap.model.GPSTracker;
import com.example.havan.mytrafficmap.model.GooglePlaces;
import com.example.havan.mytrafficmap.model.MyPlace;
import com.example.havan.mytrafficmap.model.MyPlaces;
import com.example.havan.mytrafficmap.view.ConnectionDetector;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowPlace  {


    public GooglePlaces googlePlaces;

    public MyPlaces listPlace;

    public GPSTracker gps;

    public GoogleMap mMap;

    public Activity activity;

    private double lat;

    private double latTmp;

    private double lon;

    private double lonTmp;

    private static String sKeyName = "name";

    private static String sKeyId = "placeId";

    private static String sKeySite = "website";


    private ArrayList<HashMap<String, String>> placesListItems
            = new ArrayList<HashMap<String, String>>();

    public ShowPlace (Context context, Activity activity, GoogleMap googleMap) {

        gps = new GPSTracker(context);
        if (gps.canGetLocation()) {

            lat = gps.getLatitude();
            lon = gps.getLongitude();

        }

    this.mMap = googleMap;
        this.mMap.clear();
        this.activity = activity;

        LoadPlaces loadPlaces = new LoadPlaces();
        loadPlaces.execute();

        googleMap = mMap;

       googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

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

            if (onActionListener != null){
                onActionListener.onPreExecute();
            }
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

            if (onActionListener != null){
                onActionListener.onPostExecute(listPlace);
            }

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
                    }
                }
                });
        }
    }

    private OnActionListener onActionListener;


    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    public interface OnActionListener{
        void onPreExecute();
        void onPostExecute(MyPlaces listPlace);
    }

    public Activity getActivity() {
        return activity;
    }
}
