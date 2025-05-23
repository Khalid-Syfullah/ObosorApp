package com.qubitech.oboshor.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderArrayDataModel implements Serializable {

    @SerializedName("bookId")
    @Expose
    private String bookId;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("book")
    @Expose
    private BookDataModel book;



    public OrderArrayDataModel(String bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BookDataModel getBook() {
        return book;
    }

    public void setBook(BookDataModel book) {
        this.book = book;
    }
}
