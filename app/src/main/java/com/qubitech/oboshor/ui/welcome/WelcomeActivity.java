package com.qubitech.oboshor.ui.welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qubitech.oboshor.MainActivity;
import com.qubitech.oboshor.R;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.CartDataModel;
import com.qubitech.oboshor.datamodels.LoginDataModel;
import com.qubitech.oboshor.datamodels.UserDataModel;
import com.qubitech.oboshor.datamodels.WishListDataModel;
import com.qubitech.oboshor.ui.cart.CartViewModel;
import com.qubitech.oboshor.ui.login.LoginActivity;
import com.qubitech.oboshor.ui.wishlist.WishlistViewModel;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.qubitech.oboshor.StaticData.LOGIN_SHARED_PREFS;
import static com.qubitech.oboshor.StaticData.LOGIN_USER_PASS;
import static com.qubitech.oboshor.StaticData.LOGIN_USER_PHONE;
import static com.qubitech.oboshor.StaticData.currentUserData;

public class WelcomeActivity extends AppCompatActivity {


    private Button loginBtn, signupBtn;
    private ProgressBar loginProgress;
    private CartViewModel cartViewModel;
    private WishlistViewModel wishlistViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        loginBtn = findViewById(R.id.welcome_login_button);
        signupBtn = findViewById(R.id.welcome_signup_button);
        loginProgress = findViewById(R.id.welcome_progressBar);
        cartViewModel=new ViewModelProvider(this).get(CartViewModel.class);
        wishlistViewModel=new ViewModelProvider(this).get(WishlistViewModel.class);

        cartViewModel.getCartData().observe(this, new Observer<List<CartDataModel>>() {
            @Override
            public void onChanged(List<CartDataModel> cartDataModelList) {
                cartViewModel.updateData();
            }
        });
        wishlistViewModel.getWishlistData().observe(this, new Observer<List<WishListDataModel>>() {
            @Override
            public void onChanged(List<WishListDataModel> wishListDataModels) {
                wishlistViewModel.updateData();
            }
        });

        checkSharedPrefs();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                intent.putExtra("nav","sign in");
                startActivity(intent);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                intent.putExtra("nav","signup");
                startActivity(intent);
            }
        });

    }

    private void checkSharedPrefs() {

        loginProgress.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
        String phone, password;
        if (sharedPreferences.contains(LOGIN_USER_PHONE) && sharedPreferences.contains(LOGIN_USER_PASS)) {

            phone = sharedPreferences.getString(LOGIN_USER_PHONE, "");
            password = sharedPreferences.getString(LOGIN_USER_PASS, "");

            UserDataModel userDataModel = new UserDataModel(phone, password);
            RestAPI restAPI = RetrofitClient.createRetrofitClient();
            Call<LoginDataModel> loginCall = restAPI.loginUser(userDataModel);

            loginCall.enqueue(new Callback<LoginDataModel>() {
                @Override
                public void onResponse(Call<LoginDataModel> call, Response<LoginDataModel> response) {
                    if(response.code() == 200){

                        currentUserData.setValue(response.body());


//                        Toast.makeText(WelcomeActivity.this, "Message: "+response.body().getToken(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(WelcomeActivity.this, currentUserData.getValue().getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();

                        Log.d("Welcome",response.code()+": "+response.message()+" - "+response.body().getToken());
                        Log.d("Welcome","UserID: "+ currentUserData.getValue().getCurrentUser().getId());

                        Log.d("Welcome","Token: "+response.body().getToken());


                        Intent intent=new Intent(WelcomeActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        finishAffinity();

                    }
                    else if(response.code() == 400){
                        loginProgress.setVisibility(View.GONE);
                        loginBtn.setVisibility(View.VISIBLE);
                        signupBtn.setVisibility(View.VISIBLE);

                        Gson gson = new GsonBuilder().create();
                        UserDataModel userDataModel;
                        try {
                            userDataModel= gson.fromJson(response.errorBody().string(),UserDataModel.class);
                            Toast.makeText(WelcomeActivity.this,response.code()+": "+response.message()+" - "+userDataModel.getMessage(),Toast.LENGTH_SHORT).show();
                            Log.d("Welcome",response.code()+": "+response.message()+" - "+userDataModel.getMessage());
                        } catch (IOException e) {

                        }

                    }

                    else{
                        loginProgress.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<LoginDataModel> call, Throwable t) {

                    loginProgress.setVisibility(View.GONE);
                    loginBtn.setVisibility(View.VISIBLE);
                    signupBtn.setVisibility(View.VISIBLE);
                }
            });


        }

        else{

            loginProgress.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
            signupBtn.setVisibility(View.VISIBLE);
        }


    }

}