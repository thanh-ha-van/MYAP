package com.example.havan.mytrafficmap.directions;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by NTT on 3/29/2017.
 */

public class GetDistance {

    LatLng loc1;
    LatLng loc2;

    public GetDistance(double lat1, double lon1, double lat2, double lon2) {

        loc1 = new LatLng(lat1, lon1);
        loc2 = new LatLng(lat2, lon2);
    }

    public String getTheDistance() {
        Location l1 = new Location("One");
        l1.setLatitude(loc1.latitude);
        l1.setLongitude(loc1.longitude);

        Location l2 = new Location("Two");
        l2.setLatitude(loc2.latitude);
        l2.setLongitude(loc2.longitude);

        float distance = l1.distanceTo(l2);
        int discaceint = Math.round(distance);
        String dist = discaceint + " M";

        if (distance > 1000.0f) {
            double roundOff = Math.round(distance * 10.0) / 10000.0;

            dist = roundOffTo2DecPlaces(roundOff) + " KM";
        }
        return dist;
    }

    String roundOffTo2DecPlaces(double val) {
        return String.format("%.2f", val);
    }
}
