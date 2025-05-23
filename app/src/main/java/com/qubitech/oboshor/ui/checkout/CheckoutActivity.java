package com.qubitech.oboshor.ui.checkout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.api.RoomDB;
import com.qubitech.oboshor.api.UserDao;
import com.qubitech.oboshor.datamodels.CartDataModel;
import com.qubitech.oboshor.datamodels.CouponDataModel;
import com.qubitech.oboshor.datamodels.OrderArrayDataModel;
import com.qubitech.oboshor.datamodels.OrderDataModel;
import com.qubitech.oboshor.ui.order.OrderActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {
    private Button placeOrderBtn;
    private TextView addPromoCodeText, subTotalText, subTotalDiscountedText, shippingText, totalText, totalText2,appBarTitle;
    private EditText phoneText, altPhoneText, addressText;
    private ImageView phoneImage, altPhoneImage, addressImage,backBtn;
    private Spinner shippingSpinner,citySpinner;
    private CouponDataModel couponDataModel;
    private HashMap<String, Object> hashMap;
    private ArrayList<CartDataModel> cartDataModels;

    private String[] deliveryOptions = new String[]{"Pre-paid Outside Dhaka - 70 Tk","Pre-paid Inside Dhaka - 50 Tk",
            "Cash-On-Delivery Outside Dhaka - 100 Tk", "Cash-On-Delivery Inside Dhaka - 70 Tk"};
    private int[] deliveryCharges = new int[]{70,50,100,70};
    private String[] paymentMethods = new String[]{"PPOD","PPID","CDOD","CDID"};
    private String[] districts = {};
    private String phone="", altPhone="", address="",paymentMethod="",zilla="";
    private int subTotal, newSubTotal, shipping, discount, total, bookprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_checkout);

        appBarTitle=findViewById(R.id.fragment_title);
        appBarTitle.setText("Checkout");
        backBtn = findViewById(R.id.backBtn);

        phoneImage = findViewById(R.id.checkout_iv_phone_edit);
        altPhoneImage =findViewById(R.id.checkout_iv_phone_alt_edit);
        addressImage = findViewById(R.id.checkout_iv_address_edit);
        phoneText = findViewById(R.id.checkout_et_phone);
        altPhoneText = findViewById(R.id.checkout_et_alt_phone);
        addressText = findViewById(R.id.checkout_et_address);
        subTotalText =findViewById(R.id.checkout_tv_subtotal);
        subTotalDiscountedText = findViewById(R.id.checkout_tv_subtotal_discounted);
        shippingText = findViewById(R.id.checkout_tv_shipping);
        totalText =findViewById(R.id.checkout_tv_total);
        totalText2 =findViewById(R.id.tv_checkout_bottom_card_total);
        addPromoCodeText = findViewById(R.id.checkout_tv_promo_add);
        shippingSpinner = findViewById(R.id.checkout_shipping_spinner);
        placeOrderBtn = findViewById(R.id.checkout_placeorder_btn);
        citySpinner = findViewById(R.id.checkout_city_spinner);

        districts = getResources().getStringArray(R.array.cities);
        Arrays.sort(districts);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, deliveryOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shippingSpinner.setAdapter(spinnerAdapter);

        ArrayAdapter cityAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, districts);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        phone = StaticData.currentUserData.getValue().getCurrentUser().getPhone1();
        altPhone = StaticData.currentUserData.getValue().getCurrentUser().getPhone2();
        address = StaticData.currentUserData.getValue().getCurrentUser().getAddress();
        subTotal = getIntent().getIntExtra("subtotal",0);
        shipping = deliveryCharges[shippingSpinner.getSelectedItemPosition()];
        total = subTotal + shipping - discount;
        bookprice = subTotal + shipping;
        paymentMethod = paymentMethods[shippingSpinner.getSelectedItemPosition()];

        phoneText.setText(phone);
        altPhoneText.setText(altPhone);
        addressText.setText(address);
        subTotalText.setText(subTotal+" Tk");
        shippingText.setText(shipping+" Tk");
        totalText.setText(total+" Tk");
        totalText2.setText(total+" Tk");

        hashMap = new HashMap<>();
        hashMap = (HashMap<String, Object>) getIntent().getSerializableExtra("cart-data");
        cartDataModels = (ArrayList<CartDataModel>) hashMap.get("order");

        if(cartDataModels != null) {
            for (int i = 0; i < cartDataModels.size(); i++) {
                Log.d("Cart-data", cartDataModels.get(i).getBookId() + " " + cartDataModels.get(i).getQuantity());
            }
        }



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckoutActivity.super.onBackPressed();
            }
        });
        phoneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateShippingInfoAlertDialog("Phone No.",phone);
            }
        });

        altPhoneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateShippingInfoAlertDialog("Alternate Phone No.",altPhone);

            }
        });

        addressImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateShippingInfoAlertDialog("Shipping Address",address);

            }
        });

        addPromoCodeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                couponAlertDialog();
            }
        });

        shippingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                shipping = deliveryCharges[i];
                paymentMethod = paymentMethods[i];
                bookprice = subTotal + shipping;
                total = subTotal + shipping - discount;
                shippingText.setText(String.valueOf(shipping)+" Tk");
                totalText.setText(String.valueOf(total)+" Tk");
                totalText2.setText(String.valueOf(total)+" Tk");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookRequestAlertDialog("Place Order?","Tap Yes to place order","Yes","Cancel");
            }
        });

    }

    private void updateShippingInfoAlertDialog(String title, String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(CheckoutActivity.this,R.style.CustomAlertDialog);
        View dialog= LayoutInflater.from(CheckoutActivity.this).inflate(R.layout.alert_update_single_box,null);
        builder.setView(dialog);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        TextView labelText = dialog.findViewById(R.id.profile_alert_label);
        EditText messageText = dialog.findViewById(R.id.profile_alert_field);
        ImageView successImage = dialog.findViewById(R.id.profile_alert_image);
        Button submitBtn = dialog.findViewById(R.id.profile_alert_submit_btn);
        Button cancelBtn = dialog.findViewById(R.id.profile_alert_cancel_btn);
        Button successBtn = dialog.findViewById(R.id.profile_alert_submit_btn2);

        if(title.equals("Phone No.") || title.equals("Alternate Phone No.")){
            messageText.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            messageText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});
        }

        labelText.setText(title);
        messageText.setText(message);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(title.equals("Phone No.")){


                    if(messageText.getText().toString().length() < 11){
                        messageText.setError("Phone number must be 11 digits");
                    }
                    else {
                        phone = messageText.getText().toString();
                        phoneText.setText(phone);
                        alertDialog.dismiss();

                    }

                }
                else if(title.equals("Alternate Phone No.")){


                    if(messageText.getText().toString().length() < 11){
                        messageText.setError("Phone number must be 11 digits");
                    }
                    else {
                        altPhone = messageText.getText().toString();
                        altPhoneText.setText(altPhone);
                        alertDialog.dismiss();

                    }

                }

                else if(title.equals("Shipping Address")){


                    if(messageText.getText().toString().length() == 0){
                        messageText.setError("Address cannot be empty");
                    }
                    else {
                        address = messageText.getText().toString();
                        addressText.setText(address);
                        alertDialog.dismiss();
                    }

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


    private void couponAlertDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(CheckoutActivity.this,R.style.CustomAlertDialog);
        View dialog=LayoutInflater.from(CheckoutActivity.this).inflate(R.layout.alert_update_single_box,null);
        builder.setView(dialog);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        TextView labelText = dialog.findViewById(R.id.profile_alert_label);
        EditText messageText = dialog.findViewById(R.id.profile_alert_field);
        ImageView successImage = dialog.findViewById(R.id.profile_alert_image);
        Button submitBtn = dialog.findViewById(R.id.profile_alert_submit_btn);
        Button cancelBtn = dialog.findViewById(R.id.profile_alert_cancel_btn);
        Button successBtn = dialog.findViewById(R.id.profile_alert_submit_btn2);

        labelText.setText("Promo code");
        submitBtn.setText("Check");
        cancelBtn.setText("Cancel");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = "";

                if (messageText.getText().toString().isEmpty()) {

                    messageText.setError("Promo code required");
                    messageText.requestFocus();
                } else {
                    message = messageText.getText().toString();
                }

                if (!message.isEmpty()) {

                    RestAPI restAPI = RetrofitClient.createRetrofitClient();
                    Call<CouponDataModel> checkCouponCall = restAPI.checkCoupon(StaticData.currentUserData.getValue().getToken(), message);
                    checkCouponCall.enqueue(new Callback<CouponDataModel>() {
                        @Override
                        public void onResponse(Call<CouponDataModel> call, Response<CouponDataModel> response) {

                            if (response.code() == 200) {

                                couponDataModel = response.body();
                                if(couponDataModel.getCouponType().equals("flat")){

                                    discount = Integer.parseInt(couponDataModel.getAmount());
                                    newSubTotal = subTotal - discount;
                                    total = subTotal + shipping - discount;
                                    bookprice = subTotal + shipping;
                                    subTotalText.setPaintFlags(subTotalText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    subTotalDiscountedText.setText(newSubTotal+" Tk");
                                    subTotalDiscountedText.setVisibility(View.VISIBLE);
                                    addPromoCodeText.setText(discount+" Tk");
                                    totalText.setText(total+" Tk");
                                    totalText2.setText(total+" Tk");
                                }
                                else if(couponDataModel.getCouponType().equals("percantage")){

                                    discount = Integer.parseInt(couponDataModel.getAmount())*subTotal/100;
                                    newSubTotal = subTotal - discount;
                                    total = subTotal + shipping - discount;
                                    bookprice = subTotal + shipping;
                                    subTotalText.setPaintFlags(subTotalText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    subTotalDiscountedText.setText(newSubTotal+" Tk");
                                    subTotalDiscountedText.setVisibility(View.VISIBLE);
                                    addPromoCodeText.setText(discount+" Tk");
                                    totalText.setText(total+" Tk");
                                    totalText2.setText(total+" Tk");
                                }
                                Toast.makeText(CheckoutActivity.this, "Discount available: "+couponDataModel.getAmount()+" "+couponDataModel.getCouponType(), Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                            else {
                                messageText.setError("Sorry! Voucher is not valid.");
                                messageText.requestFocus();
                                subTotalDiscountedText.setVisibility(View.GONE);
                                total = subTotal + shipping;
                                bookprice = subTotal + shipping;
                                addPromoCodeText.setText("0 Tk");
                                totalText.setText(total+" Tk");
                                totalText2.setText(total+" Tk");

                            }
                        }

                        @Override
                        public void onFailure(Call<CouponDataModel> call, Throwable t) {

                            Toast.makeText(CheckoutActivity.this, "Error occurred with server", Toast.LENGTH_SHORT).show();
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

    private void bookRequestAlertDialog(String title, String message, String submit, String cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this, R.style.CustomAlertDialog);

        View view = LayoutInflater.from(CheckoutActivity.this).inflate(R.layout.alert_custom_title, null);
        TextView titleText = view.findViewById(R.id.alert_custom_title);
        TextView messageText = view.findViewById(R.id.alert_custom_message);
        Button submitBtn = view.findViewById(R.id.alert_custom_title_submit_btn);
        Button cancelBtn = view.findViewById(R.id.alert_custom_title_cancel_btn);

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

                verifyOrder();
                alertDialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(view.getWindowToken(), 0);
                alertDialog.dismiss();
            }
        });
    }

    private void verifyOrder(){

        if (altPhone == null || altPhone.isEmpty()) {

            Toast.makeText(this, "Alt Phone required!", Toast.LENGTH_SHORT).show();
            altPhoneText.setError("Alt Phone required!");
            altPhoneText.requestFocus();
        }

        else{
            placeOrder();
        }
    }


    private void placeOrder(){


        ArrayList<OrderArrayDataModel> newOrderDataModels = new ArrayList<>();
        for(int i=0;i<cartDataModels.size();i++){

            OrderArrayDataModel orderArrayDataModel = new OrderArrayDataModel(cartDataModels.get(i).getBookId(), cartDataModels.get(i).getQuantity());
            newOrderDataModels.add(orderArrayDataModel);

        }

        zilla =citySpinner.getSelectedItem().toString();
        OrderDataModel orderDataModel = new OrderDataModel("Placed",phone,altPhone,address,String.valueOf(bookprice),String.valueOf(total),paymentMethod,zilla,newOrderDataModels,couponDataModel);
        RestAPI restAPI = RetrofitClient.createRetrofitClient();

        Dialog dialog = new Dialog(CheckoutActivity.this, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View root = LayoutInflater.from(CheckoutActivity.this).inflate(R.layout.fragment_order_placed, null);
        TextView orderPlacedLabel = root.findViewById(R.id.tv_order_placed_label);
        TextView orderPlacedMessage = root.findViewById(R.id.tv_order_placed_msg);
        ImageView orderPlacedImg = root.findViewById(R.id.iv_order_placed_image);
        ImageView closeImg = root.findViewById(R.id.iv_order_placed_closeBtn);
        Button trackOrderBtn = root.findViewById(R.id.order_placed_track_order_btn);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();

            }
        });
        Intent trackOrderIntent = new Intent(CheckoutActivity.this, OrderActivity.class);

        trackOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(trackOrderIntent);
                finish();
            }
        });

        Log.d("Order","Address: "+orderDataModel.getAddress());

        Call<OrderDataModel> placeOrderCall = restAPI.uploadNewOrder(StaticData.currentUserData.getValue().getToken(),orderDataModel);
        placeOrderCall.enqueue(new Callback<OrderDataModel>() {
            @Override
            public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {


                if(response.code() == 200){

                    RoomDB database = RoomDB.getInstance(CheckoutActivity.this);
                    UserDao userDao = database.userDao();
                    userDao.clearCart();

                    orderPlacedLabel.setText("Order Placed");
                    orderPlacedMessage.setText("Your order is placed successfully");
                    orderPlacedImg.setImageResource(R.drawable.ic_check_circle);
                    trackOrderIntent.putExtra("status","successful");



//                    Bundle bundle = new Bundle();
//                    bundle.putString("status","successful");
//                    Navigation.findNavController(CheckoutActivity.this,R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_checkout_to_navigation_order_placed,bundle);
                }
                else{

                    Log.d("Order","Response Code: "+response.code()+" :"+response.message());
                    orderPlacedLabel.setText("Order Failed");
                    orderPlacedMessage.setText("Your order is unsuccessful");
                    orderPlacedImg.setImageResource(R.drawable.ic_close);
                    trackOrderIntent.putExtra("status","failed");
//                    Bundle bundle = new Bundle();
//                    bundle.putString("status","failed");
//                    Navigation.findNavController(CheckoutActivity.this,R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_checkout_to_navigation_order_placed,bundle);
                }
                dialog.setContentView(root);
                dialog.show();
            }

            @Override
            public void onFailure(Call<OrderDataModel> call, Throwable t) {

                orderPlacedLabel.setText("Order Failed");
                orderPlacedMessage.setText("Your order is unsuccessful");
                orderPlacedImg.setImageResource(R.drawable.ic_close);

                dialog.setContentView(root);
                dialog.show();
            }
        });
    }
}