package com.qubitech.oboshor.ui.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.qubitech.oboshor.MainActivity;
import com.qubitech.oboshor.R;
import com.qubitech.oboshor.datamodels.CartDataModel;
import com.qubitech.oboshor.ui.cart.recyclerview.AddRemoveOnclickListener;
import com.qubitech.oboshor.ui.cart.recyclerview.CartAdapter;
import com.qubitech.oboshor.ui.checkout.CheckoutActivity;

import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView cartProductsRecyclerview;
    private TextView appBarTitle,totalAmount;
    private Button checkoutBtn;
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;
    private LiveData<List<CartDataModel>> cartData;
    private ImageView backBtn;
    private ConstraintLayout emptyCartContainer;
    private CardView totalCardView;
    private Button continueShoppingBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cart);


        cartProductsRecyclerview = findViewById(R.id.rv_cart_product_list);
        appBarTitle = findViewById(R.id.fragment_title);
        totalAmount=findViewById(R.id.tv_cart_total);
        checkoutBtn = findViewById(R.id.cart_checkout_btn);
        backBtn = findViewById(R.id.backBtn);
        emptyCartContainer = findViewById(R.id.empty_cart_container);
        totalCardView = findViewById(R.id.cart_total_cardView);
        continueShoppingBtn = findViewById(R.id.continue_shopping_btn);

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        cartData = cartViewModel.getCartData();
        cartAdapter=new CartAdapter(new AddRemoveOnclickListener() {
            @Override
            public void onClick(CartDataModel cartDataModel) {
                cartViewModel.updateQuantity(cartDataModel);
            }
        });
        cartProductsRecyclerview.setAdapter( isNetworkConnected()?cartAdapter: null);
        cartAdapter.getTotalAmount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer total) {
                totalAmount.setText(total +" Tk");
            }
        });
        cartData.observe(this, new Observer<List<CartDataModel>>() {
            @Override
            public void onChanged(List<CartDataModel> cartDataModels) {
                cartAdapter.setData(cartDataModels);
                if(cartDataModels.size()==0){
                    totalCardView.setVisibility(View.GONE);
                    emptyCartContainer.setVisibility(View.VISIBLE);
                }
                else {
                    emptyCartContainer.setVisibility(View.GONE);
                    totalCardView.setVisibility(View.VISIBLE);
                }
            }
        });
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent checkoutIntent = new Intent(CartActivity.this, CheckoutActivity.class);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("order",cartData.getValue());
                checkoutIntent.putExtra("cart-data",hashMap );
                checkoutIntent.putExtra("subtotal",cartAdapter.getTotalAmount().getValue());
                startActivity(checkoutIntent);
            }
        });

        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getStringExtra("fromDashboard")!=null){
                    if(getIntent().getStringExtra("fromDashboard").equals("y")) {
                        finish();
                    }
                    else {
                        startActivity(new Intent(CartActivity.this, MainActivity.class));
                        finishAffinity();
                    }
                }else {
                    startActivity(new Intent(CartActivity.this, MainActivity.class));
                    finishAffinity();
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartActivity.super.onBackPressed();
            }
        });

        appBarTitle.setText("Cart");
        cartProductsRecyclerview.setLayoutManager(new LinearLayoutManager(this));


    }



    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}