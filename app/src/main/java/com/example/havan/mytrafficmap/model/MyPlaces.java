package com.example.havan.mytrafficmap.model;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.util.Key;

public class MyPlaces implements Serializable {

    @Key
    public String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Key
    public List<MyPlace> results;

}
