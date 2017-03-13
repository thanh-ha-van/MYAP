package com.example.havan.mytrafficmap;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.havan.mytrafficmap.SQLite.DatabaseHandler;
import com.example.havan.mytrafficmap.view.CustomAdapter;
import com.example.havan.mytrafficmap.SQLite.DataModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

import java.util.ArrayList;
import java.util.List;


@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.activity_fav_list)
public class FavListActivity extends AppCompatActivity {

    ListView listView;
    private static CustomAdapter adapter;

    @AfterViews
    public void afterViews() {


        listView=(ListView)findViewById(R.id.list);

        DatabaseHandler db = new DatabaseHandler(this);

        final List<DataModel> favPlaces = db.getAllPLaces();


        adapter= new CustomAdapter(favPlaces,getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataModel dataModel= favPlaces.get(position);

                Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getAddress()
                        +" API: ", Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }
}
