package com.qubitech.oboshor.ui.wishlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qubitech.oboshor.MainActivity;
import com.qubitech.oboshor.R;
import com.qubitech.oboshor.datamodels.BookDataModel;
import com.qubitech.oboshor.datamodels.CartDataModel;
import com.qubitech.oboshor.datamodels.WishListDataModel;
import com.qubitech.oboshor.ui.cart.CartActivity;
import com.qubitech.oboshor.ui.cart.CartViewModel;
import com.qubitech.oboshor.ui.wishlist.recycler.WishListOnclickListener;
import com.qubitech.oboshor.ui.wishlist.recycler.WishlistAdapter;

import java.util.List;

public class WishlistActivity extends AppCompatActivity {
    private RecyclerView wishlistRecycler;
    private TextView appBarTitle;
    private WishlistViewModel wishlistViewModel;
    private LiveData<List<WishListDataModel>> wishListLiveData;
    private WishlistAdapter wishlistAdapter;
    private CartViewModel cartViewModel;
    private ImageView backBtn;
    private ConstraintLayout emptyWishlistContainer;
    private Button backToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_wishlist);
        wishlistViewModel = new ViewModelProvider(this).get(WishlistViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        wishlistRecycler = findViewById(R.id.rv_wishlist_items);
        appBarTitle = findViewById(R.id.fragment_title);
        backBtn = findViewById(R.id.backBtn);

        emptyWishlistContainer = findViewById(R.id.empty_wishlist_container);
        backToHome = findViewById(R.id.back_to_home_btn);
        appBarTitle.setText("WishList");


        wishListLiveData = wishlistViewModel.getWishlistData();
        wishlistAdapter = new WishlistAdapter(this, new WishListOnclickListener() {
            @Override
            public void onclickAddToCart(WishListDataModel wishListData) {
                cartViewModel.insert(new CartDataModel(1, wishListData.getBookId(), wishListData.getTitle(), wishListData.getAuthor(), wishListData.getCategory(), wishListData.getPrice(),
                        wishListData.getImageUrl()));
            }

            @Override
            public void onclickRemove(String bookId) {
                wishlistViewModel.deleteWishlistItem(bookId);
            }


        });
        wishlistRecycler.setAdapter(wishlistAdapter);
        wishlistRecycler.setLayoutManager(new LinearLayoutManager(this));

        wishListLiveData.observe(this, new Observer<List<WishListDataModel>>() {
            @Override
            public void onChanged(List<WishListDataModel> wishListDataModels) {
                wishlistAdapter.setData(wishListDataModels);

                if (wishListDataModels.size() == 0) {
                    emptyWishlistContainer.setVisibility(View.VISIBLE);
                } else {
                    emptyWishlistContainer.setVisibility(View.GONE);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WishlistActivity.super.onBackPressed();
            }
        });

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getStringExtra("fromDashboard") != null) {
                    if (getIntent().getStringExtra("fromDashboard").equals("y")) {
                        finish();
                    } else {
                        startActivity(new Intent(WishlistActivity.this, MainActivity.class));
                        finishAffinity();
                    }
                } else {

                    startActivity(new Intent(WishlistActivity.this, MainActivity.class));
                    finishAffinity();
                }
            }
        });
    }
}