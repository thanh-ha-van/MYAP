package com.example.havan.mytrafficmap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.havan.mytrafficmap.Network.Object.Data;
import com.example.havan.mytrafficmap.SQLite.DatabaseHandler;
import com.example.havan.mytrafficmap.view.CustomAdapter;
import com.example.havan.mytrafficmap.SQLite.DataModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.activity_fav_list)
public class FavListActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener  {

    @ViewById(R.id.show_info)
    ImageButton showInfo;
    @ViewById(R.id.direct)
    ImageButton direct;
    @ViewById(R.id.delete)
    ImageButton delete;
    DatabaseHandler db;
    ListView listView;
    private static CustomAdapter adapter;
    protected GoogleApiClient mGoogleApiClient;

    int currentPosition = -1;
    public String info;

    @AfterViews
    public void afterViews() {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/SVN-Aguda Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        listView = (ListView) findViewById(R.id.list);

        db = new DatabaseHandler(this);

        final List<DataModel> favPlaces = db.getAllPLaces();

        adapter = new CustomAdapter(favPlaces, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id) {
                adapter.setCheck(position);
                currentPosition = position;
            }
        });
    }

    @Click(R.id.direct)
    void directClicked() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();


        Places.GeoDataApi.getPlaceById(
                mGoogleApiClient,
                db.getPlace(currentPosition).getPlaceID()
        )
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (
                                places.getStatus()
                                        .isSuccess()) {
                            Intent intent = new Intent();
                            //intent.putExtra("lat", latLng.latitude);
                            final Place myPlace = places.get(0);
                            intent.putExtra("lat", myPlace.getLatLng().latitude);
                            intent.putExtra("lon", myPlace.getLatLng().longitude);
                            intent.putExtra("address", myPlace.getName().toString());

                            setResult(RESULT_OK, intent);
                            finish();
                            onBackPressed();
                        }
                        places.release();
                    }
                });

    }



    @Click(R.id.show_info)
    void infoClicked() {


        info =  "Name: "+ adapter.getItem(currentPosition).getName()
                + "\nAddress: "
                + adapter.getItem(currentPosition).getAddress();

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Information");
        builder1.setMessage(info);
        builder1.setCancelable(true);
        builder1.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    @Click(R.id.delete)
    void delClicked() {

        AlertDialog.Builder ab = new AlertDialog.Builder(FavListActivity.this);
        ab.setMessage("Are you sure to delete this place?")
                .setPositiveButton("YES", dialogClickListener)
                .setNegativeButton("NO", dialogClickListener).show();

    }


    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (currentPosition != -1) {
                        db.deletePlace(adapter.getItem(currentPosition));
                        afterViews();

                    } else Toast.makeText(
                            FavListActivity.this,
                            "Please choose a place first!",
                            Toast.LENGTH_SHORT
                    ).show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
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
}
