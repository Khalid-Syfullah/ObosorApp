package com.qubitech.oboshor.ui.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qubitech.oboshor.MainActivity;
import com.qubitech.oboshor.R;
import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.LoginDataModel;
import com.qubitech.oboshor.datamodels.UserDataModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.qubitech.oboshor.StaticData.LOGIN_SHARED_PREFS;
import static com.qubitech.oboshor.StaticData.LOGIN_USER_PASS;
import static com.qubitech.oboshor.StaticData.LOGIN_USER_PHONE;
import static com.qubitech.oboshor.StaticData.currentUserData;

public class LoginFragment extends Fragment {

    private Button loginBtn;
    private TextView noAccountText, signUpText, forgotPassText;
    private EditText phoneText, passwordText, enterOtpText;
    private ImageView passwordVisibility;
    private ProgressBar progressBar;
    private View root;
    private String verificationId, phonenumber;
    private FirebaseAuth mAuth;
    private AlertDialog currentAlertDialog;
    private String otpPhone="";

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(root==null)
            root = inflater.inflate(R.layout.fragment_login, container, false);

        phoneText = root.findViewById(R.id.login_et_phone);
        passwordText = root.findViewById(R.id.login_et_password);
        passwordVisibility = root.findViewById(R.id.login_password_visibility);
        progressBar = root.findViewById(R.id.login_progress_bar);
        loginBtn = root.findViewById(R.id.login_button);
        noAccountText=root.findViewById(R.id.login_no_account);
        signUpText=root.findViewById(R.id.login_sign_up);
        forgotPassText=root.findViewById(R.id.login_forgot_password);

        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("bn");

