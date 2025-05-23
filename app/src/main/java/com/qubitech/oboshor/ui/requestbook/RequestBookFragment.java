package com.qubitech.oboshor.ui.requestbook;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.BookDataModel;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestBookFragment extends Fragment {


    private  View root;
    private BookDataModel bookDataModel;
    private EditText bookNameText, authorNameText, phoneText;
    private Button requestBtn;


    public RequestBookFragment() {
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
            root = inflater.inflate(R.layout.fragment_request_book, container, false);

        bookNameText = root.findViewById(R.id.request_book_et_name);
        authorNameText = root.findViewById(R.id.request_book_et_author_name);
        phoneText = root.findViewById(R.id.request_book_et_phone);
        requestBtn = root.findViewById(R.id.request_book_btn);


        return root;

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyRequestBook();
            }
        });


    }

    private void verifyRequestBook(){

        String bookName="", authorName="", phone="";


        if(bookNameText.getText().toString().isEmpty()){

            bookNameText.setError("Book Name required");
            bookNameText.requestFocus();

        }
        else{
            bookName = bookNameText.getText().toString();
        }

        if(authorNameText.getText().toString().isEmpty()){

            authorNameText.setError("Author Name required");
            authorNameText.requestFocus();

        }
        else{
            authorName = authorNameText.getText().toString();
        }

        if(phoneText.getText().toString().isEmpty()){

            phoneText.setError("Phone number required");
            phoneText.requestFocus();

        }
        else{
            phone = phoneText.getText().toString();
        }

        if(!bookName.isEmpty() && !authorName.isEmpty() && !phone.isEmpty()){

            bookDataModel = new BookDataModel(bookName, authorName, phone);
            bookRequestAlertDialog("Are you sure?","Tap Yes to confirm book request", "Yes", "No","Book Requested","Request Failed");
            //uploadBookRequest(bookDataModel);
        }


    }

    private void bookRequestAlertDialog(String title, String message, String submit, String cancel, String success, String failure){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alert_custom_title,null);
        TextView titleText = view.findViewById(R.id.alert_custom_title);
        TextView messageText = view.findViewById(R.id.alert_custom_message);
        ImageView successImg = view.findViewById(R.id.alert_custom_image);
        Button submitBtn = view.findViewById(R.id.alert_custom_title_submit_btn);
        Button cancelBtn = view.findViewById(R.id.alert_custom_title_cancel_btn);
        Button successBtn = view.findViewById(R.id.alert_custom_title_submit_btn2);

        titleText.setText(title);
        messageText.setText(message);
        submitBtn.setText(submit);
        cancelBtn.setText(cancel);

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                RestAPI restAPI = RetrofitClient.createRetrofitClient();
                Call<BookDataModel> requestBookCall = restAPI.requestBook(StaticData.currentUserData.getValue().getToken(), bookDataModel);
                requestBookCall.enqueue(new Callback<BookDataModel>() {
                    @Override
                    public void onResponse(Call<BookDataModel> call, Response<BookDataModel> response) {

                        if(response.code() == 200){
                            titleText.setText(success);
                            titleText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            successBtn.setText("Ok");

                            messageText.setVisibility(View.GONE);
                            submitBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                            successImg.setVisibility(View.VISIBLE);
                            successBtn.setVisibility(View.VISIBLE);
                        }

                        else{
                            titleText.setText(failure);
                            titleText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            successBtn.setText("Ok");
                            successImg.setImageResource(R.drawable.ic_close);

                            messageText.setVisibility(View.GONE);
                            submitBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                            successImg.setVisibility(View.VISIBLE);
                            successBtn.setVisibility(View.VISIBLE);

                            Log.d("Response","Response Code: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<BookDataModel> call, Throwable t) {

                    }
                });

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

                bookNameText.setText("");
                authorNameText.setText("");
                phoneText.setText("");

            }
        });


    }
}