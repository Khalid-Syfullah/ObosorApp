package com.qubitech.oboshor.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
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
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.UserDataModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupFragment extends Fragment {
    private TextView alreadyHaveAccount, enterOtpLabel, otpTimer;
    private EditText nameText, phoneText, altPhoneText, emailText, passwordText, confirmPasswordText, addressText, enterOtpText;
    private Button otpBtn, verifyBtn, signupBtn;
    private ImageView icon, passwordVisibility, confirmPasswordVisibility;
    private ProgressBar progressBar;
    private View root;
    private String verificationId, phonenumber="";
    private FirebaseAuth mAuth;
    private boolean isVerificationCompleted = false,isTimerOn=false;

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(root==null)
            root = inflater.inflate(R.layout.fragment_signup, container, false);

        nameText = root.findViewById(R.id.signup_name);
        phoneText = root.findViewById(R.id.signup_phone);
        altPhoneText = root.findViewById(R.id.signup_alt_phone);
        emailText = root.findViewById(R.id.signup_email);
        addressText = root.findViewById(R.id.signup_address);
        passwordText = root.findViewById(R.id.signup_password);
        confirmPasswordText = root.findViewById(R.id.signup_retype_password);
        enterOtpLabel = root.findViewById(R.id.signup_enter_otp_label);
        otpTimer = root.findViewById(R.id.signup_otp_timer);
        enterOtpText = root.findViewById(R.id.signup_enter_otp);
        otpBtn = root.findViewById(R.id.signup_send_otp_button);
        verifyBtn = root.findViewById(R.id.signup_confirm_otp_button);
        signupBtn = root.findViewById(R.id.signup_button);
        icon = root.findViewById(R.id.signup_button_icon);
        passwordVisibility = root.findViewById(R.id.signup_password_visibility);
        confirmPasswordVisibility = root.findViewById(R.id.signup_retype_password_visibility);
        alreadyHaveAccount =root.findViewById(R.id.signup_already_have_account);
        progressBar = root.findViewById(R.id.signup_progress_bar);

        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("bn");

        otpTimer.setVisibility(View.GONE);
        enterOtpLabel.setVisibility(View.GONE);
        enterOtpText.setVisibility(View.GONE);
        verifyBtn.setVisibility(View.GONE);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        phoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!isTimerOn)
                    otpBtn.setEnabled(!phoneText.getText().toString().isEmpty() && phoneText.getText().toString().length()==11);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        enterOtpText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                verifyBtn.setEnabled(enterOtpText.getText().toString().length()>=6);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (otpBtn.getText().toString().toLowerCase().equals("get otp")) {

                    if (phoneText.getText().toString().isEmpty()) {
                        phoneText.setError("Phone Required");
                        phoneText.requestFocus();
                    } else if (phoneText.getText().toString().length() < 11) {
                        phoneText.setError("Phone No must be 11 digits");
                        phoneText.requestFocus();
                    } else {
                        phonenumber = phoneText.getText().toString();

                        sendVerificationCode("+88" + phonenumber);

                    }

                }
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!phonenumber.equals(phoneText.getText().toString())) {
                    phoneText.setError("The phone number was changed");
                    phoneText.requestFocus();

                }
                else if(enterOtpText.getText().toString().isEmpty()){
                    enterOtpText.setError("OTP required");
                    enterOtpText.requestFocus();
                }
                else if(enterOtpText.getText().toString().length() < 6){
                    enterOtpText.setError("OTP must be 6 digits");
                    enterOtpText.requestFocus();
                }
                else {

                    verifyCodeAndSignIn(enterOtpText.getText().toString());
                }
            }
        });

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

        confirmPasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmPasswordText.getTransformationMethod() == null) {
                    confirmPasswordText.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    confirmPasswordText.setTransformationMethod(null);
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifySignUp();

            }
        });

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.login_signup_nav_host_fragment).navigate(R.id.action_navigation_sign_up_to_navigation_login);
            }
        });
    }

    private void verifySignUp(){
        String name="",phone="",altPhone="",email="",address="",password="";

        if(nameText.getText().toString().isEmpty()){
            nameText.setError("Name Required");
            nameText.requestFocus();
        }
        else{
            name = nameText.getText().toString();
        }


        if(phoneText.getText().toString().isEmpty()){
            phoneText.setError("Phone Required");
            phoneText.requestFocus();
        }
        else if(phoneText.getText().toString().length() < 11){
            phoneText.setError("Phone No must be 11 digits");
            phoneText.requestFocus();
        }
        else{
            phone = phoneText.getText().toString();
        }

        if(altPhoneText.getText().toString().isEmpty()){
            altPhoneText.setError("Alt Phone Required");
            altPhoneText.requestFocus();
        }

        else if(altPhoneText.getText().toString().length() < 11){
            altPhoneText.setError("Alt Phone No must be 11 digits");
            altPhoneText.requestFocus();
        }
        else{
            altPhone = altPhoneText.getText().toString();
        }

        if(emailText.getText().toString().isEmpty()){
            emailText.setError("Email Required");
            emailText.requestFocus();
        }
        else{
            email = emailText.getText().toString();
        }

        if(addressText.getText().toString().isEmpty()){
            addressText.setError("Address Required");
            addressText.requestFocus();
        }
        else{
            address = addressText.getText().toString();
        }

        if(passwordText.getText().toString().isEmpty()){
            passwordText.setError("Password Required");
            passwordText.requestFocus();
        }
        else if(!passwordText.getText().toString().equals(confirmPasswordText.getText().toString())){
            passwordText.setError("Password does not match!");
            confirmPasswordText.requestFocus();
        }
        else if(passwordText.getText().length() < 6){
            passwordText.setError("Password should be at least 6 characters!");
            passwordText.requestFocus();
        }

        else{
            password = passwordText.getText().toString();

        }

        if(!name.isEmpty() && !phone.isEmpty() && !altPhone.isEmpty() &&
                !email.isEmpty() && !address.isEmpty() && !password.isEmpty()){

            progressBar.setVisibility(View.VISIBLE);

            UserDataModel userDataModel = new UserDataModel(name, email, phone, altPhone, address, password);
            RestAPI restAPI = RetrofitClient.createRetrofitClient();
            Call<UserDataModel> signUpCall = restAPI.signupUser(userDataModel);
            signUpCall.enqueue(new Callback<UserDataModel>() {
                @Override
                public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                    if(response.code() == 200){

                        Toast.makeText(getActivity(), "Signup Successful! Please Log In.", Toast.LENGTH_SHORT).show();
                        Log.d("Signup",response.code()+": "+response.message() + " - "+response.body().getMessage());

                        Navigation.findNavController(getActivity(),R.id.login_signup_nav_host_fragment).navigate(R.id.action_navigation_sign_up_to_navigation_login);

//                        Intent intent=new Intent(getActivity(), MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        getActivity().finishAffinity();

                    }

                    else if(response.code() == 400){
                        Gson gson = new GsonBuilder().create();
                        UserDataModel userDataModel;
                        try {

                            progressBar.setVisibility(View.GONE);

                            userDataModel= gson.fromJson(response.errorBody().string(),UserDataModel.class);
                            Toast.makeText(getActivity(),response.code()+": "+response.message()+" - "+userDataModel.getMessage(),Toast.LENGTH_SHORT).show();
                            Log.d("Signup",response.code()+": "+response.message()+" - "+userDataModel.getMessage());
                        } catch (IOException e) {

                        }
                    }
                    else{

                        progressBar.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<UserDataModel> call, Throwable t) {
                    Toast.makeText(getActivity(),"Error: "+t.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.d("Signup","Error: "+t.getMessage());
                    progressBar.setVisibility(View.GONE);


                }
            });
        }
        else{
            //Toast.makeText(getActivity(),"Empty data!",Toast.LENGTH_SHORT).show();
            Log.d("Signup","Empty data!");
            progressBar.setVisibility(View.GONE);


        }
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

            otpTimer.setVisibility(View.VISIBLE);
            enterOtpLabel.setVisibility(View.VISIBLE);
            enterOtpText.setVisibility(View.VISIBLE);
            verifyBtn.setVisibility(View.VISIBLE);
            otpBtn.setEnabled(false);

            new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {
                    isTimerOn=true;

                    if(millisUntilFinished / 1000 > 9) {
                        otpTimer.setText("00:" + millisUntilFinished / 1000);
                    }
                    else{
                        otpTimer.setText("00:0" + millisUntilFinished / 1000);

                    }
                }

                public void onFinish() {
                    isTimerOn=false;

                    otpBtn.setEnabled(!isVerificationCompleted);
                }

            }.start();
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

                            otpBtn.setText("Verified");
                            otpTimer.setVisibility(View.GONE);
                            enterOtpLabel.setVisibility(View.GONE);
                            enterOtpText.setVisibility(View.GONE);
                            verifyBtn.setVisibility(View.GONE);
                            phoneText.setEnabled(false);
                            signupBtn.setEnabled(true);
                            isVerificationCompleted = true;
                            Toast.makeText(getActivity(), "Phone Verification Successful!",Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(getActivity(), "Phone Verification Failed!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }



}