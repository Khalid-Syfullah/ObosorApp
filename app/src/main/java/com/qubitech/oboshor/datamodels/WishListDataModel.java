package com.qubitech.oboshor.datamodels;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wishlist")
public class WishListDataModel {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "bookId")
    private String bookId;
    @ColumnInfo(name = "author")
    private String author;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "category")
    private String category;
    @ColumnInfo(name = "price")
    private int price;
    @ColumnInfo(name = "imageUrl")
    private String imageUrl;

    public WishListDataModel(@NonNull String bookId, String title, String author, String category, int price, String imageUrl) {
        this.bookId = bookId;
        this.author = author;
        this.title = title;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    @NonNull
    public String getBookId() {
        return bookId;
    }

    public void setBookId(@NonNull String bookId) {
        this.bookId = bookId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
