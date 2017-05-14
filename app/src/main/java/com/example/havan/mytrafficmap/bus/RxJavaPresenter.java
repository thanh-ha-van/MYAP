package com.example.havan.mytrafficmap.bus;


import android.util.Log;

import com.ez.gg.myapplication.models.directions.Busdirection;
import com.ez.gg.myapplication.models.weathers.Weather;
import com.ez.gg.myapplication.models.weathers.WeatherData;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by HaVan on 5/8/2017.
 */

public class RxJavaPresenter implements RxJavaInterface.Presenter {

    private RxJavaInterface.View mView;

    private RxJavaModel model;

    private Observable<Busdirection> dataObservable;

    public RxJavaPresenter(RxJavaInterface.View mView) {

        this.mView = mView;
        this.model = new RxJavaModel(this);
    }

    public void getData(String origin,
                        String dest,
                        String mode,
                        String transmode,
                        String sensor,
                        String key) {

        dataObservable = model.getData( origin,
                dest,
                mode,
                transmode,
                sensor,
                key);
        getDirection(dataObservable);
    }

    @Override
    public void  getDirection(final Observable<Busdirection> data) {

        //
        data.subscribe(busdirection ->
                {
                    mView.showInfor(busdirection);

                },
                (Throwable error) ->
                {
                    mView.showError(error.toString());
                });
    }
}
