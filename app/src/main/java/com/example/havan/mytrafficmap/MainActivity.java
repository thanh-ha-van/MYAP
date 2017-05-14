package com.example.havan.mytrafficmap;

import android.app.ActionBar;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.havan.mytrafficmap.Route.DrawRouteOffline;
import com.example.havan.mytrafficmap.Route.RouteDatabaseHandler;
import com.example.havan.mytrafficmap.Route.RouteListActivity_;
import com.example.havan.mytrafficmap.Route.RouteModel;
import com.example.havan.mytrafficmap.ShowOnMap.Movecamera;
import com.example.havan.mytrafficmap.ShowOnMap.SetOptionView;
import com.example.havan.mytrafficmap.ShowOnMap.ShowFavorite;
import com.example.havan.mytrafficmap.ShowOnMap.ShowPlace;
import com.example.havan.mytrafficmap.StyleMap.CheckFirstRun;
import com.example.havan.mytrafficmap.StyleMap.SetStyle;
import com.example.havan.mytrafficmap.directions.GetDistance;
import com.example.havan.mytrafficmap.directions.PlaceDirections;
import com.example.havan.mytrafficmap.model.GPSTracker;
import com.example.havan.mytrafficmap.view.AlertDialogManager;
import com.example.havan.mytrafficmap.view.ConnectionDetector;
import com.example.havan.mytrafficmap.view.TitleNavigationAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

@EActivity(R.layout.maps)
public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        ActionBar.OnNavigationListener,
        LocationListener,
        GoogleMap.OnInfoWindowClickListener {

    public PlaceDirections directions;

    public GoogleMap mMap;

    public SetStyle mapStyle;
    //ui
    public android.support.v7.app.ActionBar actionBar;

    private ActionBarDrawerToggle mDrawerToggle;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;

    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private TitleNavigationAdapter adapter;

    private boolean isInternet = false;

    private ConnectionDetector detector;

    private AlertDialogManager alert = new AlertDialogManager();

    private GPSTracker gps;

    public ShowPlace showPlace;

    private double lat;

    private double lon;

    private double lat1;

    private double lon1;

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private static final int SECOND_ACTIVITY_RESULT_CODE = 0;

    private static final int FAV_LIST_ACTIVITY_RESULT_CODE = 11;

    private static final int ROUTE_ACTIVITY_RESULT_CODE = 22;

    @AfterViews
    public void afterViews() {

        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = pref.edit();

        new CheckFirstRun(this);

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
            alert.showAlertDialog(this, "No Internet",
                    "Please connect to working Internet connection", 2);
        }
        // check able of gps
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {

            lat = gps.getLatitude();
            lon = gps.getLongitude();

        } else {
            // Can't get user's current location
            alert.showAlertDialog(this, "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    2);
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
            new ShowFavorite(this, mMap, pref.getBoolean("show_fav_place", false));
            Utils.sKeyPlace = query;
            showPlace = new ShowPlace(getApplicationContext(), MainActivity.this, mMap);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        double mlat = marker.getPosition().latitude;
        double mlon = marker.getPosition().longitude;
        String dis = "unknow";
        GetDistance getDistance = new GetDistance(lat, lon, mlat, mlon);
        dis = getDistance.getTheDistance();
        alert.showAlertDialog(this, "Information",
                "Name: " + marker.getTitle()
                        + "\n\nAddress: " + marker.getSnippet()
                        + "\n\nDistance: "
                        + dis
                , 3);
    }

    private void initUi() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        navigationView.setNavigationItemSelectedListener(this);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        actionBar = getSupportActionBar();

        adapter = new TitleNavigationAdapter(getApplicationContext());
        getSupportActionBar().setListNavigationCallbacks(adapter,
                new android.support.v7.app.ActionBar.OnNavigationListener() {
                    @Override
                    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                        // Action to be taken after selecting a spinner item
                        // dua list marker = rong
                        if (itemPosition > -1) {
                            mMap.clear();

                            new ShowFavorite(getApplicationContext(),
                                    mMap,
                                    pref.getBoolean("show_fav_place", false));

                            Utils.sKeyPlace = adapter.getName(itemPosition);
                            showPlace = new ShowPlace(getApplicationContext(),
                                    MainActivity.this,
                                    mMap);
                            itemPosition = -1;
                        }
                        return true;
                    }
                });
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
        mapStyle = new SetStyle(this, mMap);
        new SetOptionView(mMap, this);
        new Movecamera(mMap, lat, lon);

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
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {

                myPolylineClicked();
            }
        });
    }

    public void myPolylineClicked() {

        RouteDatabaseHandler db = new RouteDatabaseHandler(this.getApplicationContext());
        if (db.checkIfExist(Utils.sTrDestination)) {

            alert.showAlertDialog(
                    this,
                    "Whoop!",
                    "This route is already added to your favorite list",
                    2
            );
        } else {

            db.addRoute(new RouteModel(Utils.sTrDestination, Utils.sTrSnippet, lat, lon, Utils.sRoute));
            // Reading all contacts
            Toast.makeText(this,
                    "Saved to your route list",
                    Toast.LENGTH_SHORT).show();

        }
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

        } else if (id == R.id.route_fav) {

            Intent intent = new Intent(this, RouteListActivity_.class);
            startActivityForResult(intent, ROUTE_ACTIVITY_RESULT_CODE);

        } else if (id == R.id.fav_place) {


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
                                "Please choice destination place", 2);
                    } else {
                        LatLng from = new LatLng(lat, lon);
                        directions = new PlaceDirections(
                                getApplicationContext(), MainActivity.this, mMap, from, des);
                    }
                }
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

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

            if (resultCode == RESULT_OK) {
                makeDirection(data);

            }
        }
        if (requestCode == ROUTE_ACTIVITY_RESULT_CODE) {

            if (resultCode == RESULT_OK) {

              new DrawRouteOffline(this, mMap, data);

            }
        }

    }

    public void makeDirection(Intent data) {

        lat1 = data.getDoubleExtra("lat", 10);
        lon1 = data.getDoubleExtra("lon", 10);
        String address = data.getStringExtra("address");
        String name = data.getStringExtra("name");

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat1, lon1))
                .title(name)
                .snippet(address)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_new_red)));

        Utils.sDestination = new LatLng(lat1, lon1);
        LatLng des = Utils.sDestination;
        Utils.sTrDestination = name;
        Utils.sTrSnippet = address;
        LatLng from = new LatLng(lat, lon);

        directions = new PlaceDirections(
                getApplicationContext(), MainActivity.this, mMap, from, des);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        lon = location.getLongitude();
        new Movecamera(mMap, lat, lon);
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
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}