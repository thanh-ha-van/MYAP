package com.example.havan.mytrafficmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity (R.layout.activity_view_option)

public class ViewOption extends AppCompatActivity {


    @ViewById(R.id.switch_fav_place)
    Switch favPlace;
    @ViewById (R.id.switch_traffic)
    Switch showTraffic;
    @ViewById (R.id.switch_zoom)
    Switch zoom;
    @ViewById (R.id.btn_done)
    Button done;
    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    @AfterViews
    protected void AfterViews() {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/SVN-Aguda Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = pref.edit();

        // get the statement

        favPlace.setChecked(pref.getBoolean("show_fav_place", false));
        showTraffic.setChecked(pref.getBoolean("show_traffic",false));
        zoom.setChecked(pref.getBoolean("zoom",false));

        // set current statement to view

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewOption.this, "All saved", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        favPlace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    editor.putBoolean("show_fav_place", true);

                }else{

                    editor.putBoolean("show_fav_place", false);
                }

                editor.commit();

            }
        });


        showTraffic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    editor.putBoolean("show_traffic", true);

                }else{

                    editor.putBoolean("show_traffic", false);
                }

                editor.commit();

            }
        });

        zoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    editor.putBoolean("zoom", true);

                }else{

                    editor.putBoolean("zoom", false);
                }

                editor.commit();

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
