
package com.example.havan.mytrafficmap.model.bus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransitDetails {

    @SerializedName("arrival_stop")
    @Expose
    public ArrivalStop arrivalStop;
    @SerializedName("arrival_time")
    @Expose
    public ArrivalTime_ arrivalTime;
    @SerializedName("departure_stop")
    @Expose
    public DepartureStop departureStop;
    @SerializedName("departure_time")
    @Expose
    public DepartureTime_ departureTime;
    @SerializedName("headsign")
    @Expose
    public String headsign;
    @SerializedName("headway")
    @Expose
    public Integer headway;
    @SerializedName("line")
    @Expose
    public Line line;
    @SerializedName("num_stops")
    @Expose
    public Integer numStops;

}
