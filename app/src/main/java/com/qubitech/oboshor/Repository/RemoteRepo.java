package com.qubitech.oboshor.Repository;

import static com.qubitech.oboshor.api.RetrofitClient.BASE_URL;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.BookDataModel;
import com.qubitech.oboshor.datamodels.BookQueryDataModel;
import com.qubitech.oboshor.datamodels.CategoryDataModel;
import com.qubitech.oboshor.datamodels.PublisherDataModel;
import com.qubitech.oboshor.datamodels.SliderViewDataModel;
import com.qubitech.oboshor.ui.dashboard.recyclerview.BookAdapter;
import com.qubitech.oboshor.ui.dashboard.recyclerview.PublishersAdapter;
import com.qubitech.oboshor.ui.dashboard.recyclerview.SliderAdapter;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteRepo {
    private final Application application;
    MutableLiveData<ArrayList<SliderViewDataModel>> sliderData = new MutableLiveData<>();
    MutableLiveData<ArrayList<CategoryDataModel>> categories = new MutableLiveData<>();
    MutableLiveData<ArrayList<PublisherDataModel>> publishers = new MutableLiveData<>();
    MutableLiveData<ArrayList<BookDataModel>> bestSellerBooks = new MutableLiveData<>();
    MutableLiveData<ArrayList<BookDataModel>> newArrivalBooks = new MutableLiveData<>();
    MutableLiveData<ArrayList<BookDataModel>> onSaleBooks = new MutableLiveData<>();
    MutableLiveData<ArrayList<BookDataModel>> preOrderBooks = new MutableLiveData<>();
    MutableLiveData<ArrayList<BookDataModel>> trendingBooks = new MutableLiveData<>();

    private RestAPI restAPI;

    public RemoteRepo(Application application) {
        this.application = application;
        restAPI = RetrofitClient.createRetrofitClient();
        fetchSliderData();
        fetchCategories();
        fetchPublishers();
        fetchBestSellerBooks();
        fetchNewArrivalBooks();
        fetchOnSaleBooks();
        fetchPreOrderBooks();
        fetchTrendingBooks();
    }

    public LiveData<ArrayList<BookDataModel>> getBestSellerBooks() {
        return bestSellerBooks;
    }

    public LiveData<ArrayList<BookDataModel>> getNewArrivalBooks() {
        return newArrivalBooks;
    }

    public LiveData<ArrayList<BookDataModel>> getOnSaleBooks() {
        return onSaleBooks;
    }

    public LiveData<ArrayList<BookDataModel>> getPreOrderBooks() {
        return preOrderBooks;
    }

    public MutableLiveData<ArrayList<BookDataModel>> getTrendingBooks() {
        return trendingBooks;
    }

    public LiveData<ArrayList<SliderViewDataModel>> fetchSliderData() {
        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<ArrayList<SliderViewDataModel>> sliderCall = restAPI.getSliderData();
        sliderCall.enqueue(new Callback<ArrayList<SliderViewDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SliderViewDataModel>> call, Response<ArrayList<SliderViewDataModel>> response) {
                if (response.code() == 200) {
                    ArrayList<SliderViewDataModel> sliderViewDataModels = response.body();
                    ArrayList<SliderViewDataModel> sliderViewDataModelsUpdated = new ArrayList<>();
                    for (int i = 0; i < sliderViewDataModels.size(); i++) {
                        SliderViewDataModel sliderViewDataModel = sliderViewDataModels.get(i);
                        String link = sliderViewDataModel.getLink();
                        String image = BASE_URL + "/" + sliderViewDataModel.getImgUrl();
                        Log.d("SliderData", "Link: " + link + " image: " + image);
                        sliderViewDataModelsUpdated.add(new SliderViewDataModel(image, link));
                    }
                    sliderData.setValue(sliderViewDataModelsUpdated);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SliderViewDataModel>> call, Throwable t) {
            }
        });
        return sliderData;
    }

    public LiveData<ArrayList<CategoryDataModel>> fetchCategories() {
        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<ArrayList<CategoryDataModel>> categoryCall = restAPI.getAllCategory(StaticData.currentUserData.getValue().getToken());
        categoryCall.enqueue(new Callback<ArrayList<CategoryDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryDataModel>> call, Response<ArrayList<CategoryDataModel>> response) {
                if (response.code() == 200) {
                    categories.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CategoryDataModel>> call, Throwable t) {
            }
        });
        return categories;
    }

    public LiveData<ArrayList<PublisherDataModel>> fetchPublishers() {
        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<ArrayList<PublisherDataModel>> publishersCall = restAPI.getAllPublishers(StaticData.currentUserData.getValue().getToken());
        publishersCall.enqueue(new Callback<ArrayList<PublisherDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PublisherDataModel>> call, Response<ArrayList<PublisherDataModel>> response) {
                if (response.code() == 200) {
                    publishers.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PublisherDataModel>> call, Throwable t) {
            }
        });
        return publishers;
    }

    private void fetchBestSellerBooks() {

        BookQueryDataModel bookQueryDataModel = new BookQueryDataModel("bestSeller",1,10,0);
        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<BookQueryDataModel> booksCall = restAPI.getDashboardViewAllBooks(bookQueryDataModel);

        booksCall.enqueue(new Callback<BookQueryDataModel>() {
            @Override
            public void onResponse(Call<BookQueryDataModel> call, Response<BookQueryDataModel> response) {

                if (response.code() == 200) {

                    if(response.body() != null) {

                        bestSellerBooks.setValue(response.body().getBooks());

                    }

                }
            }

            @Override
            public void onFailure(Call<BookQueryDataModel> call, Throwable t) {

            }
        });
    }

    private void fetchNewArrivalBooks() {
        BookQueryDataModel bookQueryDataModel = new BookQueryDataModel("newArrivals",1,10,0);
        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<BookQueryDataModel> booksCall = restAPI.getDashboardViewAllBooks(bookQueryDataModel);

        booksCall.enqueue(new Callback<BookQueryDataModel>() {
            @Override
            public void onResponse(Call<BookQueryDataModel> call, Response<BookQueryDataModel> response) {

                if (response.code() == 200) {

                    if(response.body() != null) {

                        newArrivalBooks.setValue(response.body().getBooks());

                    }

                }
            }

            @Override
            public void onFailure(Call<BookQueryDataModel> call, Throwable t) {

            }
        });
    }

    private void fetchOnSaleBooks() {
        BookQueryDataModel bookQueryDataModel = new BookQueryDataModel("onSell",1,10,0);
        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<BookQueryDataModel> booksCall = restAPI.getDashboardViewAllBooks(bookQueryDataModel);

        booksCall.enqueue(new Callback<BookQueryDataModel>() {
            @Override
            public void onResponse(Call<BookQueryDataModel> call, Response<BookQueryDataModel> response) {

                if (response.code() == 200) {

                    if(response.body() != null) {

                        onSaleBooks.setValue(response.body().getBooks());

                    }

                }
            }

            @Override
            public void onFailure(Call<BookQueryDataModel> call, Throwable t) {

            }
        });
    }

    private void fetchPreOrderBooks() {
        BookQueryDataModel bookQueryDataModel = new BookQueryDataModel("preOrderList",1,10,0);
        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<BookQueryDataModel> booksCall = restAPI.getDashboardViewAllBooks(bookQueryDataModel);

        booksCall.enqueue(new Callback<BookQueryDataModel>() {
            @Override
            public void onResponse(Call<BookQueryDataModel> call, Response<BookQueryDataModel> response) {

                if (response.code() == 200) {

                    if(response.body() != null) {

                        preOrderBooks.setValue(response.body().getBooks());

                    }

                }
            }

            @Override
            public void onFailure(Call<BookQueryDataModel> call, Throwable t) {

            }
        });
    }

    private void fetchTrendingBooks() {
        BookQueryDataModel bookQueryDataModel = new BookQueryDataModel("trending",1,10,0);
        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<BookQueryDataModel> booksCall = restAPI.getDashboardViewAllBooks(bookQueryDataModel);

        booksCall.enqueue(new Callback<BookQueryDataModel>() {
            @Override
            public void onResponse(Call<BookQueryDataModel> call, Response<BookQueryDataModel> response) {

                if (response.code() == 200) {

                    if(response.body() != null) {

                        trendingBooks.setValue(response.body().getBooks());

                    }

                }
            }

            @Override
            public void onFailure(Call<BookQueryDataModel> call, Throwable t) {

            }
        });
    }
}
