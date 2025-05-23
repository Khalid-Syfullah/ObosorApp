package com.qubitech.oboshor.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.BookDataModel;
import com.qubitech.oboshor.datamodels.OrderDataModel;
import com.qubitech.oboshor.ui.dashboard.recyclerview.BookAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {
    private TextView appBarTitle, orderPlacedHeading, orderPlacedSubtitle, orderPendingHeading, orderPendingSubtitle, orderConfirmedHeading, orderConfirmedSubtitle, orderProcessingHeading, orderProcessingSubtitle,
            orderShippedHeading, orderShippedSubtitle, orderDeliveredHeading, orderDeliveredSubtitle, id, status, books, payment, date, phone, altPhone, address;
    private ImageView backBtn, orderPlacedImage, orderPendingImage, orderConfirmedImage, orderProcessingImage, orderShippedImage, orderDeliveredImage;
    private CardView orderPlacedImageCardView, orderPendingImageCardView, orderConfirmedImageCardView, orderProcessingImageCardView, orderShippedImageCardView, orderDeliveredImageCardView;

    private RecyclerView orderBookRecycler;
    private ArrayList<BookDataModel> bookDataModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order_details);

        appBarTitle = findViewById(R.id.fragment_title);
        backBtn = findViewById(R.id.backBtn);

        id = findViewById(R.id.order_details_order_id);
        status = findViewById(R.id.order_details_order_status);
        books = findViewById(R.id.order_details_order_books);
        payment = findViewById(R.id.order_details_order_payment);
        date = findViewById(R.id.order_details_order_date);
        phone = findViewById(R.id.order_details_order_phone);
        altPhone = findViewById(R.id.order_details_order_alt_phone);
        address = findViewById(R.id.order_details_order_address);

        orderBookRecycler = findViewById(R.id.order_details_order_book_recyclerview);


        orderPlacedHeading = findViewById(R.id.order_details_track_order_placed_heading);
        orderPendingHeading = findViewById(R.id.order_details_track_order_pending_heading);
        orderConfirmedHeading = findViewById(R.id.order_details_track_order_confirmed_heading);
        orderProcessingHeading = findViewById(R.id.order_details_track_order_processing_heading);
        orderShippedHeading = findViewById(R.id.order_details_track_order_shipped_heading);
        orderDeliveredHeading = findViewById(R.id.order_details_track_order_delivered_heading);

        orderPlacedSubtitle = findViewById(R.id.order_details_track_order_placed_subtitle);
        orderPendingSubtitle = findViewById(R.id.order_details_track_order_pending_subtitle);
        orderConfirmedSubtitle = findViewById(R.id.order_details_track_order_confirmed_subtitle);
        orderProcessingSubtitle = findViewById(R.id.order_details_track_order_processing_subtitle);
        orderShippedSubtitle = findViewById(R.id.order_details_track_order_shipped_subtitle);
        orderDeliveredSubtitle = findViewById(R.id.order_details_track_order_delivered_subtitle);



        orderPlacedImage = findViewById(R.id.order_details_track_order_placed_image);
        orderPendingImage = findViewById(R.id.order_details_track_order_pending_image);
        orderConfirmedImage = findViewById(R.id.order_details_track_order_confirmed_image);
        orderProcessingImage = findViewById(R.id.order_details_track_order_processing_image);
        orderShippedImage = findViewById(R.id.order_details_track_order_shipped_image);
        orderDeliveredImage = findViewById(R.id.order_details_track_order_delivered_image);

        orderPlacedImageCardView = findViewById(R.id.track_image_card_placed);
        orderPendingImageCardView = findViewById(R.id.track_image_card_pending);
        orderConfirmedImageCardView = findViewById(R.id.track_image_card_confirmed);
        orderProcessingImageCardView = findViewById(R.id.track_image_card_processing);
        orderShippedImageCardView = findViewById(R.id.track_image_card_shipped);
        orderDeliveredImageCardView = findViewById(R.id.track_image_card_delivered);

        ConstraintLayout constraintLayout = findViewById(R.id.order_details_track_order_constraint_layout);

        appBarTitle.setText("Order Details");
        StaticData.CURRENT_FRAGMENT="order-details";

        bookDataModels = new ArrayList<>();

        Bundle bundle = getIntent().getBundleExtra("bundle");
        OrderDataModel orderDataModel = (OrderDataModel) bundle.getSerializable("data-model");

        int totalBooks = 0;

        for(int i=0;i<orderDataModel.getOrder().size();i++){

            totalBooks+= orderDataModel.getOrder().get(i).getQuantity();
        }
        id.setText(orderDataModel.getId());
        status.setText(orderDataModel.getStatus());
        books.setText(String.valueOf(totalBooks));
        payment.setText(orderDataModel.getTotalprice()+" tk");
        date.setText(orderDataModel.getDate());
        phone.setText(orderDataModel.getPhone());
        altPhone.setText(orderDataModel.getPhone2());
        address.setText(orderDataModel.getAddress());

        for(int i=0;i<orderDataModel.getOrder().size();i++){


            BookDataModel bookDataModel = orderDataModel.getOrder().get(i).getBook();
            bookDataModel.setQuantity(orderDataModel.getOrder().get(i).getQuantity());
            bookDataModels.add(bookDataModel);

        }

        BookAdapter bookAdapter = new BookAdapter(this, bookDataModels, 1);
        orderBookRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        orderBookRecycler.setAdapter(bookAdapter);

