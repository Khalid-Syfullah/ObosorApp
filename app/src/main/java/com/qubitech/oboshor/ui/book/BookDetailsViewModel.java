package com.qubitech.oboshor.ui.book;

import static com.qubitech.oboshor.StaticData.CURRENT_BOOK_ID;
import static com.qubitech.oboshor.StaticData.currentUserData;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.amulyakhare.textdrawable.TextDrawable;
import com.qubitech.oboshor.R;
import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.BookDataModel;
import com.qubitech.oboshor.datamodels.ReviewDataModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<ReviewDataModel>> reviews = new MutableLiveData<>();

    public BookDetailsViewModel() {

    }

    public LiveData<ArrayList<ReviewDataModel>> fetchBookReviews(){

        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call getReviewCall = restAPI.getBookDetails(CURRENT_BOOK_ID);
        getReviewCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if(response.code() == 200){


                    BookDataModel bookDataModel = (BookDataModel) response.body();
                    ArrayList<ReviewDataModel> reviewDataModels = new ArrayList<>();

                    if(bookDataModel != null) {
                        if(bookDataModel.getReviews() != null) {

                            reviewDataModels = bookDataModel.getReviews();

                        }
                    }

                    reviews.setValue(reviewDataModels);

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        return reviews;
    }

    public LiveData<ArrayList<ReviewDataModel>> fetchReviews(){

        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call getReviewCall = restAPI.getReviews(currentUserData.getValue().getToken());
        getReviewCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if(response.code() == 200){


                    ArrayList<ReviewDataModel> reviewDataModelsFetch = (ArrayList<ReviewDataModel>) response.body();
                    ArrayList<ReviewDataModel> reviewDataModels = new ArrayList<>();

                    if(reviewDataModelsFetch != null) {
                        for (int i = 0; i < reviewDataModelsFetch.size(); i++) {

                            ReviewDataModel reviewDataModel = reviewDataModelsFetch.get(i);

                            if (reviewDataModel.getBookId().equals(CURRENT_BOOK_ID)) {

                                reviewDataModel.setLike(reviewDataModel.getLikes().length);
                                reviewDataModel.setDislike(reviewDataModel.getDislikes().length);

                                reviewDataModels.add(reviewDataModel);
                            }

                        }
                    }

                    reviews.setValue(reviewDataModels);

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        return reviews;
    }

    public void setReviews(ArrayList<ReviewDataModel> reviews) {
        this.reviews.setValue(reviews);
    }
}