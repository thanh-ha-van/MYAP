package com.example.havan.mytrafficmap.Network.Service;

import com.example.havan.mytrafficmap.Network.Object.Account;
import com.example.havan.mytrafficmap.Network.Object.FullData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by HaVan on 3/13/2017.
 */
public interface RestService {

    @FormUrlEncoded
    @POST("/api/token")
    Call<Account> login(
            @Field("grant_type") String grant_type,
            @Field("username") String username,
            @Field("password") String password);


    @Headers("Accept: application/vnd.app.atoms.mobile-v1+json")
    @GET("/api/stories")
    Call<FullData> loadData(@Header("Authorization") String bearerToken);
}
