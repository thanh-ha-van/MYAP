package com.example.havan.mytrafficmap.SQLite;


/**
 * Created by HaVan on 3/9/2017.
 */

// class define a place to save in database and show in app.
public class DataModel {

    private int id;
    private String name;
    private String address;
    private String placeID;
    private boolean isChecked;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
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