package com.example.havan.mytrafficmap.bus;


import android.util.Log;


import com.example.havan.mytrafficmap.model.busDirection.Busdirection;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by HaVan on 5/8/2017.
 */

public class RxJavaPresenter implements RxJavaInterface.Presenter {


    private String mode = "transit";
    private String transit_mode = "bus";
    private String sensor = "false";
    private String key = "AIzaSyCd2gUfR3QAHhNNov7z4hR_Y6LEV0ccljw";

    private RxJavaInterface.View mView;

    private RxJavaModel model;

    private Observable<Busdirection> dataObservable;

    public RxJavaPresenter(RxJavaInterface.View mView) {

        this.mView = mView;
        this.model = new RxJavaModel(this);
    }

    public void getData(String origin, String dest) {

        dataObservable = model.getData(origin,
                dest,
                mode,
                transit_mode,
                sensor,
                key);
        getDirection(dataObservable);
    }

    @Override
    public void getDirection(final Observable<Busdirection> data) {


        data.subscribe(new Observer<Busdirection>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showError(e.toString());

            }

            @Override
            public void onNext(Busdirection busdirection) {
                mView.showInfor(busdirection);
            }
        });
    }
}
