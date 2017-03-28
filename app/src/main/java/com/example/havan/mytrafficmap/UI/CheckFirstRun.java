package com.example.havan.mytrafficmap.UI;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by NTT on 3/28/2017.
 */



public class CheckFirstRun {

    SharedPreferences pref;

    private SharedPreferences.Editor editor;

    public CheckFirstRun (Context context) {

        pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = pref.edit();
        // check if first run
        if (pref.getBoolean("firstrun", true)) {
            editor.putBoolean("firstrun", false);
            editor.putBoolean("show_traffic", true);
            editor.putInt("style", 1);
            editor.commit();
        }

    }
}
