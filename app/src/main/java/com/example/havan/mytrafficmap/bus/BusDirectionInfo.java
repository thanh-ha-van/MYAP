package com.example.havan.mytrafficmap.bus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.example.havan.mytrafficmap.R;
import com.example.havan.mytrafficmap.Utils;
import com.example.havan.mytrafficmap.model.busDirection.Busdirection;
import com.example.havan.mytrafficmap.model.busDirection.Step;
import com.example.havan.mytrafficmap.model.busDirection.Step_;
import com.example.havan.mytrafficmap.model.busDirection.TransitDetails;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HaVan on 5/14/2017.
 */

public class BusDirectionInfo {

    Context mContext;
    GoogleMap mGoogleMap;
    Intent mIntent;

    String jsonback;

    PolylineOptions polyLineOptions = null;
    Busdirection busdirection;

    List<Step_> listchildstep = new ArrayList<>();
    List<Step> steps;

    public BusDirectionInfo(Context context, GoogleMap googleMap, Intent data) {

        this.mContext = context;
        this.mGoogleMap = googleMap;
        this.mIntent = data;
        this.jsonback = data.getStringExtra("busdirection");

        polyLineOptions = null;


        busdirection = new Gson().fromJson(jsonback, Busdirection.class);

        if (!busdirection.getStatus().equals("OK")) {

            // cant not find the way
        } else {
            drawPolyline();
            makeInfo();
        }

    }

    private void drawPolyline() {


        // stuffs and getting data
        mGoogleMap.clear();
        steps = new ArrayList<>();
        steps = busdirection.getRoutes().get(0).getLegs().get(0).getSteps();

        drawBigStuffs();
        //draw2Points();

    }

    public void draw2Points() {
        double lat = busdirection.getRoutes().get(0).getLegs().get(0).getStartLocation().getLat();
        double lon = busdirection.getRoutes().get(0).getLegs().get(0).getStartLocation().getLat();
        double lat2 = busdirection.getRoutes().get(0).getLegs().get(0).getStartLocation().getLat();
        double lon2 = busdirection.getRoutes().get(0).getLegs().get(0).getStartLocation().getLat();
        String ori = busdirection.getRoutes().get(0).getLegs().get(0).getStartAddress();
        String des = busdirection.getRoutes().get(0).getLegs().get(0).getEndAddress();
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title(ori)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.despin)));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat2, lon2))
                .title(des)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.despin)));
    }

    public void drawBigStuffs() {

        for (int i = 0; i < steps.size(); i++) {

            LatLng bigStartLatLng = new LatLng(
                    steps.get(i).getStartLocation().getLat(),
                    steps.get(i).getStartLocation().getLng()
            );

            LatLng bigEngLatLng = new LatLng(
                    steps.get(i).getEndLocation().getLat(),
                    steps.get(i).getEndLocation().getLng()
            );


            mGoogleMap.addMarker(new MarkerOptions()
                    .position(bigStartLatLng)
                    .title(steps.get(i).getHtmlInstructions())
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.steppin)));

            mGoogleMap.addMarker(new MarkerOptions()
                    .position(bigEngLatLng)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.steppin)));
        }
    }

    public void makeInfo() {
        String string = "";
        string += "STATUS:    ";
        string += busdirection.getStatus().toString() + "\n";
        string += "WARNING:   ";
        string += busdirection.getRoutes().get(0).getWarnings().toString() + "\n";
        for (int i = 0; i < steps.size(); i++) {

            int k = i +1;
            string += "Step " + k + ".\n";
            string += steps.get(i).getHtmlInstructions() + ".\n";
            if (steps.get(i).getTransitDetails() != null) {
                string += "Bus number: ";
                string += steps.get(i).getTransitDetails().getLine().getName() + ".\n";
                string += "Drop at the bus stop: ";
                string += steps.get(i).getTransitDetails().getNumStops() + ".\n\n";
            }

        }
        string += "\nYou are good to go!\n\n";
        Utils.sInfo = string;
    }

}
