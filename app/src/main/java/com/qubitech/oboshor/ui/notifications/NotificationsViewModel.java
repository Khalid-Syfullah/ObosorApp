package com.qubitech.oboshor.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.NotificationDataModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<NotificationDataModel>> notifications = new MutableLiveData<>();

    public NotificationsViewModel() {

    }

    public LiveData<ArrayList<NotificationDataModel>> getNotifications(){

        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<ArrayList<NotificationDataModel>> getNotificationsCall = restAPI.getNotifications(StaticData.currentUserData.getValue().getToken());
        getNotificationsCall.enqueue(new Callback<ArrayList<NotificationDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<NotificationDataModel>> call, Response<ArrayList<NotificationDataModel>> response) {

                if(response.code() == 200){

                    notifications.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationDataModel>> call, Throwable t) {

            }
        });

        return notifications;
    }

}