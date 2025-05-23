package com.qubitech.oboshor.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginDataModel {

    @SerializedName("currentUser")
    @Expose
    private UserDataModel currentUser;

    @SerializedName("token")
    @Expose
    private String token;

    public LoginDataModel() {
    }

    public LoginDataModel(UserDataModel currentUser, String token) {
        this.currentUser = currentUser;
        this.token = token;
    }

    public UserDataModel getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDataModel currentUser) {
        this.currentUser = currentUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
