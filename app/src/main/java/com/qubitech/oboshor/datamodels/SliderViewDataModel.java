package com.qubitech.oboshor.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderViewDataModel {

    @SerializedName("image")
    @Expose
    private String imgUrl;
    @SerializedName("link")
    @Expose
    private String link;

    public SliderViewDataModel() {

    }

    public SliderViewDataModel(String imgUrl, String link) {
        this.imgUrl = imgUrl;
        this.link = link;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


}