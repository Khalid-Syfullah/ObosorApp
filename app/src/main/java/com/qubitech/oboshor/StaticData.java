package com.qubitech.oboshor;

import androidx.lifecycle.MutableLiveData;

import com.qubitech.oboshor.datamodels.LoginDataModel;

public class StaticData {
    public static MutableLiveData<LoginDataModel> currentUserData=new MutableLiveData<>();
    public static final String LOGIN_SHARED_PREFS = "login_pref";
    public static final String LOGIN_USER_PHONE = "login_phone";
    public static final String LOGIN_USER_PASS = "login_pass";
    public static String CURRENT_BOOK_ID="";
    public static String CURRENT_FRAGMENT="";

}
