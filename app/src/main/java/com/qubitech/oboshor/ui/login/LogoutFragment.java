package com.qubitech.oboshor.ui.login;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.qubitech.oboshor.ui.login.LoginActivity;

import static android.content.Context.MODE_PRIVATE;
import static com.qubitech.oboshor.StaticData.LOGIN_SHARED_PREFS;

public class LogoutFragment extends Fragment {
    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences loginSharedPrefs = getActivity().getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
        loginSharedPrefs.edit().clear().apply();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();

    }
}
