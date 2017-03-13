package com.example.havan.mytrafficmap.Network;

/**
 * Created by NTT on 3/10/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("grant_type")
    @Expose
    public String type;

    @SerializedName("username")
    @Expose
    public String name;

    @SerializedName("password")
    @Expose
    public String password;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
