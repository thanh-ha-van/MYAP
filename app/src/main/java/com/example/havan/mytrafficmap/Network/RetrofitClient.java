package com.example.havan.mytrafficmap.Network;

import com.google.gson.Gson;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by NTT on 3/10/2017.
 */

public class RetrofitClient {

    private static Retrofit retrofit = null;

    private RestServiceInterface restService;

    public RetrofitClient(){

        retrofit = new Retrofit.Builder()
                .baseUrl("http://ibss.io:5599")
                .addConverterFactory(GsonConverterFactory
                        .create(new Gson()))
                .build();
        restService = retrofit.create(RestServiceInterface.class);
    }
    public void getApi(Callback callback){
        restService
                .getApi()
                .enqueue(callback);
    }
}
