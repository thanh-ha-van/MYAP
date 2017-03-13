package com.example.havan.mytrafficmap.Network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by NTT on 3/10/2017.
 */


public interface RestServiceInterface {

    @POST("api/token")
    Call<List<UserModel>> getApi();

}
