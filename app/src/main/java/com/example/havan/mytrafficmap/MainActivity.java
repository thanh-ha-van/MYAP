package com.example.havan.mytrafficmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.havan.mytrafficmap.directions.PlaceDirections;
import com.example.havan.mytrafficmap.model.GPSTracker;
import com.example.havan.mytrafficmap.model.GooglePlaces;
import com.example.havan.mytrafficmap.model.MyPlaces;
import com.example.havan.mytrafficmap.model.MyPlace;

import com.example.havan.mytrafficmap.view.AlertDialogManager;
import com.example.havan.mytrafficmap.view.ConnectionDetector;
import com.example.havan.mytrafficmap.view.SpinnerItem;
import com.example.havan.mytrafficmap.view.TitleNavigationAdapter;

import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MapStyleOptions;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

@EActivity(R.layout.maps)
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ActionBar.OnNavigationListener, LocationListener {

    public PlaceDirections directions;

    public GoogleMap mMap;

    private Marker marker;
    //ui
    public android.support.v7.app.ActionBar actionBar;

    private ActionBarDrawerToggle mDrawerToggle;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;

    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private String mActivityTitle;


    private String[] compare;

    private TitleNavigationAdapter adapter;

    private boolean isInternet = false;

    private ConnectionDetector detector;

    private AlertDialogManager alert = new AlertDialogManager();

    private GPSTracker gps;

    private GooglePlaces googlePlaces;

    private MyPlaces listPlace;

    private double lat;

    private double lon;

    private double lat1;

    private double lon1;

    private double latTmp;

    private double lonTmp;

    private ProgressDialog pDialog;

    private ArrayList<Marker> listMaker;

    private ArrayList<HashMap<String, String>> placesListItems
            = new ArrayList<HashMap<String, String>>();

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    // main variable
    private static String sKeyReference = "reference";

    private static String sKeyName = "name";
    //MapView m;
    private static final int SECOND_ACTIVITY_RESULT_CODE = 0;

    private static final int FAV_LIST_ACTIVITY_RESULT_CODE = 0;

    private static String TAG = MainActivity.class.getSimpleName();


    @AfterViews
    public void afterViews() {


        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = pref.edit();
        // check if first run
        if (pref.getBoolean("firstrun", true)) {
            editor.putBoolean("firstrun",false);
            editor.putBoolean("show_traffic", true);
            editor.putString("map_style", "normal");
            editor.commit();
        }


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/SVN-Aguda Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        // init UI
        initUi();

        // check internet
        detector = new ConnectionDetector(this.getApplicationContext());
        isInternet = detector.isConnectingToInternet();
        if (!isInternet) {
            // Internet Connection is not present
            alert.showAlertDialog(this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        // check able of gps
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {

            lat = gps.getLatitude();
            lon = gps.getLongitude();

            new LoadPlaces().execute();
        } else {
            // Can't get user's current location
            alert.showAlertDialog(this, "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    false);

        }

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
            new LoadPlaces().execute();
        }

    }

    private void initUi() {

        mActivityTitle = getTitle().toString();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        navigationView.setNavigationItemSelectedListener(this);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        ///////////////////////////////

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        actionBar = getSupportActionBar();

        compare = getValue1();

        adapter = new TitleNavigationAdapter(getApplicationContext());
        getSupportActionBar().setListNavigationCallbacks(adapter,
                new android.support.v7.app.ActionBar.OnNavigationListener() {
                    @Override
                    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                        // Action to be taken after selecting a spinner item
                        // dua list marker = rong
                        if (itemPosition > 0) {
                            mMap.clear();
                            Utils.sKeyPlace = compare[itemPosition];
                            new LoadPlaces().execute();
                            itemPosition = -1;
                        }
                        return true;
                    }
                });
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


    }

    private String[] getValue1() {
        return getResources().getStringArray(R.array.compare);
    }


    private void loadMap() {

        SupportMapFragment mapFragment
                = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        // Set the event that map is ready
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                onMyMapReady(googleMap);
            }
        });

    }

    private void onMyMapReady(GoogleMap googleMap) {

        // Get the google map object
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        setMyMapStyle();
        setViewOption();
        // Need user permisson (above)
        listMaker = new ArrayList<Marker>();
        mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                // TODO Auto-generated method stub
                Utils.sDestination = arg0.getPosition();
                Utils.sTrDestination = arg0.getTitle();
                Utils.sTrSnippet = arg0.getSnippet();
                return false;
            }
        });
        // Setting a click event handler for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {


                if (marker != null)
                    marker.remove();

                marker = mMap.addMarker(new MarkerOptions()
                        .title (getCompleteAddressString (
                                point.latitude,
                                point.longitude
                        ).toString())
                        .position(point)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_yellow)));

                    Utils.sDestination = point;
                    Utils.sTrDestination = marker.getTitle();
                    Utils.sTrSnippet = marker.getSnippet();

            }
        });

    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    LATITUDE,
                    LONGITUDE,
                    1
            );
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(
                                    returnedAddress
                                            .getAddressLine(i)
                            ).append("; ");
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }
    public void setViewOption () {

        mMap.setTrafficEnabled(pref.getBoolean("show_traffic", false));
        mMap.getUiSettings().setZoomControlsEnabled(pref.getBoolean("zoom", false));
    }

    public void setMyMapStyle () {
        String mapStyle = pref.getString("map_style", null);
        switch (mapStyle) {
            case "normal":
                mMap.setMapStyle(null);
                break;
            case "night":
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.mapstyle_night));
                break;
            case "retro":
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.mapstyle_retro));
                break;

            case "dark":
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.mapstyle_dark));
                break;
            case "silver":
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.mapstyle_silver));
                break;
            case "aubergine":
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.mapstyle_aubergine));
                break;
            default:
                mMap.setMapStyle(null);
        }

    }


    class LoadPlaces extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Places JSON
         */
        protected String doInBackground(String... args) {
            googlePlaces = new GooglePlaces();

            try {
                String types = Utils.sKeyPlace;
                double radius = 3000;
                listPlace = googlePlaces.search(gps.getLatitude(),
                        gps.getLongitude(), radius, types);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                    // Get json response status
                    String status = listPlace.status;

                    // Check for all possible status
                    if (status.equals("OK")) {
                        // Successfully got places details
                        if (listPlace.results != null) {
                            // loop through each place
                            for (MyPlace p : listPlace.results) {
                                HashMap<String, String> map = new HashMap<String, String>();

                                // Place reference won't display in listview - it will be hidden
                                // Place reference is used to get "place full details"
                                map.put(sKeyReference, p.reference);

                                // Place name
                                map.put(sKeyName, p.name);
                                // adding HashMap to ArrayList
                                placesListItems.add(map);
                            }
                        }
                        loadMap();

                        // draw my position
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lon))
                                .title("Me")
                                .snippet("Local of me")
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.blue_pin)));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(lat, lon))
                                // Sets the center of the map to location user
                                .zoom(15)                   // Sets the zoom
                                .bearing(90)                // Sets to east
                                .tilt(40)                   // Sets to 30 degrees
                                .build();                   // Creates a CameraPosition
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        if (listPlace.results != null) {
                            // loop through all the places
                            for (MyPlace place : listPlace.results) {
                                latTmp = place.geometry.location.lat; // latitude
                                lonTmp = place.geometry.location.lng; // longitude

                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latTmp, lonTmp))
                                        .title(place.name)
                                        .snippet(place.vicinity)
                                        .icon(BitmapDescriptorFactory
                                                .fromResource(R.drawable.pin_red)));

                                listMaker.add(marker);
                            }
                        }
                    } else if (status.equals("ZERO_RESULTS")) {
                        // Zero results found
                        alert.showAlertDialog(MainActivity.this, "ERROR",
                                "Sorry no places found. Try to change the types of places",
                                false);
                    } else if (status.equals("UNKNOWN_ERROR")) {
                        alert.showAlertDialog(MainActivity.this, "ERROR",
                                "Sorry unknown error occured.",
                                false);
                    } else if (status.equals("REQUEST_DENIED")) {
                        alert.showAlertDialog(MainActivity.this, "ERROR",
                                "Sorry error occured. Request is denied",
                                false);
                    } else {
                        alert.showAlertDialog(MainActivity.this, "ERROR",
                                "Sorry error occured.",
                                false);
                    }
                }
            });

        }
    }


    @Override
    public boolean onNavigationItemSelected (MenuItem item) {

        int id = item.getItemId(); // Handle navigation view item clicks here.

        if (id == R.id.nav_sign_in) {
            // sign in activity
            Intent intent = new Intent(this, LoginActivity_.class);
            startActivity(intent);
        } else if (id == R.id.map_style) {
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
        }
        else if (id == R.id.about) {
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

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
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
                                "Please choice destination place", false);
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
                makeDirection(data);
            }
        }
        if (requestCode == FAV_LIST_ACTIVITY_RESULT_CODE) {

            if (resultCode== RESULT_OK) {
                makeDirection(data);

            }
        }
    }

    public void makeDirection (Intent data) {

        lat1 = data.getDoubleExtra("lat", 10);
        lon1 = data.getDoubleExtra("lon", 10);
        String address = data.getStringExtra("address");

        // draw destination
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat1, lon1))
                .title(address)
                .snippet(address)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.pin_yellow)));


        Utils.sDestination =new LatLng(lat1, lon1);
        LatLng des = Utils.sDestination;
        byte way = 2;
        LatLng from = new LatLng(lat, lon);
        directions = new PlaceDirections(getApplicationContext()
                , mMap, from, des, way);

    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        // Action to be taken after selecting a spinner item
        // dua list marker = rong
        if (itemPosition > 0) {
            mMap.clear();
            Utils.sKeyPlace = compare[itemPosition];
            new LoadPlaces().execute();
            itemPosition = -1;
        }
        return true;

    }


    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 3000, null);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
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
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
