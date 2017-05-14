
package com.example.havan.mytrafficmap.model.bus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Agency {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("url")
    @Expose
    public String url;

}
