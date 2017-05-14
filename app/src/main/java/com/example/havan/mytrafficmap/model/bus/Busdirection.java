
package com.example.havan.mytrafficmap.model.bus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Busdirection {

    @SerializedName("geocoded_waypoints")
    @Expose
    public List<GeocodedWaypoint> geocodedWaypoints = null;
    @SerializedName("routes")
    @Expose
    public List<Route> routes = null;
    @SerializedName("status")
    @Expose
    public String status;

}
