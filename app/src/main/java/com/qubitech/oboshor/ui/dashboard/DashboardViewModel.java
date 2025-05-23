package com.qubitech.oboshor.ui.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qubitech.oboshor.Repository.RemoteRepo;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.BookDataModel;
import com.qubitech.oboshor.datamodels.CategoryDataModel;
import com.qubitech.oboshor.datamodels.PublisherDataModel;
import com.qubitech.oboshor.datamodels.SliderViewDataModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DashboardViewModel extends AndroidViewModel {
    RemoteRepo remoteRepo;


    public DashboardViewModel(@NonNull @NotNull Application application) {
        super(application);
        remoteRepo = new RemoteRepo(application);
    }

    public LiveData<ArrayList<SliderViewDataModel>> getSliderData(){
        return remoteRepo.fetchSliderData();
    }

    public LiveData<ArrayList<CategoryDataModel>> getCategories() {
        return remoteRepo.fetchCategories();
    }

    public LiveData<ArrayList<PublisherDataModel>> getPublishers(){
        return remoteRepo.fetchPublishers();
    }

    public LiveData<ArrayList<BookDataModel>> getBestSellerBooks() {
        return remoteRepo.getBestSellerBooks();
    }

    public LiveData<ArrayList<BookDataModel>> getNewArrivalBooks() {
        return remoteRepo.getNewArrivalBooks();
    }

    public LiveData<ArrayList<BookDataModel>> getOnSaleBooks() {
        return remoteRepo.getOnSaleBooks();
    }

    public LiveData<ArrayList<BookDataModel>> getPreOrderBooks() {
        return remoteRepo.getPreOrderBooks();
    }

    public LiveData<ArrayList<BookDataModel>> getTrendingBooks() {
        return remoteRepo.getTrendingBooks();
    }




}