        return root;

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        passwordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordText.getTransformationMethod() == null) {
                    passwordText.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    passwordText.setTransformationMethod(null);
                }
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyLogin();
            }
        });
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.login_signup_nav_host_fragment).navigate(R.id.action_navigation_login_to_navigation_sign_up);
            }
        });


        forgotPassText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordAlertDialog();
            }
        });
    }

    private void verifyLogin(){

        String phone="",password="";

        if(phoneText.getText().toString().isEmpty()){
            phoneText.setError("Phone Required");
            phoneText.requestFocus();
        }
        else if(phoneText.getText().length() != 11){
            phoneText.setError("Phone number needs to be 11 characters!");
            phoneText.requestFocus();
        }
        else{
            phone = phoneText.getText().toString();
        }

        if(passwordText.getText().toString().isEmpty()){
            passwordText.setError("Password Required");
            passwordText.requestFocus();
        }
        else if(passwordText.getText().length() < 6){
            passwordText.setError("Password should be at least 6 characters!");
            passwordText.requestFocus();
        }

        else{
            password = passwordText.getText().toString();

        }

        if(!phone.isEmpty() && !password.isEmpty()){

            progressBar.setVisibility(View.VISIBLE);

            UserDataModel userDataModel = new UserDataModel(phone, password);
            RestAPI restAPI = RetrofitClient.createRetrofitClient();
            Call<LoginDataModel> loginCall = restAPI.loginUser(userDataModel);
            loginCall.enqueue(new Callback<LoginDataModel>() {
                @Override
                public void onResponse(Call<LoginDataModel> call, Response<LoginDataModel> response) {

                    if(response.code() == 200){

                        currentUserData.setValue(response.body());

                        SharedPreferences loginSharedPrefs = getActivity().getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor loginPrefsEditor = loginSharedPrefs.edit();

                        loginPrefsEditor.putString(LOGIN_USER_PHONE, currentUserData.getValue().getCurrentUser().getPhone1());
                        loginPrefsEditor.putString(LOGIN_USER_PASS, passwordText.getText().toString());
                        loginPrefsEditor.apply();

//                        Toast.makeText(getActivity(), "Message: "+response.body().getToken(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(), currentUserData.getValue().getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();

                        Log.d("Login",response.code()+": "+response.message()+" - "+response.body().getToken());
                        Log.d("Login","Token: "+response.body().getToken());

                        Intent intent=new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finishAffinity();

                    }
                    else if(response.code() == 400){
                        Gson gson = new GsonBuilder().create();
                        UserDataModel userDataModel;
                        try {
                            userDataModel= gson.fromJson(response.errorBody().string(),UserDataModel.class);
                            Toast.makeText(getActivity(),response.code()+": "+response.message()+" - "+userDataModel.getMessage(),Toast.LENGTH_SHORT).show();
                            Log.d("Login",response.code()+": "+response.message()+" - "+userDataModel.getMessage());
                            progressBar.setVisibility(View.GONE);

                        } catch (IOException e) {

                        }

                    }
                    else{
                        progressBar.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<LoginDataModel> call, Throwable t) {
                    Toast.makeText(getActivity(),"Error: "+t.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.d("Login","Error: "+t.getMessage());
                    progressBar.setVisibility(View.GONE);

                }
            });
        }
    }

    private void forgotPasswordAlertDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);
        View dialog=LayoutInflater.from(getContext()).inflate(R.layout.alert_forgot_pass,null);
        builder.setView(dialog);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        EditText phoneText = dialog.findViewById(R.id.et_forgot_pass_alert_phone);
        Button submitBtn = dialog.findViewById(R.id.btn_forgot_pass_alert_submit);
        Button cancelBtn = dialog.findViewById(R.id.btn_forgot_pass_alert_cancel);

        currentAlertDialog = alertDialog;

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (phoneText.getText().toString().isEmpty()) {
                    phoneText.setError("Phone Required");
                    phoneText.requestFocus();
                } else if (phoneText.getText().toString().length() < 11) {
                    phoneText.setError("Phone No must be 11 digits");
                    phoneText.requestFocus();
                }
                else {
                    sendVerificationCode("+88"+phoneText.getText().toString());
                    alertDialog.dismiss();
                    otpPhone=phoneText.getText().toString();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    private void enterOtpAlertDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);
        View dialog=LayoutInflater.from(getContext()).inflate(R.layout.alert_enter_otp,null);
        builder.setView(dialog);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        currentAlertDialog = alertDialog;


        EditText otpText = dialog.findViewById(R.id.et_enter_otp_alert_phone);
        enterOtpText = dialog.findViewById(R.id.et_enter_otp_alert_phone);
        Button submitBtn = dialog.findViewById(R.id.btn_enter_otp_alert_submit);
        Button cancelBtn = dialog.findViewById(R.id.btn_enter_otp_alert_cancel);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(otpText.getText().toString().isEmpty()){
                    otpText.setError("OTP required");
                    otpText.requestFocus();
                }
                else if(otpText.getText().toString().length() < 6){
                    otpText.setError("OTP must be 6 digits");
                    otpText.requestFocus();
                }
                else {
                    verifyCodeAndSignIn(otpText.getText().toString());
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });



    }

    private void changePasswordAlertDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);
        View dialog=LayoutInflater.from(getContext()).inflate(R.layout.alert_forgot_pass_2,null);
        builder.setView(dialog);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        TextView labelText = dialog.findViewById(R.id.forgot_pass_2_alert_header);
        TextView newPassLabelText = dialog.findViewById(R.id.tv_forgot_pass_2_alert_label_new_pass);
        TextView retypePassLabelText = dialog.findViewById(R.id.tv_forgot_pass_2_alert_label_retype_pass);

        EditText newPassText = dialog.findViewById(R.id.et_forgot_pass_2_alert_new_pass);
        EditText retypePassText = dialog.findViewById(R.id.et_forgot_pass_2_alert_retype_pass);

        ImageView newPassVisibility = dialog.findViewById(R.id.forgot_pass_2_alert_visibility_new_pass);
        ImageView retypePassVisibility = dialog.findViewById(R.id.forgot_pass_2_alert_visibility_retype_pass);

        ImageView successImage = dialog.findViewById(R.id.forgot_pass_2_alert_image);
        Button submitBtn = dialog.findViewById(R.id.btn_forgot_pass_2_alert_submit);
        Button cancelBtn = dialog.findViewById(R.id.btn_forgot_pass_2_alert_cancel);
        Button successBtn = dialog.findViewById(R.id.forgot_pass_2_alert_submit_btn2);


        newPassVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newPassText.getTransformationMethod() == null) {
                    newPassText.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    newPassText.setTransformationMethod(null);
                }
            }
        });

        retypePassVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (retypePassText.getTransformationMethod() == null) {
                    retypePassText.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    retypePassText.setTransformationMethod(null);
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newPass="", retypePass="";


                if(newPassText.getText().toString().isEmpty()){

                    newPassText.setError("New Password Required");
                    newPassText.requestFocus();

                }
                else if(newPassText.getText().length() < 6){
                    newPassText.setError("Password should be at least 6 characters!");
                    newPassText.requestFocus();
                }
                else if(!newPassText.getText().toString().equals(retypePassText.getText().toString())){
                    newPassText.setError("Password does not match");
                    newPassText.requestFocus();
                }
                else{
                    newPass = newPassText.getText().toString();
                }

                if(retypePassText.getText().toString().isEmpty()){

                    retypePassText.setError("Retype New Password");
                    retypePassText.requestFocus();

                }
                else{
                    retypePass = retypePassText.getText().toString();
                }

                if(!newPass.isEmpty() && !retypePass.isEmpty()){

                    UserDataModel userDataModel = new UserDataModel(otpPhone,newPass);
                    otpPhone="";
                    RestAPI restAPI = RetrofitClient.createRetrofitClient();
                    Call<UserDataModel> updatePasswordCall = restAPI.forgotPassword(userDataModel);
                    updatePasswordCall.enqueue(new Callback<UserDataModel>() {
                        @Override
                        public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {

                            if(response.code() == 200){

                                labelText.setText("Password Changed");
                                successBtn.setText("Ok");
                                successImage.setImageResource(R.drawable.ic_check_circle);

                                newPassLabelText.setVisibility(View.GONE);
                                retypePassLabelText.setVisibility(View.GONE);
                                newPassText.setVisibility(View.GONE);
                                retypePassText.setVisibility(View.GONE);
                                newPassVisibility.setVisibility(View.GONE);
                                retypePassVisibility.setVisibility(View.GONE);
                                submitBtn.setVisibility(View.GONE);
                                cancelBtn.setVisibility(View.GONE);
                                successImage.setVisibility(View.VISIBLE);
                                successBtn.setVisibility(View.VISIBLE);
                            }

                            else if(response.code() == 400){

                                Toast.makeText(getActivity(),"Invalid Phone",Toast.LENGTH_LONG).show();
                            }
                            else{

                                labelText.setText("Failed");
                                successBtn.setText("Ok");
                                successImage.setImageResource(R.drawable.ic_close);

                                newPassLabelText.setVisibility(View.GONE);
                                retypePassLabelText.setVisibility(View.GONE);
                                newPassText.setVisibility(View.GONE);
                                retypePassText.setVisibility(View.GONE);
                                newPassVisibility.setVisibility(View.GONE);
                                retypePassVisibility.setVisibility(View.GONE);
                                submitBtn.setVisibility(View.GONE);
                                cancelBtn.setVisibility(View.GONE);
                                successImage.setVisibility(View.VISIBLE);
                                successBtn.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDataModel> call, Throwable t) {

                            labelText.setText("Failed");
                            successBtn.setText("Ok");
                            successImage.setImageResource(R.drawable.ic_close);

                            newPassLabelText.setVisibility(View.GONE);
                            retypePassLabelText.setVisibility(View.GONE);
                            newPassText.setVisibility(View.GONE);
                            retypePassText.setVisibility(View.GONE);
                            newPassVisibility.setVisibility(View.GONE);
                            retypePassVisibility.setVisibility(View.GONE);
                            submitBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                            successImage.setVisibility(View.VISIBLE);
                            successBtn.setVisibility(View.VISIBLE);
                        }
                    });



                }




            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });

        successBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();


            }
        });

    }


    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(getActivity())
                .setCallbacks(mCallBack)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            enterOtpAlertDialog();

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                enterOtpText.setText(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getActivity(), "Phone Verification Failed!",Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCodeAndSignIn(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getActivity(), "Phone Verification Successful!",Toast.LENGTH_SHORT).show();
                            currentAlertDialog.dismiss();
                            changePasswordAlertDialog();

                        } else {
                            Toast.makeText(getActivity(), "Phone Verification Failed!",Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }




}