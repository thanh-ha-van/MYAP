package com.example.havan.mytrafficmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

@EActivity(R.layout.activity_map_style)
public class MapStyle extends AppCompatActivity  {

    @ViewById(R.id.grid)
    private GridView gridView;

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private String[] web = {
            "Normal",
            "Silver",
            "Retro",
            "Dracula",
            "Night",
            "Aubergine"

    } ;
    private int[] imageId = {
            R.drawable.normal,
            R.drawable.silver,
            R.drawable.retro,
            R.drawable.nigh,
            R.drawable.dark,
            R.drawable.nigh2

    };

    @AfterViews
    public void afterViews() {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/SVN-Aguda Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = pref.edit();

        CustomGrid adapter = new CustomGrid(MapStyle.this, web, imageId);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MapStyle.this, "Activated style "
                        + web[+ position], Toast.LENGTH_SHORT).show();

             switch (position) {
                 case 0: editor.putString("map_style", "normal");
                     break;
                 case 1: editor.putString("map_style","silver");
                     break;
                 case 2: editor.putString("map_style","retro");
                     break;
                 case 3: editor.putString("map_style","night");
                     break;
                 case 4: editor.putString("map_style","dark");
                     break;
                 case 5: editor.putString("map_style","aubergine");
                     break;
                 default:
             }
                editor.commit();
                onBackPressed();
            }
        });
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
