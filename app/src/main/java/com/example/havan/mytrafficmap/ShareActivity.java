package com.example.havan.mytrafficmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.activity_share)
public class ShareActivity extends AppCompatActivity {

    @AfterViews
    public void afterViews() {
    }
}
