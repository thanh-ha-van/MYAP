package com.example.havan.mytrafficmap;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.havan.mytrafficmap.view.CustomAdapter;
import com.example.havan.mytrafficmap.view.DataModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

import java.util.ArrayList;


@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.activity_fav_list)
public class FavListActivity extends AppCompatActivity {

    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomAdapter adapter;

    @AfterViews
    public void afterViews() {

        listView=(ListView)findViewById(R.id.list);

        dataModels= new ArrayList<>();
        dataModels.add(new DataModel("Apple Pie", "Android 1.0", "1","September 23, 2008"));
        dataModels.add(new DataModel("Banana Bread", "Android 1.1", "2","February 9, 2009"));
        dataModels.add(new DataModel("Cupcake", "Android 1.5", "3","April 27, 2009"));
        dataModels.add(new DataModel("Donut","Android 1.6","4","September 15, 2009"));
        dataModels.add(new DataModel("Eclair", "Android 2.0", "5","October 26, 2009"));
        dataModels.add(new DataModel("Froyo", "Android 2.2", "8","May 20, 2010"));
        dataModels.add(new DataModel("Gingerbread", "Android 2.3", "9","December 6, 2010"));
        dataModels.add(new DataModel("Honeycomb","Android 3.0","11","February 22, 2011"));
        dataModels.add(new DataModel("Ice Cream Sandwich", "Android 4.0", "14","October 18, 2011"));
        dataModels.add(new DataModel("Jelly Bean", "Android 4.2", "16","July 9, 2012"));
        dataModels.add(new DataModel("Kitkat", "Android 4.4", "19","October 31, 2013"));
        dataModels.add(new DataModel("Lollipop","Android 5.0","21","November 12, 2014"));
        dataModels.add(new DataModel("Marshmallow", "Android 6.0", "23","October 5, 2015"));

        adapter= new CustomAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataModel dataModel= dataModels.get(position);

                Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getType()
                        +" API: "+dataModel.getVersion_number(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }
}
