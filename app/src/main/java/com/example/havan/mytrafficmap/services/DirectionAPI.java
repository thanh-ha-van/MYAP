package com.example.havan.mytrafficmap.services;



import com.example.havan.mytrafficmap.model.bus.Busdirection;

import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface DirectionAPI {


    @GET("maps/api/directions/json")
    Observable<Busdirection> getJson(@Query("origin") String origin,
                                     @Query("destination") String destination,
                                     @Query("mode") String mode,
                                     @Query("transit_mode") String transmode,
                                     @Query("sensor") String sensor,
                                     @Query("key") String key
    );

}
