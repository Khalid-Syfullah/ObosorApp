package com.qubitech.oboshor.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CouponDataModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cuponcode")
    @Expose
    private String couponCode;
    @SerializedName("cupontype")
    @Expose
    private String couponType;
    @SerializedName("amount")
    @Expose
    private String amount;


    public CouponDataModel(String couponCode, String couponType, String amount) {
        this.couponCode = couponCode;
        this.couponType = couponType;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
