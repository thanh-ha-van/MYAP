package com.example.havan.mytrafficmap.bus;

import com.ez.gg.myapplication.api.services.DirectionService;
import com.ez.gg.myapplication.api.services.WeatherService;
import com.ez.gg.myapplication.models.directions.Busdirection;
import com.ez.gg.myapplication.models.weathers.WeatherData;

import rx.Observable;


/**
 * Created by HaVan on 5/8/2017.
 */

public class RxJavaModel {


    private DirectionService service = new DirectionService();

    private RxJavaInterface.Presenter mPresenter;

    public RxJavaModel(RxJavaInterface.Presenter mPresenter) {
        this.mPresenter = mPresenter;

    }

    public Observable<Busdirection> getData(String origin,
                                            String dest,
                                            String mode,
                                            String transmode,
                                            String sensor,
                                            String key) {

        return service.getJsonData(
                origin,
                dest,
                mode,
                transmode,
                sensor,
                key);
    }
}
