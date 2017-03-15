package com.example.havan.mytrafficmap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
public class FavListActivity extends AppCompatActivity {

    @ViewById(R.id.show_info)
    ImageButton showInfo;
    @ViewById(R.id.direct)
    ImageButton direct;
    @ViewById(R.id.delete)
    ImageButton delete;
    DatabaseHandler db;
    ListView listView;
    private static CustomAdapter adapter;

    int currentPosition = -1;

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
            public void onItemClick (
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id)
            {
                adapter.setCheck(position);
                currentPosition = position;
            }
        });
    }
    @Click (R.id.direct)
    void directClicked (){

    }

    @Click (R.id.show_info)
    void infoClicked (){

    }

    @Click (R.id.delete)
    void delClicked (){

        AlertDialog.Builder ab = new AlertDialog.Builder(FavListActivity.this);
        ab.setMessage("Are you sure to delete this place?")
                .setPositiveButton("YES", dialogClickListener)
                .setNegativeButton("NO", dialogClickListener).show();

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    if (currentPosition !=-1) {
                        db.deletePlace(adapter.getItem(currentPosition));
                        afterViews();

                    }
                    else   Toast.makeText(
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
}
