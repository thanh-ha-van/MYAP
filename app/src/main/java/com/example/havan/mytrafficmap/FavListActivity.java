package com.example.havan.mytrafficmap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.havan.mytrafficmap.SQLite.DatabaseHandler;
import com.example.havan.mytrafficmap.view.AlertDialogManager;
import com.example.havan.mytrafficmap.view.CustomAdapter;
import com.example.havan.mytrafficmap.SQLite.DataModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.activity_fav_list)
public class FavListActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    AlertDialogManager alert = new AlertDialogManager();

    String title = "Confirm";
    String message = "Are you sure to delete this place?";

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

    String currentId = null;
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
                currentId = adapter.getItem(position).getPlaceID();
                currentPosition = position;
            }
        });
    }

    @Click(R.id.direct)
    void directClicked() {

        if (currentPosition != -1) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, 0 /* clientId */, this)
                    .addApi(Places.GEO_DATA_API)
                    .build();


            Places.GeoDataApi.getPlaceById(
                    mGoogleApiClient,
                    currentId
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
                                intent.putExtra("address",
                                        myPlace.getAddress()
                                                + "\n"
                                                + myPlace.getId()
                                );

                                setResult(RESULT_OK, intent);
                                finish();
                            }
                            places.release();
                        }
                    });
        } else {
            Toast.makeText(
                    FavListActivity.this,
                    "Please choose a place first!",
                    Toast.LENGTH_SHORT
            ).show();
        }

    }


    @Click(R.id.show_info)
    void infoClicked() {


        if (currentPosition != -1) {

            alert.showAlertDialog(
                    this,
                    "Information",

                    "Place: " + adapter.getItem(currentPosition).getName()
                            + "\nAddress: "
                            + adapter.getItem(currentPosition).getAddress()
                            + "\nID: "
                            + adapter.getItem(currentPosition).getPlaceID(),
                    3
            );

        } else Toast.makeText(
                FavListActivity.this,
                "Please choose a place first!",
                Toast.LENGTH_SHORT
        ).show();

    }

    @Click(R.id.btn_add_place)
    void addPlace() {
        onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(), SearchActivity_.class));

    }

    @Click(R.id.delete)
    void delClicked() {

        if (currentPosition != -1) {

            {
                final Dialog alertDialog = new Dialog(this);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow()
                        .setBackgroundDrawable(new ColorDrawable(
                                        android
                                                .graphics
                                                .Color
                                                .TRANSPARENT
                                )
                        );
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.setContentView(R.layout.yes_no_dialog_layout);

                TextView tvTitle = (TextView) alertDialog.findViewById(R.id.tv_title);
                tvTitle.setText(title);

                TextView tvContent = (TextView) alertDialog.findViewById(R.id.tv_content);
                tvContent.setText(message);

                ImageButton dialogButton = (ImageButton)
                        alertDialog.findViewById(R.id.btn_ok);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                        db.deletePlace(adapter.getItem(currentPosition));
                        afterViews();

                    }
                });

                ImageButton cancelButton = (ImageButton)
                        alertDialog.findViewById(R.id.btn_cancel);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
                alertDialog.show();

            }
        } else Toast.makeText(
                FavListActivity.this,
                "Please choose a place first!",
                Toast.LENGTH_SHORT
        ).

                show();

    }

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
