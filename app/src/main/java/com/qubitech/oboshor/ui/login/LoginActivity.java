package com.qubitech.oboshor.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qubitech.oboshor.MainActivity;
import com.qubitech.oboshor.R;

import org.jetbrains.annotations.NotNull;

import static com.qubitech.oboshor.StaticData.LOGIN_SHARED_PREFS;
import static com.qubitech.oboshor.StaticData.LOGIN_USER_PASS;
import static com.qubitech.oboshor.StaticData.LOGIN_USER_PHONE;

public class LoginActivity extends AppCompatActivity {

    private ConstraintLayout loginHeader;
    private TextView loginTab,signupTab;
    private NavController navController;
    private ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String nav = getIntent().getStringExtra("nav");
        loginHeader = findViewById(R.id.login_header);
        navController= Navigation.findNavController(this,R.id.login_signup_nav_host_fragment);
        loginTab = findViewById(R.id.tab_login);
        signupTab=findViewById(R.id.tab_signUp);
        backBtn = findViewById(R.id.login_backBtn);

        if(nav != null){
            if(nav.equals("signup")){
                navController.navigate(R.id.action_navigation_login_to_navigation_sign_up);
            }
        }

        signupTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(navController.getCurrentDestination().getId()!=R.id.navigation_sign_up){
                    gotoSignup();
                }
            }
        });

        loginTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(navController.getCurrentDestination().getId()!=R.id.navigation_login){
                    gotoLogin();
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination, @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                if(destination.getId()==R.id.navigation_sign_up){
                    signupTab.setBackground(ContextCompat.getDrawable(LoginActivity.this,R.drawable.background_login_btn));
                    loginTab.setBackground(null);
                }
                else if(destination.getId()==R.id.navigation_login){
                    loginTab.setBackground(ContextCompat.getDrawable(LoginActivity.this,R.drawable.background_login_btn));
                    signupTab.setBackground(null);
                }
            }
        });

    }

    private void gotoLogin() {
        navController.navigate(R.id.action_navigation_sign_up_to_navigation_login);

    }

    private void gotoSignup() {
        navController.navigate(R.id.action_navigation_login_to_navigation_sign_up);

    }

    @Override
    public void onBackPressed() {


        if(navController.getCurrentDestination().getId()==R.id.navigation_sign_up){
            navController.navigate(R.id.action_navigation_sign_up_to_navigation_login);
        }
        else {
            super.onBackPressed();
        }

    }


}