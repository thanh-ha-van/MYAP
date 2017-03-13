package com.example.havan.mytrafficmap.Network.Object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by HaVan on 3/13/2017.
 */
public class Account implements Serializable {

    @SerializedName("access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
