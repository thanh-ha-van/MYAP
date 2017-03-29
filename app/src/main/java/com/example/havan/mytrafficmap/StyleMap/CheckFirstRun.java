package com.example.havan.mytrafficmap.StyleMap;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by NTT on 3/29/2017.
 */

public class CheckFirstRun {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public CheckFirstRun (Context context) {

        pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = pref.edit();

        if (pref.getBoolean("firstrun", true)) {
            editor.putBoolean("firstrun", false);
            editor.putBoolean("show_traffic", true);
            editor.putString("map_style", "normal");
            editor.commit();
        }
    }
}
