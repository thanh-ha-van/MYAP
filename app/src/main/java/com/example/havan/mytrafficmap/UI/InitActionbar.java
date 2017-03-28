package com.example.havan.mytrafficmap.UI;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.havan.mytrafficmap.MainActivity;
import com.example.havan.mytrafficmap.Map.ShowPlace;
import com.example.havan.mytrafficmap.Utils;
import com.example.havan.mytrafficmap.view.TitleNavigationAdapter;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by NTT on 3/27/2017.
 */

public class InitActionbar  extends AppCompatActivity {

    Context context;

    Activity activity;

    GoogleMap mMap;

    ActionBar mActionBar;

    TitleNavigationAdapter mAdapter;

    ShowPlace showPlace;

    public InitActionbar (Context context,
                          final Activity activity,
                          GoogleMap googleMap,
                          ActionBar actionBar,
                          TitleNavigationAdapter adapter) {
        this.context = context;
        this.mMap = googleMap;
        this.activity = activity;
        this.mActionBar = actionBar;
        this.mAdapter = adapter;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_LIST);

        actionBar = getSupportActionBar();

        adapter = new TitleNavigationAdapter(getApplicationContext());

        final TitleNavigationAdapter adapter1 = adapter;
        getSupportActionBar().setListNavigationCallbacks(adapter,
                new android.support.v7.app.ActionBar.OnNavigationListener() {
                    @Override
                    public boolean onNavigationItemSelected(int itemPosition, long itemId) {

                        if (itemPosition > -1) {
                            mMap.clear();
                            Utils.sKeyPlace = adapter1.getName(itemPosition);
                            showPlace = new ShowPlace(getApplicationContext(),activity, mMap);
                            itemPosition = -1;
                        }
                        return true;
                    }
                });


    }
}
