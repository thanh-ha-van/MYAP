package com.example.havan.mytrafficmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.havan.mytrafficmap.SQLite.DatabaseHandler;
import com.example.havan.mytrafficmap.view.AlertDialogManager;
import com.example.havan.mytrafficmap.view.CustomAdapter;
import com.example.havan.mytrafficmap.SQLite.DataModel;
import com.example.havan.mytrafficmap.view.PlaceAutocompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.activity_search)
public class SearchActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    private AlertDialogManager alert = new AlertDialogManager();

    protected GoogleApiClient mGoogleApiClient;

    private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteView;

    // data sending back to main activity.
    private String placeId = null;
    private LatLng latLng = null;
    public String placeName = null;
    public String placeAddress = null;
    private TextView mPlaceDetailsName;
    private TextView mPlaceDetailsAddress;
    private TextView mPlaceDetailsPhone;
    private TextView mPlaceDetailsSite;

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));


    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomAdapter adapter;

    @AfterViews
    public void afterViews() {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/SVN-Aguda Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()

        );


        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = pref.edit();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);
        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        // Retrieve the TextViews that will display details and attributions of the selected place.
        mPlaceDetailsName = (TextView) findViewById(R.id.tv_place_name);
        mPlaceDetailsAddress = (TextView) findViewById(R.id.tv_address);
        mPlaceDetailsPhone = (TextView) findViewById(R.id.tv_phone);
        mPlaceDetailsSite = (TextView) findViewById(R.id.tv_site);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        mAutocompleteView.setAdapter(mAdapter);

        // Set up the 'clear text' button that clears the text in the autocomplete view
        Button clearButton = (Button) findViewById(R.id.button_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutocompleteView.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    };

    @Click(R.id.btn_direct)
    void directionClicked() {

        if (latLng != null) {


            // put the String to pass back into an Intent and close this activity
            Intent intent = new Intent();
            intent.putExtra("lat", latLng.latitude);
            intent.putExtra("lon", latLng.longitude);
            intent.putExtra("address", placeName);
            setResult(RESULT_OK, intent);
            finish();
            onBackPressed();
        } else {
            Toast.makeText(this, "Please search for a place first"
                    , Toast.LENGTH_SHORT).show();
        }
    }

    @Click(R.id.btn_add)
    void favClicked() {


        if (placeId != null) {
            DatabaseHandler db = new DatabaseHandler(this);
            if (db.checkIfExist(placeId)) {

                alert.showAlertDialog(
                        this,
                        "Whoop!",
                        "This place is already added to your favorite list",
                        2
                );
            } else {
                db.addPlace(new DataModel(placeName, placeAddress, placeId));
                // Reading all contacts
                Toast.makeText(this,
                        "Added to your favorite list",
                        Toast.LENGTH_SHORT).show();

            }
        }
        else   Toast.makeText(this,
                "Please search for a place first",
                Toast.LENGTH_SHORT).show();
    }

    @Click (R.id.btn_phone)
    void phoneClicked () {
    }

    @Click (R.id.btn_site)
    void siteClicked(){
    }
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            // Format details of the place for display and show it in a TextView.
            mPlaceDetailsAddress.setText(place.getAddress().toString());
            mPlaceDetailsName.setText(place.getName().toString());

            mPlaceDetailsPhone.setText("No phone number");
            if(place.getPhoneNumber()!="")
            mPlaceDetailsPhone.setText(place.getPhoneNumber().toString());

            mPlaceDetailsSite.setText("No website");
            if (place.getWebsiteUri() != null)
            mPlaceDetailsSite.setText(place.getWebsiteUri().toString());

            placeId = place.getId();
            latLng = place.getLatLng();
            placeName = place.getName().toString();
            placeAddress = place.getAddress().toString();

            places.release();
        }
    };



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Toast.makeText(this,
                "Could not connect to Google API Client: " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
