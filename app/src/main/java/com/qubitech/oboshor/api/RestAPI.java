package com.qubitech.oboshor.api;

import com.qubitech.oboshor.datamodels.BookDataModel;
import com.qubitech.oboshor.datamodels.BookQueryDataModel;
import com.qubitech.oboshor.datamodels.CategoryDataModel;
import com.qubitech.oboshor.datamodels.CouponDataModel;
import com.qubitech.oboshor.datamodels.LoginDataModel;
import com.qubitech.oboshor.datamodels.NotificationDataModel;
import com.qubitech.oboshor.datamodels.OrderDataModel;
import com.qubitech.oboshor.datamodels.PublisherDataModel;
import com.qubitech.oboshor.datamodels.ReviewDataModel;
import com.qubitech.oboshor.datamodels.UserDataModel;
import com.qubitech.oboshor.datamodels.SliderViewDataModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestAPI {

    @POST("/api/login")
    Call<LoginDataModel> loginUser(@Body UserDataModel userDataModel);

    @POST("/api/signup")
    Call<UserDataModel> signupUser(@Body UserDataModel userDataModel);

    @PATCH("/api/forgot-password")
    Call<UserDataModel> forgotPassword(@Body UserDataModel userDataModel);

    @GET("/api/user")
    Call<ArrayList<UserDataModel>> getUser(@Header("auth-token") String authToken);

    @PATCH("/api/user/update/{user_id}")
    Call<UserDataModel> updateUser(@Header("auth-token") String authToken, @Path("user_id") String user_id, @Body UserDataModel userDataModel);

    @PATCH("/api/user/password/update")
    Call<UserDataModel> updatePassword(@Header("auth-token") String authToken, @Body UserDataModel userDataModel);

    @GET("/api/admin/hero")
    Call<ArrayList<SliderViewDataModel>> getSliderData();

    @GET("/api/books")
    Call<BookQueryDataModel> searchBooks (@Header("auth-token") String authToken, @Query("search") String search, @Query("pageno") int pageNo, @Query("itemperpage") int item);

    @GET("/api/admin/bookbyauthor/{author_id}")
    Call<ArrayList<BookDataModel>> searchBooksByAuthor (@Header("auth-token") String authToken, @Path("author_id") String author);

    @GET("/api/admin/bookbypublisher/{publisher_id}")
    Call<ArrayList<BookDataModel>> searchBooksByPublisher (@Header("auth-token") String authToken, @Path("publisher_id") String author);

    @GET("/api/admin/bookbygenre/{genre_id}")
    Call<ArrayList<BookDataModel>> searchBooksByGenre (@Header("auth-token") String authToken, @Path("genre_id") String author);

    @GET("/api/admin/allcategory")
    Call<ArrayList<CategoryDataModel>> getAllCategory (@Header("auth-token") String authToken);

    @GET("/api/admin/allpublishers")
    Call<ArrayList<PublisherDataModel>> getAllPublishers (@Header("auth-token") String authToken);

    @GET("/api/admin/allbooksarray")
    Call<ArrayList<BookDataModel>> getAllBooks(@Header("auth-token") String authToken);

    @POST("/api/allBooks/onlyCategoryWise")
    Call<BookQueryDataModel> getAllBooksByCategory(@Body BookQueryDataModel bookQueryDataModel);

    @POST("/api/allBooks/categoryWise")
    Call<BookQueryDataModel> getAllBooksBySubcategory(@Body BookQueryDataModel bookQueryDataModel);

    @POST("/api/allBooks/publisherWise")
    Call<BookQueryDataModel> getAllBooksByPublisher(@Body BookQueryDataModel bookQueryDataModel);

    @GET("/api/allBooks/best-seller")
    Call<ArrayList<BookDataModel>> getBestSellerBooks(@Header("auth-token") String authToken);

    @GET("/api/admin/new-arrival")
    Call<ArrayList<BookDataModel>> getNewArrivalBooks(@Header("auth-token") String authToken);

    @GET("/api/admin/on-sell")
    Call<ArrayList<BookDataModel>> getOnSaleBooks(@Header("auth-token") String authToken);

    @GET("/api/admin/pre-order")
    Call<ArrayList<BookDataModel>> getPreOrderBooks(@Header("auth-token") String authToken);

    @POST("/api/allBooks/frugmented")
    Call<BookQueryDataModel> getDashboardViewAllBooks(@Body BookQueryDataModel bookQueryDataModel);

    @GET("/api/admin/allbookid")
    Call<ArrayList<BookDataModel>> getAllBookIds(@Header("auth-token") String authToken);

    @GET("/api/admin/book/{book_id}")
    Call<BookDataModel> getBookDetails(@Path("book_id") String book_id);

    @GET("/api/user/review")
    Call<ArrayList<ReviewDataModel>> getReviews(@Header("auth-token") String authToken);

    @POST("/api/user/review")
    Call<ReviewDataModel> uploadNewReview(@Header("auth-token") String authToken,
                                          @Body ReviewDataModel reviewDataModel);

    @PATCH("/api/user/review/like")
    Call<ArrayList<ReviewDataModel>> updateLike(@Header("auth-token") String authToken,
                                                @Body ReviewDataModel reviewDataModel);

    @PATCH("/api/user/review/dislike")
    Call<ArrayList<ReviewDataModel>> updateDislike(@Header("auth-token") String authToken,
                                                   @Body ReviewDataModel reviewDataModel);

    @GET("/api/orders/for-particular/{user_id}")
    Call<ArrayList<OrderDataModel>> getOrdersForUser(@Header("auth-token") String authToken, @Path("user_id") String userId);

    @POST("/api/order")
    Call<OrderDataModel> uploadNewOrder(@Header("auth-token") String authToken, @Body OrderDataModel orderDataModel);

    @GET("/api/admin/checkcuponcode/{coupon_id}")
    Call<CouponDataModel> checkCoupon(@Header("auth-token") String authToken, @Path("coupon_id") String couponId);

    @POST("/api/request-book")
    Call<BookDataModel> requestBook(@Header("auth-token") String authToken, @Body BookDataModel bookDataModel);

    @GET("/api/admin/allnotifications")
    Call<ArrayList<NotificationDataModel>> getNotifications(@Header("auth-token") String authToken);

}
