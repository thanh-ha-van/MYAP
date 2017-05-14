
package com.example.havan.mytrafficmap.model.bus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArrivalStop {

    @SerializedName("location")
    @Expose
    public Location location;
    @SerializedName("name")
    @Expose
    public String name;

}
