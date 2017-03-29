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
import android.widget.Toast;

<<<<<<< HEAD
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
=======
import com.example.havan.mytrafficmap.ShowOnMap.Movecamera;
import com.example.havan.mytrafficmap.ShowOnMap.SetOptionView;
import com.example.havan.mytrafficmap.ShowOnMap.ShowFavorite;
import com.example.havan.mytrafficmap.ShowOnMap.ShowPlace;
import com.example.havan.mytrafficmap.StyleMap.CheckFirstRun;
import com.example.havan.mytrafficmap.StyleMap.SetStyle;
import com.example.havan.mytrafficmap.directions.PlaceDirections;
import com.example.havan.mytrafficmap.model.GPSTracker;
>>>>>>> Course_New
import com.example.havan.mytrafficmap.model.MyPlace;
import com.example.havan.mytrafficmap.model.MyPlaces;
import com.example.havan.mytrafficmap.view.AlertDialogManager;
import com.example.havan.mytrafficmap.view.TitleNavigationAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
<<<<<<< HEAD
import com.google.common.collect.Sets;
=======
>>>>>>> Course_New

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

<<<<<<< HEAD
=======
    public SetStyle mapStyle;
>>>>>>> Course_New
    //ui
    public android.support.v7.app.ActionBar actionBar;
    private ActionBarDrawerToggle mDrawerToggle;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;

    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private TitleNavigationAdapter adapter;


    private AlertDialogManager alert = new AlertDialogManager();

<<<<<<< HEAD
=======
    private GPSTracker gps;

    public ShowPlace showPlace;

>>>>>>> Course_New
    private double lat;
    private double lon;


<<<<<<< HEAD
    CheckConnection check;
    CheckFirstRun checkFirstRun;
    ReadyMap readyMap;
    setMapStyle setMapStyle;
    setView setView;

=======
>>>>>>> Course_New
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private static final int SECOND_ACTIVITY_RESULT_CODE = 0;
    private static final int FAV_LIST_ACTIVITY_RESULT_CODE = 0;


    @AfterViews
    public void afterViews() {

<<<<<<< HEAD
=======
        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = pref.edit();

        new CheckFirstRun(this);

>>>>>>> Course_New
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/SVN-Aguda Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
<<<<<<< HEAD

        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = pref.edit();

        checkFirstRun = new CheckFirstRun(this); // check first run settings
        // init UI
        initUi();
        loadMap();
=======
        // init UI
        initUi();
        // check internet
        detector = new ConnectionDetector(this.getApplicationContext());
        isInternet = detector.isConnectingToInternet();
        if (!isInternet) {
            // Internet Connection is not present
            alert.showAlertDialog(this, "Internet Connection Error",
                    "Please connect to working Internet connection", 2);
            // stop executing code by return
            return;
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

>>>>>>> Course_New
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
<<<<<<< HEAD
            //new LoadPlaces().execute();
            showPlace = new ShowPlace(getApplicationContext(),MainActivity.this, mMap);
        }
=======
            showPlace = new ShowPlace(getApplicationContext(), MainActivity.this, mMap);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        alert.showAlertDialog(this, "Information",
                marker.getTitle()
                        + "\n\n"
                        + marker.getSnippet()
                        + "\n\n"
                        + ""
                , 3);
>>>>>>> Course_New
    }

    private void initUi() {

        new InitSideMenu(
                this.getApplicationContext(),
                MainActivity.this,
                mDrawerToggle, navigationView, mDrawerLayout );

        navigationView.setNavigationItemSelectedListener(this);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_LIST);

        actionBar = getSupportActionBar();

        adapter = new TitleNavigationAdapter(getApplicationContext());

        getSupportActionBar().setListNavigationCallbacks(adapter,
                new android.support.v7.app.ActionBar.OnNavigationListener() {
                    @Override
                    public boolean onNavigationItemSelected(int itemPosition, long itemId) {

                        if (itemPosition > -1) {
                            mMap.clear();

                            new ShowFavorite(getApplicationContext(),
                                    mMap,
                                    pref.getBoolean("show_fav_place", false));

                            Utils.sKeyPlace = adapter.getName(itemPosition);
<<<<<<< HEAD
                            showPlace = new ShowPlace(getApplicationContext(), MainActivity.this, mMap);
=======
                            showPlace = new ShowPlace(getApplicationContext(),
                                    MainActivity.this,
                                    mMap);
>>>>>>> Course_New
                            itemPosition = -1;
                        }
                        return true;
                    }
                });
<<<<<<< HEAD



=======
>>>>>>> Course_New
    }

    private void loadMap() {

        final SupportMapFragment mapFragment
                = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        // Set the event that map is ready
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
<<<<<<< HEAD
                mMap = googleMap;
                readyMap = new ReadyMap(getApplicationContext(), mMap , lat, lon);
                setView = new setView (getApplicationContext(), mMap);
                setMapStyle = new setMapStyle(getApplicationContext(), mMap);
            }
        });

=======
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
>>>>>>> Course_New
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
                direction = new Direction(this, mMap, data, lat, lon);
            }
        }
        if (requestCode == FAV_LIST_ACTIVITY_RESULT_CODE) {

            if (resultCode == RESULT_OK) {
                direction = new Direction(this, mMap, data, lat, lon);
            }
        }
    }

<<<<<<< HEAD
=======
    public void makeDirection(Intent data) {

        lat1 = data.getDoubleExtra("lat", 10);
        lon1 = data.getDoubleExtra("lon", 10);
        String address = data.getStringExtra("address");
        String name = data.getStringExtra("name");

        // draw destination
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat1, lon1))
                .title(name)
                .snippet(address)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_new_red)));

        Utils.sDestination = new LatLng(lat1, lon1);
        LatLng des = Utils.sDestination;
        LatLng from = new LatLng(lat, lon);

        directions = new PlaceDirections(
                getApplicationContext(), MainActivity.this, mMap, from, des);
    }

>>>>>>> Course_New
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
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}