//        Log.d("BookID(0)","ID="+orderDataModel.getOrder().get(0).getId());
//
//        String s="";
//        for(int i=0;i<orderDataModel.getOrder().size();i++) {
//
//            if(i==0) {
//                s += orderDataModel.getOrder().get(i).getId();
//            }
//            else{
//                s += ","+orderDataModel.getOrder().get(i).getId();
//
//            }
//
//        }
//
//        Log.d("s","String = "+s);
//
//        RestAPI restAPI = RetrofitClient.createRetrofitClient();
//        Call<ArrayList<BookDataModel>> bookDetailsCall = restAPI.getBookDetails(s);
//        bookDetailsCall.enqueue(new Callback<ArrayList<BookDataModel>>() {
//            @Override
//            public void onResponse(Call<ArrayList<BookDataModel>> call, Response<ArrayList<BookDataModel>> response) {
//
//                if (response.code() == 200) {
//
//
//                    for(int j=0;j<bookDataModels.size();j++){
//
//                        BookDataModel bookDataModel = bookDataModels.get(j);
//                        bookDataModel.setQuantity(orderDataModel.getOrder().get(j).getQuantity());
//                        bookDataModels.add(bookDataModel);
//                    }
//                    bookAdapter.setBookDataModels(bookDataModels);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<BookDataModel>> call, Throwable t) {
//
//            }
//        });

        String status = getIntent().getStringExtra("order-status");
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetailsActivity.super.onBackPressed();
            }
        });

        if(status.equals("Placed")){

            orderPlacedHeading.setText("Order Placed");
            orderPlacedSubtitle.setText("Order Placed successfully");
            orderPlacedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderPlacedImage.setImageResource(R.drawable.order_placed_2);

            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.START,R.id.order_details_track_order_progress_background,ConstraintSet.START,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.END,R.id.order_details_track_order_progress_background,ConstraintSet.END,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.TOP,R.id.order_details_track_order_progress_background,ConstraintSet.TOP,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.BOTTOM,R.id.track_image_card_placed,ConstraintSet.BOTTOM,0);
            constraintSet.applyTo(constraintLayout);

            Log.d("OrderTrack","Placed");
        }

        else if(status.equals("Canceled")){

            orderPlacedHeading.setText("Order Canceled");
            orderPlacedSubtitle.setText("Your Order has been canceled");
            orderPlacedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderPlacedImage.setImageResource(R.drawable.order_canceled_2);

            orderPendingHeading.setText("Not applicable");
            orderPendingSubtitle.setText("Not applicable");
            orderPendingImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderPendingImage.setImageResource(R.drawable.order_pending_1);

            orderConfirmedHeading.setText("Not applicable");
            orderConfirmedSubtitle.setText("Not applicable");
            orderConfirmedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderConfirmedImage.setImageResource(R.drawable.order_confirmed_1);

            orderProcessingHeading.setText("Not applicable");
            orderProcessingSubtitle.setText("Not applicable");
            orderProcessingImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderProcessingImage.setImageResource(R.drawable.order_processing_1);

            orderShippedHeading.setText("Not applicable");
            orderShippedSubtitle.setText("Not applicable");
            orderShippedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderShippedImage.setImageResource(R.drawable.order_shipped_1);

            orderDeliveredHeading.setText("Not applicable");
            orderDeliveredSubtitle.setText("Not applicable");
            orderDeliveredImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderDeliveredImage.setImageResource(R.drawable.order_delivered_1);

//            orderPendingHeading.setVisibility(View.INVISIBLE);
//            orderConfirmedHeading.setVisibility(View.INVISIBLE);
//            orderProcessingHeading.setVisibility(View.INVISIBLE);
//            orderShippedHeading.setVisibility(View.INVISIBLE);
//            orderDeliveredHeading.setVisibility(View.INVISIBLE);
//
//            orderPendingSubtitle.setVisibility(View.INVISIBLE);
//            orderConfirmedSubtitle.setVisibility(View.INVISIBLE);
//            orderProcessingSubtitle.setVisibility(View.INVISIBLE);
//            orderShippedSubtitle.setVisibility(View.INVISIBLE);
//            orderDeliveredSubtitle.setVisibility(View.INVISIBLE);


//            orderPendingImageCardView.setVisibility(View.INVISIBLE);
//            orderConfirmedImageCardView.setVisibility(View.INVISIBLE);
//            orderProcessingImageCardView.setVisibility(View.INVISIBLE);
//            orderShippedImageCardView.setVisibility(View.INVISIBLE);
//            orderDeliveredImageCardView.setVisibility(View.INVISIBLE);
//
//            orderPendingCardView.setVisibility(View.INVISIBLE);
//            orderConfirmedCardView.setVisibility(View.INVISIBLE);
//            orderProcessingCardView.setVisibility(View.INVISIBLE);
//            orderShippedCardView.setVisibility(View.INVISIBLE);
//            orderDeliveredCardView.setVisibility(View.INVISIBLE);


            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.START,R.id.order_details_track_order_progress_background,ConstraintSet.START,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.END,R.id.order_details_track_order_progress_background,ConstraintSet.END,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.TOP,R.id.order_details_track_order_progress_background,ConstraintSet.TOP,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.BOTTOM,R.id.track_image_card_placed,ConstraintSet.BOTTOM,0);
            constraintSet.applyTo(constraintLayout);

            Log.d("OrderTrack","Canceled");
        }

        else if(status.equals("Pending")){

            orderPlacedHeading.setText("Order Placed");
            orderPlacedSubtitle.setText("Your Order is placed successfully");
            orderPlacedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderPlacedImage.setImageResource(R.drawable.order_placed_2);

            orderPendingHeading.setText("Order Pending");
            orderPendingSubtitle.setText("Your Order has been received");
            orderPendingImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderPendingImage.setImageResource(R.drawable.order_processing_2);

            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.START,R.id.order_details_track_order_progress_background,ConstraintSet.START,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.END,R.id.order_details_track_order_progress_background,ConstraintSet.END,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.TOP,R.id.order_details_track_order_progress_background,ConstraintSet.TOP,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.BOTTOM,R.id.track_image_card_placed,ConstraintSet.BOTTOM,0);
            constraintSet.applyTo(constraintLayout);

            Log.d("OrderTrack","Pending");
        }

        else if(status.equals("Confirm")){

            orderPlacedHeading.setText("Order Placed");
            orderPlacedSubtitle.setText("Order Placed successfully");
            orderPlacedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderPlacedImage.setImageResource(R.drawable.order_placed_2);

            orderPendingHeading.setText("Order Pending");
            orderPendingSubtitle.setText("Your Order has been received");
            orderPendingImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderPendingImage.setImageResource(R.drawable.order_pending_2);

            orderConfirmedHeading.setText("Order Confirmed");
            orderConfirmedSubtitle.setText("Order Confirmed successfully");
            orderConfirmedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderConfirmedImage.setImageResource(R.drawable.order_confirmed_2);


            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.START,R.id.order_details_track_order_progress_background,ConstraintSet.START,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.END,R.id.order_details_track_order_progress_background,ConstraintSet.END,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.TOP,R.id.order_details_track_order_progress_background,ConstraintSet.TOP,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.BOTTOM,R.id.track_image_card_confirmed,ConstraintSet.BOTTOM,0);
            constraintSet.applyTo(constraintLayout);


            Log.d("OrderTrack","Confirm");
        }


        else if(status.equals("Confirm")){

            orderPlacedHeading.setText("Order Placed");
            orderPlacedSubtitle.setText("Order Placed successfully");
            orderPlacedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderPlacedImage.setImageResource(R.drawable.order_placed_2);

            orderPendingHeading.setText("Order Pending");
            orderPendingSubtitle.setText("Your Order has been received");
            orderPendingImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderPendingImage.setImageResource(R.drawable.order_pending_2);

            orderConfirmedHeading.setText("Order Confirmed");
            orderConfirmedSubtitle.setText("Order Confirmed successfully");
            orderConfirmedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderConfirmedImage.setImageResource(R.drawable.order_confirmed_2);


            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.START,R.id.order_details_track_order_progress_background,ConstraintSet.START,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.END,R.id.order_details_track_order_progress_background,ConstraintSet.END,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.TOP,R.id.order_details_track_order_progress_background,ConstraintSet.TOP,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.BOTTOM,R.id.track_image_card_confirmed,ConstraintSet.BOTTOM,0);
            constraintSet.applyTo(constraintLayout);


            Log.d("OrderTrack","Confirm");
        }


        else if(status.equals("Processing")){

            orderPlacedHeading.setText("Order Placed");
            orderPlacedSubtitle.setText("Order Placed successfully");
            orderPlacedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderPlacedImage.setImageResource(R.drawable.order_placed_2);

            orderPendingHeading.setText("Order Pending");
            orderPendingSubtitle.setText("Your Order has been received");
            orderPendingImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderPendingImage.setImageResource(R.drawable.order_pending_2);

            orderConfirmedHeading.setText("Order Confirmed");
            orderConfirmedSubtitle.setText("Order Confirmed successfully");
            orderConfirmedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderConfirmedImage.setImageResource(R.drawable.order_confirmed_2);

            orderProcessingHeading.setText("Order Processing");
            orderProcessingSubtitle.setText("Order Processing successfully");
            orderProcessingImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderProcessingImage.setImageResource(R.drawable.order_processing_2);

            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.START,R.id.order_details_track_order_progress_background,ConstraintSet.START,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.END,R.id.order_details_track_order_progress_background,ConstraintSet.END,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.TOP,R.id.order_details_track_order_progress_background,ConstraintSet.TOP,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.BOTTOM,R.id.track_image_card_processing,ConstraintSet.BOTTOM,0);
            constraintSet.applyTo(constraintLayout);

            Log.d("OrderTrack","Processing");
        }

        else if(status.equals("Shipped")){

            orderPlacedHeading.setText("Order Placed");
            orderPlacedSubtitle.setText("Order Placed successfully");
            orderPlacedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderPlacedImage.setImageResource(R.drawable.order_placed_2);

            orderPendingHeading.setText("Order Pending");
            orderPendingSubtitle.setText("Your Order has been received");
            orderPendingImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderPendingImage.setImageResource(R.drawable.order_pending_2);

            orderConfirmedHeading.setText("Order Confirmed");
            orderConfirmedSubtitle.setText("Order Confirmed successfully");
            orderConfirmedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderConfirmedImage.setImageResource(R.drawable.order_confirmed_2);

            orderProcessingHeading.setText("Order Processing");
            orderProcessingSubtitle.setText("Order Processing successfully");
            orderProcessingImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderProcessingImage.setImageResource(R.drawable.order_processing_2);

            orderShippedHeading.setText("Order Shipped");
            orderShippedSubtitle.setText("Order Shipped successfully");
            orderShippedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderShippedImage.setImageResource(R.drawable.order_shipped_2);

            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.START,R.id.order_details_track_order_progress_background,ConstraintSet.START,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.END,R.id.order_details_track_order_progress_background,ConstraintSet.END,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.TOP,R.id.order_details_track_order_progress_background,ConstraintSet.TOP,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.BOTTOM,R.id.track_image_card_shipped,ConstraintSet.BOTTOM,0);
            constraintSet.applyTo(constraintLayout);

            Log.d("OrderTrack","Shipped");
        }

        else if(status.equals("Delivered")){

            orderPlacedHeading.setText("Order Placed");
            orderPlacedSubtitle.setText("Order Placed successfully");
            orderPlacedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderPlacedImage.setImageResource(R.drawable.order_placed_2);

            orderPendingHeading.setText("Order Pending");
            orderPendingSubtitle.setText("Your Order has been received");
            orderPendingImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderPendingImage.setImageResource(R.drawable.order_pending_2);

            orderConfirmedHeading.setText("Order Confirmed");
            orderConfirmedSubtitle.setText("Order Confirmed successfully");
            orderConfirmedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderConfirmedImage.setImageResource(R.drawable.order_confirmed_2);

            orderProcessingHeading.setText("Order Processing");
            orderProcessingSubtitle.setText("Order Processing successfully");
            orderProcessingImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderProcessingImage.setImageResource(R.drawable.order_processing_2);

            orderShippedHeading.setText("Order Shipped");
            orderShippedSubtitle.setText("Order Shipped successfully");
            orderShippedImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderShippedImage.setImageResource(R.drawable.order_shipped_2);

            orderDeliveredHeading.setText("Order Delivered");
            orderDeliveredSubtitle.setText("Order Delivered successfully");
            orderDeliveredImageCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#52341E")));
            orderDeliveredImage.setImageResource(R.drawable.order_delivered_2);

            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.START,R.id.order_details_track_order_progress_background,ConstraintSet.START,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.END,R.id.order_details_track_order_progress_background,ConstraintSet.END,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.TOP,R.id.order_details_track_order_progress_background,ConstraintSet.TOP,0);
            constraintSet.connect(R.id.order_details_track_order_progress,ConstraintSet.BOTTOM,R.id.track_image_card_delivered,ConstraintSet.BOTTOM,0);
            constraintSet.applyTo(constraintLayout);

            Log.d("OrderTrack","Delivered");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        StaticData.CURRENT_FRAGMENT = "";
    }
}