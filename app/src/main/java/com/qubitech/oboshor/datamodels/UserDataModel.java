package com.qubitech.oboshor.datamodels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDataModel {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("phone1")
    @Expose
    private String phone1;
    @SerializedName("phone2")
    @Expose
    private String phone2;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("wishlist")
    @Expose
    private String[] wishlist;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("oldPass")
    @Expose
    private String oldPass;

    @SerializedName("newPass")
    @Expose
    private String newPass;

    public UserDataModel() {

    }

    public UserDataModel(String username, String email, String phone1, String phone2, String address, String password) {
        this.username = username;
        this.email = email;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.address = address;
        this.password = password;
    }

    public UserDataModel(String username, String email, String phone1, String phone2, String address) {
        this.username = username;
        this.email = email;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.address = address;
    }

    public UserDataModel(String userId, String oldPass, String newPass) {
        this.userId = userId;
        this.oldPass = oldPass;
        this.newPass = newPass;
    }

    public UserDataModel(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public UserDataModel(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getWishlist() {
        return wishlist;
    }

    public void setWishlist(String[] wishlist) {
        this.wishlist = wishlist;
    }
}
