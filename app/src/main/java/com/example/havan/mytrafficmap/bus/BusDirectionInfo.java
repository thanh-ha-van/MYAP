package com.example.havan.mytrafficmap.bus;

import android.content.Context;
import android.content.Intent;

import com.example.havan.mytrafficmap.R;
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

    List<Step> steps;
    List<List<Step_>> childSteps;
    List<TransitDetails> transitDetailses;

    public BusDirectionInfo(Context context, GoogleMap googleMap, Intent data) {

        this.mContext = context;
        this.mGoogleMap = googleMap;
        this.mIntent = data;
        this.jsonback = data.getStringExtra("busdirection");

        polyLineOptions = null;
        Gson gson = new Gson();

        busdirection = new Gson().fromJson(jsonback, Busdirection.class);

        if (!busdirection.getStatus().equals("OK")) {

            // cant not find the way
        } else {
            drawPolyline();
        }

    }

    private void drawPolyline() {


        // stuffs and getting data
        mGoogleMap.clear();
        steps = new ArrayList<>();
        childSteps = new ArrayList<>();
        transitDetailses = new ArrayList<>();
        steps = busdirection.getRoutes().get(0).getLegs().get(0).getSteps();

        for (int i = 0; i < steps.size(); i++) {
            childSteps.add(i, steps.get(i).getSteps());
        }

        childSteps.size();
        for (int i = 0; i < steps.size(); i++) {
            transitDetailses.add(i, steps.get(i).getTransitDetails());
        }
        drawBigStuffs();
        //drawSmallStuffs();


    }

    public void drawSmallStuffs() {

        for (int i = 0; i < childSteps.size(); i++) {

            for (int k = 0; k < childSteps.get(i).size(); k++) {
                LatLng smallStartLatLng = new LatLng(
                        childSteps.get(i).get(k).getStartLocation().getLat(),
                        childSteps.get(i).get(k).getStartLocation().getLng()
                );
                LatLng smallEndLatLng = new LatLng(
                        childSteps.get(i).get(k).getStartLocation().getLat(),
                        childSteps.get(i).get(k).getStartLocation().getLng()
                );
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(smallStartLatLng)
                        .title(childSteps.get(i).get(k).getHtmlInstructions())
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.pin_bus)));
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(smallEndLatLng)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.pin_bus)));
            }

        }

    }

    public void drawBigStuffs() {

        for (int i = 0; i < steps.size(); i++) {

            LatLng bigStartLatLng = new LatLng(
                    steps.get(i).getStartLocation().getLat(),
                    steps.get(i).getStartLocation().getLng()
            );

            LatLng bigEngLatLng = new LatLng(
                    steps.get(i).getStartLocation().getLat(),
                    steps.get(i).getStartLocation().getLng()
            );


            mGoogleMap.addMarker(new MarkerOptions()
                    .position(bigStartLatLng)
                    .title(steps.get(i).getHtmlInstructions())
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.pin_bus)));

            mGoogleMap.addMarker(new MarkerOptions()
                    .position(bigEngLatLng)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.pin_bus)));
        }
    }

}
