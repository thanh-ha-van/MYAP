package com.example.havan.mytrafficmap.Network.Service;

import com.example.havan.mytrafficmap.Network.Object.Account;
import com.example.havan.mytrafficmap.Network.Object.FullData;
import com.google.gson.Gson;

import org.androidannotations.annotations.EBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

/**
 * Created by HaVan on 3/13/2017.
 */


@EBean(scope = Singleton)
public class DataService {


    private Retrofit retrofit;

    private RestService restService;

    private Account account = new Account();

    private FullData listData = new FullData();

    public DataService() {

        retrofit = new Retrofit.Builder().baseUrl("http://ibss.io:5599/")
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        restService = retrofit.create(RestService.class);
    }

    public Account getAccount() {
        return account;
    }

    public FullData getListData(){
        return listData;
    }

    public void login(final String grant_type, final String username, final String password,
                      final LoginSuccessListener loginSuccessListener){

        restService.login(grant_type, username, password).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                account = response.body();
                if (loginSuccessListener != null)
                    loginSuccessListener.onSuccess(account);
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });
    }

    public void loadData(final String header, final LoadSuccessListener loadDataSuccessListener) {
        restService.loadData("Bearer " + header).enqueue(new Callback<FullData>() {
            @Override
            public void onResponse(Call<FullData> call, Response<FullData> response) {
                listData = response.body();
                if (loadDataSuccessListener != null)
                    loadDataSuccessListener.onSuccess(listData);
            }

            @Override
            public void onFailure(Call<FullData> call, Throwable t) {

            }
        });
    }
}
