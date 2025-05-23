package com.qubitech.oboshor.ui.cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.qubitech.oboshor.Repository.LocalRepo;
import com.qubitech.oboshor.Repository.RemoteRepo;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.BookDataModel;
import com.qubitech.oboshor.datamodels.CartDataModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartViewModel extends AndroidViewModel {
    private LiveData<List<CartDataModel>> cartData;
    private RestAPI restAPI;

    private LocalRepo localRepo;
    private RemoteRepo remoteRepo;

    public CartViewModel(@NonNull @NotNull Application application) {
        super(application);
        restAPI= RetrofitClient.createRetrofitClient();
        cartData=new MediatorLiveData<>();
        localRepo = new LocalRepo(application);
        cartData= localRepo.getAllCartData();

    }
    public void insert(CartDataModel cartDataModel){
        localRepo.insertCartItem(cartDataModel);
    }
//    public void deleteCart(){
//        localRepo.delete();
//    }
    public LiveData<List<CartDataModel>> getCartData(){
        return cartData;
    }

    public void updateQuantity(CartDataModel cartDataModel) {
        if(cartDataModel.getQuantity()==0){
            localRepo.deleteCartItem(cartDataModel.getBookId());
        }
        else {
            localRepo.updateCartQuantity(cartDataModel);
        }
    }

    public void updateData() {
        for (int i = 0; i < cartData.getValue().size(); i++) {

            Call<BookDataModel> getBookDetails= restAPI.getBookDetails(cartData.getValue().get(i).getBookId());
            int finalI = i;
            getBookDetails.enqueue(new Callback<BookDataModel>() {

                    @Override
                    public void onResponse(Call<BookDataModel> call, Response<BookDataModel> response) {
                        if(response.isSuccessful()){
                            if(cartData.getValue().get(finalI).getPrice()!=response.body().getPrice()){
                                localRepo.updateCartPrice(cartData.getValue().get(finalI).getBookId(),response.body().getPrice());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BookDataModel> call, Throwable t) {

                    }
                });

        }

    }

//    public LiveData<List<CartDataModel>> getCartWithBookData(List<CartDataModel> cartDataList){
//        cartDataWithBookDetails =  remoteRepo.getCartData(cartDataList);
//        Log.i("cartbookData", "getCartWithBookData: "+cartDataWithBookDetails.getValue().get(0).getBookId());
//        return cartDataWithBookDetails;
//    }
//    public BookDataModel getBookData(String bookId){
//        return remoteRepo.getBookDetails(bookId);
//    }


}
