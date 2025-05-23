package com.qubitech.oboshor.datamodels;

import java.io.Serializable;
import java.util.ArrayList;

public class BookDataModel implements Serializable {

    private boolean bestSeller, newArrivals, preOrderList, trending;
    private int amount, discount, quantity, available, price, pages, year;
    private String _id, isBn, image, title, author, category, subcategory, genre, publisher, edition, description, quality, name, phone, search, id;
    private ArrayList<ReviewDataModel> reviews;

    public BookDataModel(String image, String title, String category, String author, int amount) {
        this.image = image;
        this.title = title;
        this.category = category;
        this.author = author;
        this.amount = amount;
    }

    public BookDataModel(String name, String author, String phone) {
        this.name = name;
        this.author = author;
        this.phone = phone;
    }

    public BookDataModel(String category, String subcategory) {
        this.category = category;
        this.subcategory = subcategory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BookDataModel(String search) {
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isBestSeller() {
        return bestSeller;
    }

    public void setBestSeller(boolean bestSeller) {
        this.bestSeller = bestSeller;
    }

    public boolean isNewArrivals() {
        return newArrivals;
    }

    public void setNewArrivals(boolean newArrivals) {
        this.newArrivals = newArrivals;
    }

    public boolean isPreOrderList() {
        return preOrderList;
    }

    public void setPreOrderList(boolean preOrderList) {
        this.preOrderList = preOrderList;
    }

    public boolean isTrending() {
        return trending;
    }

    public void setTrending(boolean trending) {
        this.trending = trending;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIsBn() {
        return isBn;
    }

    public void setIsBn(String isBn) {
        this.isBn = isBn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<ReviewDataModel> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<ReviewDataModel> reviews) {
        this.reviews = reviews;
    }
}
