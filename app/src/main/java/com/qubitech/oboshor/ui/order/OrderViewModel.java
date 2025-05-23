package com.qubitech.oboshor.ui.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.qubitech.oboshor.Repository.RemoteRepo;
import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.BookDataModel;
import com.qubitech.oboshor.datamodels.OrderDataModel;
import com.qubitech.oboshor.ui.order.recyclerview.OrderAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewModel extends AndroidViewModel {

    MutableLiveData<ArrayList<OrderDataModel>> orderViewModel = new MutableLiveData<>();




    public OrderViewModel(@NonNull @NotNull Application application) {
        super(application);


    }


    public LiveData<ArrayList<OrderDataModel>> getOrdersForUser() {

        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call getOrdersCall = restAPI.getOrdersForUser(StaticData.currentUserData.getValue().getToken(),StaticData.currentUserData.getValue().getCurrentUser().getId());

        getOrdersCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if(response.code() == 200){

                    orderViewModel.setValue((ArrayList<OrderDataModel>) response.body());

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
        return orderViewModel;
    }






}
