
package com.example.havan.mytrafficmap.model.busDirection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Polyline_ {

    @SerializedName("points")
    @Expose
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

}
