package com.qubitech.oboshor.datamodels;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "cart")
public class CartDataModel implements Serializable {



    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "bookId")
    private String bookId;
    @Ignore
    private String id;
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
    @ColumnInfo(name = "quantity")
    private int quantity;
    @Ignore
    private BookDataModel book;

    @Ignore
    public CartDataModel(@NonNull String bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public CartDataModel(int quantity, @NonNull String bookId, String title, String author, String category, int price, String imageUrl) {
        this.quantity = quantity;
        this.bookId = bookId;
        this.author = author;
        this.title = title;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public BookDataModel getBookDataModel() {
        return book;
    }

    public void setBookDataModel(BookDataModel book) {
        this.book = book;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
