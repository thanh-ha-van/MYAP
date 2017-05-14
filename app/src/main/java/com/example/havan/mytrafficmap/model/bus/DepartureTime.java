
package com.example.havan.mytrafficmap.model.bus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepartureTime {

    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("time_zone")
    @Expose
    public String timeZone;
    @SerializedName("value")
    @Expose
    public Integer value;

}
