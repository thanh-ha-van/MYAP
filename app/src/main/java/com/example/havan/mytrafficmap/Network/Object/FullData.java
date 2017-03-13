package com.example.havan.mytrafficmap.Network.Object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by HaVan on 3/13/2017.
 */
public class FullData {

    @SerializedName("data")
    private ArrayList<Data> data;

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
