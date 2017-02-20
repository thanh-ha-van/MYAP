package com.example.havan.mytrafficmap.model;

import java.io.Serializable;

import com.google.api.client.util.Key;

public class PlaceDetails implements Serializable {

    @Key
    public Place result;

    @Override
    public String toString() {
        if (result != null) {
            return result.toString();
        }
        return super.toString();
    }
}