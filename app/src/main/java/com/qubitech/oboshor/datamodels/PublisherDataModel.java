package com.qubitech.oboshor.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PublisherDataModel {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String publisherName;

    @SerializedName("image")
    @Expose
    private String publisherImg;

    @SerializedName("publisher")
    @Expose
    private String publisher;

    public PublisherDataModel(String publisher) {
        this.publisher = publisher;
    }

    public PublisherDataModel(String id, String publisherName, String publisherImg) {
        this.id = id;
        this.publisherName = publisherName;
        this.publisherImg = publisherImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPublisherImg() {
        return publisherImg;
    }

    public void setPublisherImg(String publisherImg) {
        this.publisherImg = publisherImg;
    }
}
