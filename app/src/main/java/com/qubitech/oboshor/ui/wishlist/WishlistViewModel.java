package com.qubitech.oboshor.ui.wishlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qubitech.oboshor.Repository.LocalRepo;
import com.qubitech.oboshor.Repository.RemoteRepo;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.BookDataModel;
import com.qubitech.oboshor.datamodels.WishListDataModel;
import com.qubitech.oboshor.datamodels.WishListDataModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishlistViewModel extends AndroidViewModel {
    private LiveData<List<WishListDataModel>> wishlistData;
    private LiveData<BookDataModel> singleBookDetail;
    private LocalRepo localRepo;
    private RestAPI restAPI;

    
    
    public WishlistViewModel(@NonNull @NotNull Application application) {
        super(application);

        localRepo= new LocalRepo(application);

        restAPI= RetrofitClient.createRetrofitClient();
        wishlistData=localRepo.getWishlistData();
    }

    public void insert(WishListDataModel wishListDataModel){
        localRepo.insertWishlistItem(wishListDataModel);
    }
    public LiveData<List<WishListDataModel>> getWishlistData(){
        return wishlistData;
    }

    public void deleteWishlistItem(String bookId){
        localRepo.deleteWishlistItem(bookId);
    }


    public void updateData() {
        for (int i = 0; i < wishlistData.getValue().size(); i++) {

            Call<BookDataModel> getBookDetails= restAPI.getBookDetails(wishlistData.getValue().get(i).getBookId());
            int finalI = i;
            getBookDetails.enqueue(new Callback<BookDataModel>() {

                @Override
                public void onResponse(Call<BookDataModel> call, Response<BookDataModel> response) {
                    if(response.isSuccessful()){
                        if(wishlistData.getValue().get(finalI).getPrice()!=response.body().getPrice()){
                            localRepo.updateCartPrice(wishlistData.getValue().get(finalI).getBookId(),response.body().getPrice());
                        }
                    }
                }

                @Override
                public void onFailure(Call<BookDataModel> call, Throwable t) {

                }
            });

        }

    }

//    public LiveData<BookDataModel> getBookData(String bookId) {
//
//        singleBookDetail = remoteRepo.getBookDetails(bookId);
//        return singleBookDetail;
//    }
}
