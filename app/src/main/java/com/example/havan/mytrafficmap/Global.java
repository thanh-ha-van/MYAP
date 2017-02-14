package com.example.havan.mytrafficmap;

import android.app.Application;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by HaVan on 2/13/2017.
 */
public class Global extends Application {
    public static GoogleMap myMap;

    public static final String MYTAG = "MYTAG";


    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;

    public GoogleMap getMyMap() {

        return myMap;
    }

    public void setMyMap(GoogleMap myMap1) {

        myMap = myMap1;

    }

}
