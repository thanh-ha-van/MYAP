package com.example.havan.mytrafficmap.Route;

/**
 * Created by HaVan on 4/11/2017.
 */
public class RouteModel {

    String value;
    String name;
    int id;

    public RouteModel(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public RouteModel() {
    }

    public RouteModel(String value, String name, int id) {
        this.value = value;
        this.name = name;
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
