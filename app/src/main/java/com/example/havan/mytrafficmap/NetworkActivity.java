package com.example.havan.mytrafficmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.Gson;
import com.orhanobut.hawk.GsonParser;
import com.orhanobut.hawk.Hawk;


public class NetworkActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        Hawk.init(this).setParser(new GsonParser(new Gson())).build();

    }
}
