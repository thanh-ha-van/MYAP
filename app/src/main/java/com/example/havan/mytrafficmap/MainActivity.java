package com.example.havan.mytrafficmap;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuInflater;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnMyLocationButtonClickListener, LocationListener {

    private Geocoder geocoder;
    private  GoogleMap myMap;

    private ProgressDialog myProgress;

    private  final String myTag = "MYTAG";

    private final int requestIdAccessCourseFineLocation = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        myProgress = new ProgressDialog(this);
        myProgress.setTitle("Map Loading ...");
        myProgress.setMessage("Please wait...");
        myProgress.setCancelable(true);

        // Show Progress Bar
        myProgress.show();
        SupportMapFragment mapFragment
                = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map_fragment);


        // Set the event that map is ready
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                onMyMapReady(googleMap);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case requestIdAccessCourseFineLocation: {


                // Note: when the request is ignore. the result is null
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();

                    // show the current location
                    this.showMyLocation();
                }
                // cancel or deny
                else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    private void onMyMapReady(GoogleMap googleMap) {

        // Get the google map object
        myMap = googleMap;


        // set the event that map is loaded.
        myMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {

                // turn of the dialog
                myProgress.dismiss();

                // show user's location
                askPermissionsAndShowMyLocation();
            }
        });


        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.setMyLocationEnabled(true);
        myMap.setTrafficEnabled(true); // show traffic
    }

    // Find a locationProvider
    private String getEnabledLocationProvider() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        // criteria forr locationProvider
        Criteria criteria = new Criteria();

        // ==> "gps", "network",...
        String bestProvider = locationManager.getBestProvider(criteria, true);

        boolean enabled = locationManager.isProviderEnabled(bestProvider);

        if (!enabled) {
            Toast.makeText(this, "No location provider enabled!", Toast.LENGTH_LONG).show();
            Log.i(myTag,"No location provider enabled!");
            return null;
        }
        return bestProvider;
    }

    private void showMyLocation() {
        LocationManager locationManager =
                (LocationManager) getSystemService(LOCATION_SERVICE);
        String locationProvider = this.getEnabledLocationProvider();
        if (locationProvider == null) {
            return; }
        // Millisecond
        final long MIN_TIME_BW_UPDATES = 1000;
        final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
        Location myLocation = null;
        try {
            // Need user permisson (above)
            locationManager.requestLocationUpdates(
                    locationProvider,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
            // get the location
            myLocation = locationManager.getLastKnownLocation(locationProvider);
        }
        // Android API >= 23 have to catch SecurityException.
        catch (SecurityException e) {
            Toast.makeText(
                    this, "Show My Location Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(myTag, "Show My Location Error:" + e.getMessage());
            e.printStackTrace();
            return;
        }
        moveCamera(myLocation);
    }

    private void moveCamera(Location myLocation) {
        if (myLocation != null) {
            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            // TAdd Marker into Map:
            MarkerOptions option = new MarkerOptions();
            option.title("My Location");
            option.snippet("....");
            option.position(latLng);
            Marker currentMarker = myMap.addMarker(option);
            currentMarker.showInfoWindow();
        } else {
            Toast.makeText(this, "Location not found!", Toast.LENGTH_LONG).show();
            Log.i(myTag, "Location not found");
        }

    }

    private void askPermissionsAndShowMyLocation() {

        // ask user for location permisson
        if (Build.VERSION.SDK_INT >= 23) {
            int accessCoarsePermission
                    = ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int accessFinePermission
                    = ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION);


            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
                    || accessFinePermission != PackageManager.PERMISSION_GRANTED) {

                // Permissons needed
                String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION};

                // Show a dialog for above permisson
                ActivityCompat.requestPermissions(this, permissions,
                        requestIdAccessCourseFineLocation);

                return;
            }
        }

        // show the current location
        this.showMyLocation();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected (MenuItem item) {
        int id = item.getItemId(); // Handle navigation view item clicks here.

        if (id == R.id.nav_sign_in) {
            // Handle the camera action
        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_style) {
        } else if (id == R.id.style_default) {
            // do nothing
        } else if (id == R.id.style_gray_scale) {
            myMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.mapstyle_grayscale));

        } else if (id == R.id.style_night) {
            myMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.mapstyle_night));

        } else if (id == R.id.style_retro) {
            myMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.mapstyle_retro));

        } else if (id == R.id.nav_share) {
        }
// will be write later
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //phuong thuc danh dau
    private void addmarker(Place place) {
        Address address = null;
        List<Address> addressList = null;
        LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());

        try {
            addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (addressList.size() > 0)
                address = addressList.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if ((place != null) && (address != null)) {
            MarkerOptions mkoption = new MarkerOptions();
            String info = address.getAddressLine(0) + "," + address.getSubLocality() + "," + address.getSubAdminArea() + "," + address.getAdminArea();
            mkoption.position(latLng).title(place.getName());
            place.setInfoDetail(info);
            Marker marker = myMap.addMarker(mkoption);

            marker.showInfoWindow();


            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    //phuong thuc tim dia diem tren map
    private void searchAddress(GoogleMap map, String location) {
        if (location != null || !location.equals("")) {
            List<Address> addressList = null;
            try {
                addressList = geocoder.getFromLocationName(location, 5);


            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList.size() > 0) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(address.getAddressLine(0) + ","
                        + address.getSubLocality() + "," + address.getSubAdminArea() + ","
                        + address.getAdminArea()).position(latLng);
                Marker marker = map.addMarker(markerOptions);
                marker.showInfoWindow();
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
            } else {
                Toast.makeText(getBaseContext(), "Can not find the place.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}
