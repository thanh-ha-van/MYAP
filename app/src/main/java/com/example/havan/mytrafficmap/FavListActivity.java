package com.example.havan.mytrafficmap;

import android.app.Dialog;

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


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.activity_fav_list)
public class FavListActivity extends AppCompatActivity {


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

            Intent intent = new Intent();
            intent.putExtra("lat", adapter.getItem(currentPosition).getPlaceLat());
            intent.putExtra("lon", adapter.getItem(currentPosition).getPlaceLon());
            intent.putExtra("address", adapter.getItem(currentPosition).getAddress());
            intent.putExtra("name", adapter.getItem(currentPosition).getName());

            setResult(RESULT_OK, intent);
            finish();
        }
    else

    {
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
    public void onBackPressed() {

        super.onBackPressed();

    }
}
