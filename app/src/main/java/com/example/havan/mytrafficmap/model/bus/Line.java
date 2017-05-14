
package com.example.havan.mytrafficmap.model.bus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Line {

    @SerializedName("agencies")
    @Expose
    public List<Agency> agencies = null;
    @SerializedName("color")
    @Expose
    public String color;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("text_color")
    @Expose
    public String textColor;
    @SerializedName("vehicle")
    @Expose
    public Vehicle vehicle;
}
