package com.qubitech.oboshor.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderDataModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("phone2")
    @Expose
    private String phone2;
    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("bookprice")
    @Expose
    private String bookprice;
    @SerializedName("totalprice")
    @Expose
    private String totalprice;
    @SerializedName("paymentmethod")
    @Expose
    private String paymentmethod;
    @SerializedName("appliedcupon")
    @Expose
    private CouponDataModel appliedCoupon;
    @SerializedName("zila")
    @Expose
    private String zilla;
    @SerializedName("order")
    @Expose
    private ArrayList<OrderArrayDataModel> order;



    public OrderDataModel() {

    }

    public OrderDataModel(ArrayList<OrderArrayDataModel> order) {
        this.order = order;
    }

    public OrderDataModel(String id, String date, String status) {
        this.id = id;
        this.date = date;
        this.status = status;
    }

    public OrderDataModel(String status, String phone, String phone2, String address, String bookprice, String totalprice, String paymentmethod, String zilla, ArrayList<OrderArrayDataModel> order, CouponDataModel appliedCoupon) {

        this.status = status;
        this.phone = phone;
        this.phone2 = phone2;
        this.address = address;
        this.bookprice = bookprice;
        this.totalprice = totalprice;
        this.paymentmethod = paymentmethod;
        this.zilla = zilla;
        this.order = order;
        this.appliedCoupon = appliedCoupon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getBookprice() {
        return bookprice;
    }

    public void setBookprice(String bookprice) {
        this.bookprice = bookprice;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public ArrayList<OrderArrayDataModel> getOrder() {
        return order;
    }

    public void setOrder(ArrayList<OrderArrayDataModel> order) {
        this.order = order;
    }

    public CouponDataModel getAppliedCoupon() {
        return appliedCoupon;
    }

    public void setAppliedCoupon(CouponDataModel appliedCoupon) {
        this.appliedCoupon = appliedCoupon;
    }

    public String getZilla() {
        return zilla;
    }

    public void setZilla(String zilla) {
        this.zilla = zilla;
    }
}
