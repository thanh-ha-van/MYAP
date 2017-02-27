package com.example.havan.mytrafficmap;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.havan.mytrafficmap.directions.PlaceDirections;
import com.example.havan.mytrafficmap.model.GPSTracker;
import com.example.havan.mytrafficmap.model.GooglePlaces;
import com.example.havan.mytrafficmap.model.Place;
import com.example.havan.mytrafficmap.model.Places;

import com.example.havan.mytrafficmap.view.AlertDialogManager;
import com.example.havan.mytrafficmap.view.ConnectionDetector;
import com.example.havan.mytrafficmap.view.ListAdapter;
import com.example.havan.mytrafficmap.view.SpinnerItem;
import com.example.havan.mytrafficmap.view.TitleNavigationAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MapStyleOptions;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.maps)
public class MainActivity extends AppCompatActivity
        implements ActionBar.OnNavigationListener, LocationListener {


    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    public String ListItemsName[] = new String[]{
            "Log in",
            "Your Location",
            "Favorite place",
            "Map style",
            "Show option",
            "Share",
    };
    public Integer ImageName[] = {
            R.drawable.ic_person,
            R.drawable.pin1,
            R.drawable.pin2,
            R.drawable.ic_style,
            R.drawable.ic_menu_manage,
            R.drawable.ic_menu_share,
            R.drawable.ic_earth,
    };
    public ListView listView;
    public ListAdapter listAdapter;


    // main variable
    private static String sKeyReference = "reference";

    private static String sKeyName = "name";
    //MapView m;

    //ui
    private android.support.v7.app.ActionBar actionBar;

    private ArrayList<SpinnerItem> navSpinner;

    private String[] value;

    private String[] compare;

    private TitleNavigationAdapter adapter;

    private boolean isInternet = false;

    private ConnectionDetector detector;

    private AlertDialogManager alert = new AlertDialogManager();

    private GPSTracker gps;

    private GooglePlaces googlePlaces;

    private Places listPlace;

    private double lat;

    private double lon;

    private double latTmp;

    private double lonTmp;

    private ProgressDialog pDialog;

    private GoogleMap mMap;

    private ArrayList<Marker> listMaker;

    private ArrayList<HashMap<String, String>> placesListItems
            = new ArrayList<HashMap<String, String>>();

    private PlaceDirections directions;

    private static String TAG = MainActivity.class.getSimpleName();

    @AfterViews
    public void afterViews() {

        // init UI
        initUI();

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
            Log.d("Your Location", "latitude:" + gps.getLatitude()
                    + ", longitude: " + gps.getLongitude());
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
            String query = intent.getStringExtra(SearchManager.QUERY);

            mMap.clear();
            Utils.sKeyPlace = query;
            new LoadPlaces().execute();
        }

    }

    private void initUI() {


        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
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
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        ////////////////
        listView = (ListView)findViewById(R.id.listView1);

        listAdapter = new ListAdapter(MainActivity.this , ListItemsName, ImageName);

        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), ListItemsName[position], Toast.LENGTH_LONG).show();
            }
        });

        ///////////////////////////////

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimary)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        actionBar = getSupportActionBar();

        value = getValue();
        compare = getValue1();

        addBar();

        adapter = new TitleNavigationAdapter(getApplicationContext(), navSpinner);
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

    private String[] getValue() {
        return getResources().getStringArray(R.array.items);
    }

    private String[] getValue1() {
        return getResources().getStringArray(R.array.compare);
    }

    private void addBar() {
        // Spinner title navigation data
        navSpinner = new ArrayList<SpinnerItem>();
        navSpinner.add(new SpinnerItem(value[0], R.drawable.airport));
        navSpinner.add(new SpinnerItem(value[1], R.drawable.atm));
        navSpinner.add(new SpinnerItem(value[2], R.drawable.bank));
        navSpinner.add(new SpinnerItem(value[3], R.drawable.bar));
        navSpinner.add(new SpinnerItem(value[4], R.drawable.cafe));
        navSpinner.add(new SpinnerItem(value[5], R.drawable.church));
        navSpinner.add(new SpinnerItem(value[6], R.drawable.food));
        navSpinner.add(new SpinnerItem(value[7], R.drawable.hospital));
        navSpinner.add(new SpinnerItem(value[8], R.drawable.hotel));
        navSpinner.add(new SpinnerItem(value[9], R.drawable.library));
        navSpinner.add(new SpinnerItem(value[10], R.drawable.police));
        navSpinner.add(new SpinnerItem(value[11], R.drawable.supermarket));
        navSpinner.add(new SpinnerItem(value[12], R.drawable.theater));
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

        //mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setTrafficEnabled(true); // show traffic
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        //mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
        //        this, R.raw.mapstyle_night));
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

    }


    private void getcurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
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
                            for (Place p : listPlace.results) {
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
                        getcurrentLocation();

                        // draw my position
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lon))
                                .title("Me")
                                .snippet("Local of me")
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.pin2)));

                        if (listPlace.results != null) {
                            // loop through all the places
                            for (Place place : listPlace.results) {
                                latTmp = place.geometry.location.lat; // latitude
                                lonTmp = place.geometry.location.lng; // longitude

                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latTmp, lonTmp))
                                        .title(place.name)
                                        .snippet(place.vicinity)
                                        .icon(BitmapDescriptorFactory
                                                .fromResource(R.drawable.pin1)));

                                listMaker.add(marker);
                            }
                        }
                    } else if (status.equals("ZERO_RESULTS")) {
                        // Zero results found
                        alert.showAlertDialog(MainActivity.this, "Near Places",
                                "Sorry no places found. Try to change the types of places",
                                false);
                    } else if (status.equals("UNKNOWN_ERROR")) {
                        alert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry unknown error occured.",
                                false);
                    } else if (status.equals("OVER_QUERY_LIMIT")) {
                        alert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry query limit to google places is reached",
                                false);
                    } else if (status.equals("REQUEST_DENIED")) {
                        alert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry error occured. Request is denied",
                                false);
                    } else if (status.equals("INVALID_REQUEST")) {
                        alert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry error occured. Invalid Request",
                                false);
                    } else {
                        alert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry error occured.",
                                false);
                    }
                }
            });

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {

            case R.id.direc: {
                // get type way
                byte way = Utils.sKeyWay;
                // get sDestination
                LatLng des = Utils.sDestination;
                // get ways by listview + show on map
                {
                    if (des == null) {
                        alert.showAlertDialog(this, "Place empty",
                                "Please choice sDestination place", false);
                    } else {
                        LatLng from = new LatLng(lat, lon);
                        directions = new PlaceDirections(getApplicationContext()
                                , mMap, from, des, way);
                    }
                }
                return true;
            }
            case R.id.action_settings: {
                alert.showAlertDialog(this, "GG",
                        "GG end fast pls", false);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
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
