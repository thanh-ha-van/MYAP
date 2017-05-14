
package com.example.havan.mytrafficmap.model.bus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route {

    @SerializedName("bounds")
    @Expose
    public Bounds bounds;
    @SerializedName("copyrights")
    @Expose
    public String copyrights;
    @SerializedName("legs")
    @Expose
    public List<Leg> legs = null;
    @SerializedName("summary")
    @Expose
    public String summary;
    @SerializedName("warnings")
    @Expose
    public List<String> warnings = null;
    @SerializedName("waypoint_order")
    @Expose
    public List<Object> waypointOrder = null;

}
