
package com.example.havan.mytrafficmap.model.bus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Step {

    @SerializedName("distance")
    @Expose
    public Distance_ distance;
    @SerializedName("duration")
    @Expose
    public Duration_ duration;
    @SerializedName("end_location")
    @Expose
    public EndLocation_ endLocation;
    @SerializedName("html_instructions")
    @Expose
    public String htmlInstructions;
    @SerializedName("polyline")
    @Expose
    public Polyline polyline;
    @SerializedName("start_location")
    @Expose
    public StartLocation_ startLocation;
    @SerializedName("steps")
    @Expose
    public List<Step_> steps = null;
    @SerializedName("travel_mode")
    @Expose
    public String travelMode;
    @SerializedName("transit_details")
    @Expose
    public TransitDetails transitDetails;

}
