package com.example.havan.mytrafficmap.Network.Service;

import com.example.havan.mytrafficmap.Network.Object.Account;
import com.example.havan.mytrafficmap.Network.Object.FullData;
import com.google.gson.Gson;

import org.androidannotations.annotations.EBean;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;
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

    final TaskCompletionSource<Account> taskLogin = new TaskCompletionSource<>();
    final TaskCompletionSource<FullData> taskLoadData = new TaskCompletionSource<>();

    public DataService() {

        retrofit = new Retrofit.Builder()
                .baseUrl("http://ibss.io:5599/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        restService = retrofit.create(RestService.class);
    }

    public Task< Account> login(final String grant_type, final String username, final String password) {

        restService.login(grant_type, username, password).enqueue(new Callback < Account>() {
            @Override
            public void onResponse(Call< Account> call, Response< Account> response) {
                taskLogin.setResult(response.body());
            }

            @Override
            public void onFailure(Call< Account> call, Throwable t) {

            }
        });
        return taskLogin.getTask();
    }

    public Task<FullData> loadData(final String grant_type, final String username, final String password) {
        login(grant_type, username, password).continueWithTask(new Continuation<Account, Task<Account>>() {
            @Override
            public Task<Account> then(Task<Account> task) throws Exception {
                restService.loadData("Bearer " + task.getResult().getAccessToken()).enqueue(new Callback<FullData>() {
                    @Override
                    public void onResponse(Call<FullData> call, Response<FullData> response) {
                        taskLoadData.setResult(response.body());

                    }

                    @Override
                    public void onFailure(Call<FullData> call, Throwable t) {

                    }
                });
                return null;
            }
        });
        return taskLoadData.getTask();
    }
}

