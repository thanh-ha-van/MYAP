package com.example.havan.mytrafficmap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;

public class StyleMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myCurrentMap = null;

    //private final String TAG = StyleMap.class.getSimpleName();

    private final String selectedStyle = "selected_style";

    // Stores the ID of the currently selected style, so that we can re-apply it when
    // the activity restores state, for example when the device changes orientation.
    private int mSelectedStyleId = R.string.map_style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mSelectedStyleId = savedInstanceState.getInt(selectedStyle);
        }
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().
                        findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Store the selected map style, so we can assign it when the activity resumes.
        outState.putInt(selectedStyle, mSelectedStyleId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        myCurrentMap = map;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_style) {
            setSelectedStyle();
        }
        return true;
    }

    private void setSelectedStyle() {
        MapStyleOptions style;
        switch (mSelectedStyleId) {
            case R.string.style_label_retro:
                style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_retro);
                break;
            case R.string.style_label_night:
                style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_night);
                break;
            case R.string.style_label_gray_scale:
                style = new MapStyleOptions("[" +
                        "  {" +
                        "    \"featureType\":\"poi.business\"," +
                        "    \"elementType\":\"all\"," +
                        "    \"stylers\":[" +
                        "      {" +
                        "        \"visibility\":\"off\"" +
                        "      }" +
                        "    ]" +
                        "  }," +
                        "  {" +
                        "    \"featureType\":\"transit\"," +
                        "    \"elementType\":\"all\"," +
                        "    \"stylers\":[" +
                        "      {" +
                        "        \"visibility\":\"off\"" +
                        "      }" +
                        "    ]" +
                        "  }" +
                        "]");
                break;
            case R.string.style_label_default:
                // Removes previously set style, by setting it to null.
                style = null;
                break;
            default:
                return;
        }
        myCurrentMap.setMapStyle(style);
    }

}