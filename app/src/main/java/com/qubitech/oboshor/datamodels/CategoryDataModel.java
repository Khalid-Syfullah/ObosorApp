package com.qubitech.oboshor.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryDataModel {


    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("category")
    @Expose
    private String categoryName;

    @SerializedName("subcategory")
    @Expose
    private String[] subcategoryName;

    @SerializedName("currentCategory")
    @Expose
    private String currentCategoryName;

    @SerializedName("image")
    @Expose
    private String categoryImg;


    public CategoryDataModel(String categoryName) {
        this.categoryName = categoryName;
    }

    public CategoryDataModel(String categoryName, String currentCategoryName) {
        this.categoryName = categoryName;
        this.currentCategoryName = currentCategoryName;
    }

    public CategoryDataModel(String categoryName, String[] subcategoryName, String currentCategoryName) {
        this.categoryName = categoryName;
        this.subcategoryName = subcategoryName;
        this.currentCategoryName = currentCategoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImg() {
        return categoryImg;
    }

    public void setCategoryImg(String categoryImg) {
        this.categoryImg = categoryImg;
    }

    public String[] getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String[] subcategoryName) {
        this.subcategoryName = subcategoryName;
    }


    public String getCurrentCategoryName() {
        return currentCategoryName;
    }

    public void setCurrentCategoryName(String currentCategoryName) {
        this.currentCategoryName = currentCategoryName;
    }
}
