package com.example.havan.mytrafficmap.Network.Object;

import com.google.api.client.util.DateTime;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by HaVan on 3/13/2017.
 */
public class Data {

    @SerializedName("id")
    private String id;

    @SerializedName("creatorId")
    private String creatorId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}
