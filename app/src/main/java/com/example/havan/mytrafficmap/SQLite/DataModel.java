package com.example.havan.mytrafficmap.SQLite;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by HaVan on 3/9/2017.
 */
public class DataModel {

    private int id;
    private String name;
    private String address;
    private String placeID;

    public DataModel() {

    }

    public DataModel(String name, String address, String placeID) {
        this.name = name;
        this.address = address;
        this.placeID = placeID;
    }

    public DataModel(int id, String name, String address, String placeID) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.placeID = placeID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }
}