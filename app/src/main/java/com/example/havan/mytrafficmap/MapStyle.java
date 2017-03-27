package com.example.havan.mytrafficmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.havan.mytrafficmap.view.CustomGrid;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.activity_map_style)
public class MapStyle extends AppCompatActivity {

    @ViewById(R.id.grid)
    GridView gridView;

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private String[] web = {
            "Normal",
            "Silver",
            "Retro",
            "Dracula",
            "Night",
            "Aubergine",
            "grey_blue",
            "assassin"

    };
    private int[] imageId = {
            R.drawable.normal,
            R.drawable.silver,
            R.drawable.retro,
            R.drawable.dracula,
            R.drawable.dark,
            R.drawable.nigh2,
            R.drawable.grey,
            R.drawable.assassin
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
                        + web[+position], Toast.LENGTH_SHORT).show();

                switch (position) {
                    case 0:
                        editor.putInt("style", 1);
                        break;
                    case 1:
                        editor.putInt("style", 2);
                        break;
                    case 2:
                        editor.putInt("style", 3);
                        break;
                    case 3:
                        editor.putInt("map_style", 4);
                        break;
                    case 4:
                        editor.putInt("map_style", 5);
                        break;
                    case 5:
                        editor.putInt("map_style", 6);
                        break;
                    case 6:
                        editor.putInt("map_style", 7);
                        break;
                    case 7:
                        editor.putInt("map_style", 8);
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
