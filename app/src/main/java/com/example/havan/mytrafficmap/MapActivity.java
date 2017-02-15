package com.example.havan.mytrafficmap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MapActivity extends AppCompatActivity
        implements GoogleMap.OnMyLocationButtonClickListener, LocationListener {

    private GoogleMap myCurrentMap;

    private  ProgressDialog myProgress;

    private  final String myTag = "MYTAG";

    private final int reqestIdAccessCourseFineLocation = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);
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

        myCurrentMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        myCurrentMap.getUiSettings().setZoomControlsEnabled(true);
        myCurrentMap.setMyLocationEnabled(true);
        myCurrentMap.setTrafficEnabled(true); // show traffic

    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case reqestIdAccessCourseFineLocation: {


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
        myCurrentMap = googleMap;


        // set the event that map is loaded.
        myCurrentMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {

                // turn of the dialog
                myProgress.dismiss();

                // show user's location
                askPermissionsAndShowMyLocation();
            }
        });

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
            myCurrentMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            myCurrentMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            // TAdd Marker into Map:
            MarkerOptions option = new MarkerOptions();
            option.title("My Location");
            option.snippet("....");
            option.position(latLng);
            Marker currentMarker = myCurrentMap.addMarker(option);
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
                        reqestIdAccessCourseFineLocation);

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
}
