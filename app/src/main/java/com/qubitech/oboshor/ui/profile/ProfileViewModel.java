package com.qubitech.oboshor.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.UserDataModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {


    private MutableLiveData<UserDataModel> profileData = new MutableLiveData<>();

    public LiveData<UserDataModel> fetchProfileData(){

        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<ArrayList<UserDataModel>> getUserCall = restAPI.getUser(StaticData.currentUserData.getValue().getToken());
        getUserCall.enqueue(new Callback<ArrayList<UserDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserDataModel>> call, Response<ArrayList<UserDataModel>> response) {

                if(response.code() == 200){

                    UserDataModel userDataModel = response.body().get(0);
                    profileData.setValue(userDataModel);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserDataModel>> call, Throwable t) {

            }
        });

        return profileData;
    }

}
