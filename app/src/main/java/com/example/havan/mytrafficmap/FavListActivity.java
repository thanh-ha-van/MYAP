package com.example.havan.mytrafficmap;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.havan.mytrafficmap.SQLite.DatabaseHandler;
import com.example.havan.mytrafficmap.view.CustomAdapter;
import com.example.havan.mytrafficmap.SQLite.DataModel;

import org.androidannotations.annotations.AfterViews;
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
    @ViewById(R.id.direc)
    ImageButton direc;
    @ViewById(R.id.delete)
    ImageButton delete;

    ListView listView;
    private static CustomAdapter adapter;

    @AfterViews
    public void afterViews() {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/SVN-Aguda Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        showInfo.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        direc.setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.list);

        DatabaseHandler db = new DatabaseHandler(this);

        final List<DataModel> favPlaces = db.getAllPLaces();


        adapter = new CustomAdapter(favPlaces, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showInfo.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                direc.setVisibility(View.VISIBLE);
            }
        });
    }
}
