package com.example.havan.mytrafficmap.ShowOnMap;

import android.content.Context;

import com.example.havan.mytrafficmap.R;
import com.example.havan.mytrafficmap.SQLite.DataModel;
import com.example.havan.mytrafficmap.SQLite.DatabaseHandler;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by NTT on 3/28/2017.
 */

public class ShowFavorite {

    private GoogleMap mMap;
    private Context mContext;

    DatabaseHandler db;

    public ShowFavorite(Context context, GoogleMap googleMap, boolean isShow) {

        this.mContext = context;
        this.mMap = googleMap;

        db = new DatabaseHandler(context);

        final List<DataModel> favPlaces = db.getAllPLaces();

        if (isShow) {
            for (DataModel datamodel : favPlaces) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(datamodel.getPlaceLat(), datamodel.getPlaceLon()))
                        .title(datamodel.getName())
                        .snippet(datamodel.getAddress())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_flag_yellow)));
            }
        } else {
            mMap.clear();
        }
    }
}
