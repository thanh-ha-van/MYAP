package com.example.havan.mytrafficmap.Route;

/**
 * Created by HaVan on 4/11/2017.
 */
public class RouteModel {

    private int id;

    private String name;
    private String address;
    private String value;
    private double placeLat;
    private double placeLon;

    private boolean isChecked;

    public RouteModel() {
    }

    public RouteModel(String name, String address,  double placeLat, double placeLon, String value) {
        this.name = name;
        this.address = address;
        this.value = value;
        this.placeLat = placeLat;
        this.placeLon = placeLon;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public double getPlaceLat() {
        return placeLat;
    }

    public void setPlaceLat(double placeLat) {
        this.placeLat = placeLat;
    }

    public double getPlaceLon() {
        return placeLon;
    }

    public void setPlaceLon(double placeLon) {
        this.placeLon = placeLon;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
