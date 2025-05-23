package com.qubitech.oboshor.ui.profile;

import static android.content.Context.MODE_PRIVATE;
import static com.qubitech.oboshor.StaticData.LOGIN_SHARED_PREFS;
import static com.qubitech.oboshor.StaticData.LOGIN_USER_PASS;
import static com.qubitech.oboshor.StaticData.LOGIN_USER_PHONE;
import static com.qubitech.oboshor.StaticData.currentUserData;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.LoginDataModel;
import com.qubitech.oboshor.datamodels.UserDataModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {


    private View root;
    private ImageView profileNameImage, profilePhoneImage, profileAltPhoneImage, profileEmailImage, profileAddressImage;
    private EditText profileNameEdit, profilePhoneEdit, profileAltPhoneEdit, profileEmailEdit, profileAddressEdit;
    private TextView changePassword;
    private ProfileViewModel profileViewModel;
    private String name="Name", phone="Phone No.", altPhone="Alt Phone No.", address="Address", email="Email";

    public ProfileFragment() {
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
            root = inflater.inflate(R.layout.fragment_profile, container, false);

        profileNameImage = root.findViewById(R.id.profile_name_edit);
        profilePhoneImage = root.findViewById(R.id.profile_phone_edit);
        profileAltPhoneImage = root.findViewById(R.id.profile_alt_phone_edit);
        profileAddressImage = root.findViewById(R.id.profile_address_edit);
        profileEmailImage = root.findViewById(R.id.profile_email_edit);
        changePassword = root.findViewById(R.id.profile_change_password);

        profileNameEdit = root.findViewById(R.id.profile_name);
        profilePhoneEdit = root.findViewById(R.id.profile_phone);
        profileAltPhoneEdit = root.findViewById(R.id.profile_alt_phone);
        profileAddressEdit = root.findViewById(R.id.profile_address);
        profileEmailEdit = root.findViewById(R.id.profile_email);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);


        return root;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        profileViewModel.fetchProfileData().observe(getViewLifecycleOwner(), new Observer<UserDataModel>() {
            @Override
            public void onChanged(UserDataModel userDataModel) {

                name = userDataModel.getUsername();
                phone = userDataModel.getPhone1();
                altPhone = userDataModel.getPhone2();
                email = userDataModel.getEmail();
                address = userDataModel.getAddress();

                profileNameEdit.setText(name);
                profilePhoneEdit.setText(phone);
                profileAltPhoneEdit.setText(altPhone);
                profileEmailEdit.setText(email);
                profileAddressEdit.setText(address);
            }
        });

        profileNameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfileAlertDialog("Name",name);
            }
        });

        profilePhoneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfileAlertDialog("Phone No.",phone);
            }
        });

        profileAltPhoneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfileAlertDialog("Alt Phone No.",altPhone);
            }
        });

        profileEmailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfileAlertDialog("Email",email);
            }
        });

        profileAddressImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfileAlertDialog("Address",address);
            }
        });

        profileAddressImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfileAlertDialog("Address",address);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordAlertDialog();
            }
        });
    }

    private void getUserData(){

        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<ArrayList<UserDataModel>> getUserCall = restAPI.getUser(StaticData.currentUserData.getValue().getToken());
        getUserCall.enqueue(new Callback<ArrayList<UserDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserDataModel>> call, Response<ArrayList<UserDataModel>> response) {

                if(response.code() == 200){

                    UserDataModel userDataModel = response.body().get(0);
                    profileNameEdit.setText(userDataModel.getUsername());
                    profilePhoneEdit.setText(userDataModel.getPhone1());
                    profileAltPhoneEdit.setText(userDataModel.getPhone2());
                    profileAddressEdit.setText(userDataModel.getAddress());
                    profileEmailEdit.setText(userDataModel.getEmail());

                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserDataModel>> call, Throwable t) {

            }
        });
    }


    private void updateProfileAlertDialog(String title, String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);
        View dialog=LayoutInflater.from(getContext()).inflate(R.layout.alert_update_single_box,null);
        builder.setView(dialog);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        TextView label = dialog.findViewById(R.id.profile_alert_label);
        EditText messageText = dialog.findViewById(R.id.profile_alert_field);
        ImageView successImage = dialog.findViewById(R.id.profile_alert_image);
        Button submitBtn = dialog.findViewById(R.id.profile_alert_submit_btn);
        Button cancelBtn = dialog.findViewById(R.id.profile_alert_cancel_btn);
        Button successBtn = dialog.findViewById(R.id.profile_alert_submit_btn2);

        label.setText(title);
        messageText.setText(message);

        if(title.equals("Phone No.") || title.equals("Alt Phone No.")){
            messageText.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            messageText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(title.equals("Name")){

                    if(messageText.getText().toString().length() == 0){
                        messageText.setError("Name cannot be empty");
                    }
                    else {
                        name = messageText.getText().toString();
                        updateUser(label, messageText, submitBtn, cancelBtn, successBtn, successImage);

                    }
                }

                else if(title.equals("Phone No.")){


                    if(messageText.getText().toString().length() < 11){
                        messageText.setError("Phone number must be 11 digits");
                    }
                    else {
                        phone = messageText.getText().toString();
                        updateUser(label, messageText, submitBtn, cancelBtn, successBtn, successImage);
                    }

                }

                else if(title.equals("Phone No.")){


                    if(messageText.getText().toString().length() < 11){
                        messageText.setError("Alt Phone number must be 11 digits");
                    }
                    else {
                        altPhone = messageText.getText().toString();
                        updateUser(label, messageText, submitBtn, cancelBtn, successBtn, successImage);
                    }

                }

                else if(title.equals("Address")){


                    if(messageText.getText().toString().length() == 0){
                        messageText.setError("Address cannot be empty");
                    }
                    else {
                        address = messageText.getText().toString();
                        updateUser(label, messageText, submitBtn, cancelBtn, successBtn, successImage);

                    }

                }

                else if(title.equals("Email")){

                    if(messageText.getText().toString().length() == 0){
                        messageText.setError("Email cannot be empty");
                    }
                    else {
                        email = messageText.getText().toString();
                        updateUser(label, messageText, submitBtn, cancelBtn, successBtn, successImage);

                    }
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(view.getWindowToken(), 0);
                alertDialog.dismiss();

            }
        });

        successBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

                profileViewModel.fetchProfileData().observe(getViewLifecycleOwner(), new Observer<UserDataModel>() {
                    @Override
                    public void onChanged(UserDataModel userDataModel) {

                        name = userDataModel.getUsername();
                        phone = userDataModel.getPhone1();
                        altPhone = userDataModel.getPhone2();
                        email = userDataModel.getEmail();
                        address = userDataModel.getAddress();

                        profileNameEdit.setText(name);
                        profilePhoneEdit.setText(phone);
                        profileAltPhoneEdit.setText(altPhone);
                        profileEmailEdit.setText(email);
                        profileAddressEdit.setText(address);
                    }
                });
            }
        });


    }

    private void updateUser(TextView label, TextView messageText, Button submitBtn, Button cancelBtn, Button successBtn, ImageView successImage){
        UserDataModel userDataModel = new UserDataModel(name, email, phone, altPhone, address);

        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<UserDataModel> updateUserCall = restAPI.updateUser(StaticData.currentUserData.getValue().getToken(),StaticData.currentUserData.getValue().getCurrentUser().getId(),userDataModel);
        updateUserCall.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {

                if(response.code() == 200){

                    SharedPreferences loginSharedPrefs = getActivity().getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor loginPrefsEditor = loginSharedPrefs.edit();

                    loginPrefsEditor.putString(LOGIN_USER_PHONE, phone);
                    loginPrefsEditor.apply();

                    currentUserData.setValue(new LoginDataModel(response.body(),currentUserData.getValue().getToken()));

                    label.setText("Profile Updated");
                    label.setTextSize(20);
                    label.setTypeface(null, Typeface.BOLD);
                    label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    successBtn.setText("Ok");

                    messageText.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.GONE);
                    cancelBtn.setVisibility(View.GONE);

                    successImage.setVisibility(View.VISIBLE);
                    successBtn.setVisibility(View.VISIBLE);

                }
                else{
                    label.setText("Update Failed");
                    label.setTextSize(20);
                    label.setTypeface(null, Typeface.BOLD);
                    label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    successImage.setImageResource(R.drawable.ic_close);
                    successBtn.setText("Ok");

                    messageText.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.GONE);
                    cancelBtn.setVisibility(View.GONE);

                    successImage.setVisibility(View.VISIBLE);
                    successBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {

            }
        });
    }


    private void changePasswordAlertDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);
        View dialog=LayoutInflater.from(getContext()).inflate(R.layout.alert_change_password,null);
        builder.setView(dialog);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView labelText = dialog.findViewById(R.id.change_pass_alert_header);
        TextView oldPassLabelText = dialog.findViewById(R.id.tv_change_pass_alert_label_old_pass);
        TextView newPassLabelText = dialog.findViewById(R.id.tv_change_pass_alert_label_new_pass);
        TextView retypePassLabelText = dialog.findViewById(R.id.tv_change_pass_alert_label_retype_pass);

        EditText oldPassText = dialog.findViewById(R.id.et_change_pass_alert_old_pass);
        EditText newPassText = dialog.findViewById(R.id.et_change_pass_alert_new_pass);
        EditText retypePassText = dialog.findViewById(R.id.et_change_pass_alert_retype_pass);

        ImageView oldPassVisibility = dialog.findViewById(R.id.change_pass_alert_visibility_old_pass);
        ImageView newPassVisibility = dialog.findViewById(R.id.change_pass_alert_visibility_new_pass);
        ImageView retypePassVisibility = dialog.findViewById(R.id.change_pass_alert_visibility_retype_pass);

        ImageView successImage = dialog.findViewById(R.id.change_pass_alert_image);
        Button submitBtn = dialog.findViewById(R.id.btn_change_pass_alert_submit);
        Button cancelBtn = dialog.findViewById(R.id.btn_change_pass_alert_cancel);
        Button successBtn = dialog.findViewById(R.id.change_pass_alert_submit_btn2);

        oldPassVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (oldPassText.getTransformationMethod() == null) {
                    oldPassText.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    oldPassText.setTransformationMethod(null);
                }
            }
        });

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

                String oldPass="", newPass="", retypePass="";

                if(oldPassText.getText().toString().isEmpty()){

                    oldPassText.setError("Old Password Required");
                    oldPassText.requestFocus();

                }
                else{
                    oldPass = oldPassText.getText().toString();
                }

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

                if(!oldPass.isEmpty() && !newPass.isEmpty() && !retypePass.isEmpty()){

                    UserDataModel userDataModel = new UserDataModel(StaticData.currentUserData.getValue().getCurrentUser().getId(),oldPass,newPass);
                    RestAPI restAPI = RetrofitClient.createRetrofitClient();
                    Call<UserDataModel> updatePasswordCall = restAPI.updatePassword(StaticData.currentUserData.getValue().getToken(),userDataModel);
                    updatePasswordCall.enqueue(new Callback<UserDataModel>() {
                        @Override
                        public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {

                            if(response.code() == 200){

                                SharedPreferences loginSharedPrefs = getActivity().getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor loginPrefsEditor = loginSharedPrefs.edit();

                                loginPrefsEditor.putString(LOGIN_USER_PHONE, currentUserData.getValue().getCurrentUser().getPhone1());
                                loginPrefsEditor.putString(LOGIN_USER_PASS, newPassText.getText().toString());
                                loginPrefsEditor.apply();

                                currentUserData.setValue(new LoginDataModel(response.body(),currentUserData.getValue().getToken()));

                                labelText.setText("Password Changed");
                                successBtn.setText("Ok");
                                successImage.setImageResource(R.drawable.ic_check_circle);

                                oldPassLabelText.setVisibility(View.GONE);
                                newPassLabelText.setVisibility(View.GONE);
                                retypePassLabelText.setVisibility(View.GONE);
                                oldPassText.setVisibility(View.GONE);
                                newPassText.setVisibility(View.GONE);
                                retypePassText.setVisibility(View.GONE);
                                oldPassVisibility.setVisibility(View.GONE);
                                newPassVisibility.setVisibility(View.GONE);
                                retypePassVisibility.setVisibility(View.GONE);
                                submitBtn.setVisibility(View.GONE);
                                cancelBtn.setVisibility(View.GONE);
                                successImage.setVisibility(View.VISIBLE);
                                successBtn.setVisibility(View.VISIBLE);
                            }

                            else if(response.code() == 400){

                                Toast.makeText(getActivity(),"Invalid Old Password",Toast.LENGTH_LONG).show();
                            }
                            else{

                                labelText.setText("Failed");
                                successBtn.setText("Ok");
                                successImage.setImageResource(R.drawable.ic_close);

                                oldPassLabelText.setVisibility(View.GONE);
                                newPassLabelText.setVisibility(View.GONE);
                                retypePassLabelText.setVisibility(View.GONE);
                                oldPassText.setVisibility(View.GONE);
                                newPassText.setVisibility(View.GONE);
                                retypePassText.setVisibility(View.GONE);
                                oldPassVisibility.setVisibility(View.GONE);
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

                            oldPassLabelText.setVisibility(View.GONE);
                            newPassLabelText.setVisibility(View.GONE);
                            retypePassLabelText.setVisibility(View.GONE);
                            oldPassText.setVisibility(View.GONE);
                            newPassText.setVisibility(View.GONE);
                            retypePassText.setVisibility(View.GONE);
                            oldPassVisibility.setVisibility(View.GONE);
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
                InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

}