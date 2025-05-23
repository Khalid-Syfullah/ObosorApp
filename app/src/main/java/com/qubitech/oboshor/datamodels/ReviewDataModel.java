package com.qubitech.oboshor.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewDataModel {

    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("bookId")
    @Expose
    private String bookId;
    @SerializedName("reviewId")
    @Expose
    private String reviewId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("username")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("title")
    @Expose
    private String commentTitle;
    @SerializedName("description")
    @Expose
    private String commentBody;
    @SerializedName("time")
    @Expose
    private String commentTime;
    @SerializedName("like")
    @Expose
    private String[] likes;
    @SerializedName("dislike")
    @Expose
    private String[] dislikes;
    @SerializedName("like_1")
    @Expose
    private int like;
    @SerializedName("dislike_1")
    @Expose
    private int dislike;
    @SerializedName("star")
    @Expose
    private float rating;


    public ReviewDataModel(String reviewId, String userId) {
        this.reviewId = reviewId;
        this.userId = userId;
    }

    public ReviewDataModel(String bookId, String userId, String name, String commentTitle, String commentBody, float rating) {
        this.bookId = bookId;
        this.userId = userId;
        this.name = name;
        this.commentTitle = commentTitle;
        this.commentBody = commentBody;
        this.rating = rating;
    }

    public ReviewDataModel(String name, String image, String commentTitle, String commentBody, String commentTime, int like, int dislike, float rating) {
        this.name = name;
        this.image = image;
        this.commentTitle = commentTitle;
        this.commentBody = commentBody;
        this.commentTime = commentTime;
        this.like = like;
        this.dislike = dislike;
        this.rating = rating;
    }

    public ReviewDataModel(String _id, String name, String image, String commentTitle, String commentBody, String commentTime, int like, int dislike, float rating) {
        this._id = _id;
        this.name = name;
        this.image = image;
        this.commentTitle = commentTitle;
        this.commentBody = commentBody;
        this.commentTime = commentTime;
        this.like = like;
        this.dislike = dislike;
        this.rating = rating;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCommentTitle() {
        return commentTitle;
    }

    public void setCommentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public String[] getLikes() {
        return likes;
    }

    public void setLikes(String[] likes) {
        this.likes = likes;
    }

    public String[] getDislikes() {
        return dislikes;
    }

    public void setDislikes(String[] dislikes) {
        this.dislikes = dislikes;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

}
