package com.example.havan.mytrafficmap.bus;

import com.ez.gg.myapplication.models.directions.Busdirection;
import com.ez.gg.myapplication.models.weathers.WeatherData;

import rx.Observable;

/**
 * Created by HaVan on 5/8/2017.
 */

public interface RxJavaInterface {

    interface View {


        void showInfor(Busdirection info);
        void showError(String error);
    }

    interface Presenter {

        void getDirection(Observable<Busdirection> data);
    }


}
