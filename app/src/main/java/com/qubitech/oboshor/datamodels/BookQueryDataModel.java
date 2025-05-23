package com.qubitech.oboshor.datamodels;

import java.io.Serializable;
import java.util.ArrayList;

public class BookQueryDataModel implements Serializable {

    private String category, subcategory, publisher, basis;
    private int pageno, itemperpage, numOfPages;
    private ArrayList<BookDataModel> books;

    public BookQueryDataModel() {

    }

    public BookQueryDataModel(String basis, int pageno, int itemperpage, int numOfPages) {
        this.basis = basis;
        this.pageno = pageno;
        this.itemperpage = itemperpage;
        this.numOfPages = 0;
    }

    public BookQueryDataModel(int pageno, String publisher, int itemperpage) {

        this.pageno = pageno;
        this.publisher = publisher;
        this.itemperpage = itemperpage;
    }

    public BookQueryDataModel(String category, int pageno, int itemperpage) {
        this.category = category;
        this.pageno = pageno;
        this.itemperpage = itemperpage;
    }

    public BookQueryDataModel(int pageno, int itemperpage) {
        this.pageno = pageno;
        this.itemperpage = itemperpage;
    }

    public BookQueryDataModel(String category, String subcategory, int pageno, int itemperpage) {
        this.category = category;
        this.subcategory = subcategory;
        this.pageno = pageno;
        this.itemperpage = itemperpage;
    }

    public BookQueryDataModel(int numOfPages, ArrayList<BookDataModel> books) {
        this.numOfPages = numOfPages;
        this.books = books;
    }

    public BookQueryDataModel(String category, String subcategory) {
        this.category = category;
        this.subcategory = subcategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPageno() {
        return pageno;
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }

    public int getItemperpage() {
        return itemperpage;
    }

    public void setItemperpage(int itemperpage) {
        this.itemperpage = itemperpage;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(int numOfPages) {
        this.numOfPages = numOfPages;
    }

    public ArrayList<BookDataModel> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<BookDataModel> books) {
        this.books = books;
    }
}
