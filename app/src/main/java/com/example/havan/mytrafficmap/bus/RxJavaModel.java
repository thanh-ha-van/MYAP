package com.example.havan.mytrafficmap.bus;


import com.example.havan.mytrafficmap.model.busDirection.Busdirection;
import com.example.havan.mytrafficmap.service.DirectionService;

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
