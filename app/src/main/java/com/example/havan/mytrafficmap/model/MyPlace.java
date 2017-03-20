package com.example.havan.mytrafficmap.model;

/**
 * Created by NTT on 3/3/2017.
 */

import com.google.api.client.util.Key;

import java.io.Serializable;


public class MyPlace implements Serializable {

    @Key
    public String website;

    @Key
    public String place_id;

    @Key
    public String name;

    @Key
    public String international_phone_number;

    @Key
    public String icon;

    @Key
    public String vicinity;

    @Key
    public Geometry geometry;

    @Override
    public String toString() {
        return
                name
                        + " - "
                        + place_id
                        + " - "
                        + international_phone_number
                        + " - "
                        + website
                        + " - "
                        + vicinity
                ;
    }

    public static class Geometry implements Serializable {
        @Key
        public Location location;
    }

    public static class Location implements Serializable {
        @Key
        public double lat;

        @Key
        public double lng;
    }
}
