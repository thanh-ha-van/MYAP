
package com.example.havan.mytrafficmap.model.bus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step_ {

    @SerializedName("distance")
    @Expose
    public Distance__ distance;
    @SerializedName("duration")
    @Expose
    public Duration__ duration;
    @SerializedName("end_location")
    @Expose
    public EndLocation__ endLocation;
    @SerializedName("html_instructions")
    @Expose
    public String htmlInstructions;
    @SerializedName("polyline")
    @Expose
    public Polyline_ polyline;
    @SerializedName("start_location")
    @Expose
    public StartLocation__ startLocation;
    @SerializedName("travel_mode")
    @Expose
    public String travelMode;
    @SerializedName("maneuver")
    @Expose
    public String maneuver;

}
