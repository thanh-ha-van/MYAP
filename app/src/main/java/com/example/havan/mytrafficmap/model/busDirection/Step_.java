
package com.example.havan.mytrafficmap.model.busDirection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step_ {

    @SerializedName("distance")
    @Expose
    private Distance__ distance;
    @SerializedName("duration")
    @Expose
    private Duration__ duration;
    @SerializedName("end_location")
    @Expose
    private EndLocation__ endLocation;
    @SerializedName("html_instructions")
    @Expose
    private String htmlInstructions;
    @SerializedName("polyline")
    @Expose
    private Polyline_ polyline;
    @SerializedName("start_location")
    @Expose
    private StartLocation__ startLocation;
    @SerializedName("travel_mode")
    @Expose
    private String travelMode;
    @SerializedName("maneuver")
    @Expose
    private String maneuver;

    public Distance__ getDistance() {
        return distance;
    }

    public void setDistance(Distance__ distance) {
        this.distance = distance;
    }

    public Duration__ getDuration() {
        return duration;
    }

    public void setDuration(Duration__ duration) {
        this.duration = duration;
    }

    public EndLocation__ getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(EndLocation__ endLocation) {
        this.endLocation = endLocation;
    }

    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
    }

    public Polyline_ getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline_ polyline) {
        this.polyline = polyline;
    }

    public StartLocation__ getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(StartLocation__ startLocation) {
        this.startLocation = startLocation;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public String getManeuver() {
        return maneuver;
    }

    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }

}
