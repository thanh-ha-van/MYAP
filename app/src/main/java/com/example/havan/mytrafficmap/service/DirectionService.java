package com.example.havan.mytrafficmap.service;


import com.example.havan.mytrafficmap.model.busDirection.Busdirection;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class DirectionService {


    private Retrofit retrofit;

    String base_url = "https://maps.googleapis.com/";



    public DirectionService() {

        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(base_url)
                .build();

    }

    public Observable<Busdirection> getJsonData(
            String origin,
            String dest,
            String mode,
            String transmode,
            String sensor,
            String key) {

        Observable<Busdirection> observable;
        observable = retrofit
                .create(DirectionAPI.class)
                .getJson(origin, dest, mode, transmode, sensor, key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        return observable;
    }
}
