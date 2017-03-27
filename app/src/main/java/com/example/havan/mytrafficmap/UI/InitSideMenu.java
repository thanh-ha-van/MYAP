package com.example.havan.mytrafficmap.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import com.example.havan.mytrafficmap.About_;
import com.example.havan.mytrafficmap.FavListActivity_;
import com.example.havan.mytrafficmap.MapStyle_;
import com.example.havan.mytrafficmap.R;
import com.example.havan.mytrafficmap.ShareActivity_;
import com.example.havan.mytrafficmap.ViewOption_;

/**
 * Created by NTT on 3/27/2017.
 */

public class InitSideMenu {

    Context context;
    ActionBarDrawerToggle mDrawerToggle;
    NavigationView mView;
    DrawerLayout mDrawerLayout;
    Activity activity;
    private static final int FAV_LIST_ACTIVITY_RESULT_CODE = 0;

    public InitSideMenu (Context context,
                         Activity activity,
                         ActionBarDrawerToggle drawerToggle,
                         NavigationView view,
                         DrawerLayout drawerLayout) {

        this.context = context;
        this.mDrawerLayout = drawerLayout;
        this.mView = view;
        this.activity = activity;
        this.mDrawerToggle = drawerToggle;
        final  Activity activity1 = activity;
        mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                activity1.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                activity1.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);


    }



}
