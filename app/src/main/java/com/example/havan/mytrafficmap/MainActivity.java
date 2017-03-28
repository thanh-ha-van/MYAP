package com.example.havan.mytrafficmap;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.havan.mytrafficmap.Map.Direction;
import com.example.havan.mytrafficmap.Map.ReadyMap;
import com.example.havan.mytrafficmap.Map.ShowPlace;
import com.example.havan.mytrafficmap.Map.setView;
import com.example.havan.mytrafficmap.Style.setMapStyle;
import com.example.havan.mytrafficmap.UI.CheckConnection;
import com.example.havan.mytrafficmap.UI.CheckFirstRun;
import com.example.havan.mytrafficmap.UI.InitActionbar;
import com.example.havan.mytrafficmap.UI.InitSideMenu;
import com.example.havan.mytrafficmap.directions.PlaceDirections;
import com.example.havan.mytrafficmap.model.MyPlace;
import com.example.havan.mytrafficmap.model.MyPlaces;
import com.example.havan.mytrafficmap.view.AlertDialogManager;
import com.example.havan.mytrafficmap.view.TitleNavigationAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

@EActivity(R.layout.maps)
public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        ActionBar.OnNavigationListener,
        LocationListener {

    public PlaceDirections directions;
    public Direction direction;

    public GoogleMap mMap;
    public ShowPlace showPlace;
    public ProgressDialog pDialog;

    double latTmp;
    double lonTmp;
    private ArrayList<Marker> listMaker;

    //ui
    public android.support.v7.app.ActionBar actionBar;
    private ActionBarDrawerToggle mDrawerToggle;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;

    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private TitleNavigationAdapter adapter;

    private AlertDialogManager alert = new AlertDialogManager();

    private double lat;
    private double lon;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private static final int SECOND_ACTIVITY_RESULT_CODE = 0;
    private static final int FAV_LIST_ACTIVITY_RESULT_CODE = 0;


    @AfterViews
    public void afterViews() {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/SVN-Aguda Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = pref.edit();

        new CheckFirstRun(this); // check first run settings
        new CheckConnection(this, lat, lon);  // check connections

        loadMap();

        // init UI
        initUi();

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // testing
            Toast.makeText(
                    this,
                    "No location provider enabled!",
                    Toast.LENGTH_LONG
            ).show();
            String query = intent.getStringExtra(SearchManager.QUERY);
            mMap.clear();
            Utils.sKeyPlace = query;
            //new LoadPlaces().execute();
            showPlace = new ShowPlace(getApplicationContext(),MainActivity.this, mMap);
            showPlace.setOnActionListener(new ShowPlace.OnActionListener() {
                @Override
                public void onPreExecute() {

                    pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading nearby Places..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

                }

                @Override
                public void onPostExecute(MyPlaces listPlace) {

                    // draw my position
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lon))
                            .title("Me")
                            .snippet("Local of me")
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.pin_new_blue)));


                    if (listPlace.results != null) {
                        // loop through all the places
                        for (MyPlace place : listPlace.results) {
                            latTmp = place.geometry.location.lat; // latitude
                            lonTmp = place.geometry.location.lng; // longitude

                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latTmp, lonTmp))
                                    .title(place.name)
                                    .snippet(place.vicinity
                                            + "\nID: "
                                            + place.place_id
                                    )
                                    .icon(BitmapDescriptorFactory
                                            .fromResource(R.drawable.pin_new_red)));

                            listMaker.add(marker);
                        }
                    } else {
                        alert.showAlertDialog(MainActivity.this, "ERROR",
                                "Sorry, cant not find nearby places. Try to change the type",
                                2);
                    }
                }
            });

        }
    }

    private void initUi() {

        new InitSideMenu(
                this.getApplicationContext(),
                MainActivity.this,
                mDrawerToggle, navigationView, mDrawerLayout );

        navigationView.setNavigationItemSelectedListener(this);

        new InitActionbar(getApplicationContext(), MainActivity.this, mMap, actionBar, adapter);

    }

    private void loadMap() {

        final SupportMapFragment mapFragment
                = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        // Set the event that map is ready
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                new ReadyMap(getApplicationContext(), mMap , lat, lon);
                new setView (getApplicationContext(), mMap);
                new setMapStyle(getApplicationContext(), mMap);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId(); // Handle navigation view item clicks here.

      if (id == R.id.map_style) {
            Intent i = new Intent(MainActivity.this, MapStyle_.class);
            startActivity(i);
            // choose map style
        } else if (id == R.id.view_option) {
            startActivity(new Intent(MainActivity.this, ViewOption_.class));
            // view option activity
        } else if (id == R.id.fav_place) {
            // list of fav place activity

            Intent intent = new Intent(this, FavListActivity_.class);
            startActivityForResult(intent, FAV_LIST_ACTIVITY_RESULT_CODE);
        } else if (id == R.id.share) {
            startActivity(new Intent(MainActivity.this, ShareActivity_.class));
        } else if (id == R.id.about) {
            startActivity(new Intent(MainActivity.this, About_.class));
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.search:
                Intent intent = new Intent(this, SearchActivity_.class);
                startActivityForResult(intent, SECOND_ACTIVITY_RESULT_CODE);
                return true;

            case R.id.direc: {
                // get type way
                byte way = Utils.sKeyWay;
                // get sDestination
                LatLng des = Utils.sDestination;
                // get ways by listview + show on map
                {
                    if (des == null) {
                        alert.showAlertDialog(this, "Place empty",
                                "Please choice destination place", 2);
                    } else {
                        LatLng from = new LatLng(lat, lon);
                        directions = new PlaceDirections(getApplicationContext()
                                , mMap, from, des, way);
                    }
                }
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // This method is called when the second activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                direction = new Direction(this, mMap, data, lat, lon);
            }
        }
        if (requestCode == FAV_LIST_ACTIVITY_RESULT_CODE) {

            if (resultCode == RESULT_OK) {
                direction = new Direction(this, mMap, data, lat, lon);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        lon = location.getLongitude();
        LatLng latLng = new LatLng(lat, lon);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 3000, null);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMap();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
