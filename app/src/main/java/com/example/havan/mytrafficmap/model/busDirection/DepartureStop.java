
package com.example.havan.mytrafficmap.model.busDirection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepartureStop {

    @SerializedName("location")
    @Expose
    private Location_ location;
    @SerializedName("name")
    @Expose
    private String name;

    public Location_ getLocation() {
        return location;
    }

    public void setLocation(Location_ location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
