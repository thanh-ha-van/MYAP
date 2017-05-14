package com.example.havan.mytrafficmap.bus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ez.gg.myapplication.R;
import com.ez.gg.myapplication.api.services.WeatherAPI;
import com.ez.gg.myapplication.api.services.WeatherService;
import com.ez.gg.myapplication.models.directions.Busdirection;
import com.ez.gg.myapplication.models.weathers.WeatherData;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxJavaActivity extends AppCompatActivity implements RxJavaInterface.View {



    String origin = "place_id:ChIJ6Xfk9KXYdDERC-LOZbOu1Sk";
    String destination = "place_id:ChIJF3UZqksvdTERfG-iJoXacog";
    String mode = "transit";
    String transit_mode = "bus";
    String sensor = "false";
    String key = "AIzaSyCd2gUfR3QAHhNNov7z4hR_Y6LEV0ccljw";
    private RxJavaPresenter mPresenter;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        mPresenter = new RxJavaPresenter(this);
        mPresenter.getData(origin, destination, mode, transit_mode, sensor, key);
        textView = (TextView) findViewById(R.id.textView);

    }

    @Override
    public void showInfor(Busdirection info) {

        info.getStatus();
        info.getGeocodedWaypoints();

    }

    @Override
    public void showError(String error) {

    }
}
