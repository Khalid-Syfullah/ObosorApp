package com.qubitech.oboshor;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.amulyakhare.textdrawable.TextDrawable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import androidx.navigation.Navigation;


import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.BookDataModel;
import com.qubitech.oboshor.datamodels.CartDataModel;

import com.qubitech.oboshor.datamodels.LoginDataModel;
import com.qubitech.oboshor.datamodels.WishListDataModel;
import com.qubitech.oboshor.ui.cart.CartActivity;
import com.qubitech.oboshor.ui.cart.CartViewModel;
import com.qubitech.oboshor.ui.notifications.NotificationActivity;
import com.qubitech.oboshor.ui.notifications.NotificationsFragment;
import com.qubitech.oboshor.ui.wishlist.WishlistActivity;
import com.qubitech.oboshor.ui.wishlist.WishlistViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.qubitech.oboshor.StaticData.currentUserData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView menuBtn, wishlistBtn, cartBtn;
    private TextView wishlistItemCount, cartItemCount;
    private NavigationView navigationView;
    private CartViewModel cartViewModel;
    private WishlistViewModel wishlistViewModel;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        menuBtn = findViewById(R.id.main_app_bar_menu);
        wishlistBtn = findViewById(R.id.main_app_bar_wishlist);
        cartBtn = findViewById(R.id.main_app_bar_cart);
        navigationView = findViewById(R.id.nav_drawer_view);
        wishlistItemCount = findViewById(R.id.main_wishlist_item_count);
        cartItemCount = findViewById(R.id.main_cart_item_count);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        wishlistViewModel = new ViewModelProvider(this).get(WishlistViewModel.class);
        drawerLayout = findViewById(R.id.drawer_layout);

        View header = navigationView.getHeaderView(0);
        ImageView headerImage = header.findViewById(R.id.nav_header_image);
        TextView headerText = header.findViewById(R.id.nav_header_title);
        TextView headerSubtitle = header.findViewById(R.id.nav_header_subtitle);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(navigationView, navController);



        wishlistViewModel.getWishlistData().observe(this, new Observer<List<WishListDataModel>>() {
            @Override
            public void onChanged(List<WishListDataModel> wishListDataModels) {
                wishlistItemCount.setText(String.valueOf(wishListDataModels.size()));
            }
        });

        cartViewModel.getCartData().observe(this, new Observer<List<CartDataModel>>() {
            @Override
            public void onChanged(List<CartDataModel> cartDataModels) {
                cartItemCount.setText(String.valueOf(cartDataModels.size()));
            }
        });

        currentUserData.observe(this, new Observer<LoginDataModel>() {
            @Override
            public void onChanged(LoginDataModel loginDataModel) {
                headerText.setText(loginDataModel.getCurrentUser().getUsername());
                headerSubtitle.setText(loginDataModel.getCurrentUser().getPhone1());
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(Color.parseColor("#52341E"))
                        .endConfig()
                        .buildRound(headerText.getText().toString().toUpperCase().substring(0, 1), Color.parseColor("#FFFFFF"));
                headerImage.setImageDrawable(drawable);

            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        wishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WishlistActivity.class);

                if (navController.getCurrentDestination().getId() == R.id.navigation_dashboard) {
                    intent.putExtra("fromDashboard", "y");
                }

                startActivity(intent);
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                if (navController.getCurrentDestination().getId() == R.id.navigation_dashboard) {
                    intent.putExtra("fromDashboard", "y");
                }

                startActivity(intent);

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int currentDestinationId = navController.getCurrentDestination().getId();
                int destinationId = item.getItemId();


                if (currentDestinationId == R.id.navigation_dashboard) {
                    if (destinationId == R.id.navigation_order) {
                        navController.navigate(R.id.action_navigation_dashboard_to_navigation_order);

                    } else if (destinationId == R.id.navigation_logout) {
                        logoutAlertDialog("Logout","Are you sure?","Logout","Cancel",R.id.action_navigation_dashboard_to_navigation_logout);
                        
                    } else if (destinationId == R.id.navigation_networks_and_contacts) {
                        navController.navigate(R.id.action_navigation_dashboard_to_navigation_networks_and_contacts);

                    } else if (destinationId == R.id.navigation_profile) {
                        navController.navigate(R.id.action_navigation_dashboard_to_navigation_profile);

                    } else if (destinationId == R.id.navigation_request_book) {
                        navController.navigate(R.id.action_navigation_dashboard_to_navigation_request_book);

                    }

                } else if (currentDestinationId == R.id.navigation_order) {
                    if (destinationId == R.id.navigation_dashboard) {
                        navController.navigate(R.id.action_navigation_order_to_navigation_dashboard);

                    } else if (destinationId == R.id.navigation_logout) {
                        logoutAlertDialog("Logout","Are you sure?","Logout","Cancel",R.id.action_navigation_order_to_navigation_logout);

                    } else if (destinationId == R.id.navigation_networks_and_contacts) {
                        navController.navigate(R.id.action_navigation_order_to_navigation_networks_and_contacts);

                    } else if (destinationId == R.id.navigation_profile) {
                        navController.navigate(R.id.action_navigation_order_to_navigation_profile);

                    } else if (destinationId == R.id.navigation_request_book) {
                        navController.navigate(R.id.action_navigation_order_to_navigation_request_book);

                    }

                } else if (currentDestinationId == R.id.navigation_request_book) {
                    if (destinationId == R.id.navigation_dashboard) {
                        navController.navigate(R.id.action_navigation_request_book_to_navigation_dashboard);

                    } else if (destinationId == R.id.navigation_logout) {
                        logoutAlertDialog("Logout","Are you sure?","Logout","Cancel",R.id.action_navigation_request_book_to_navigation_logout);

                    } else if (destinationId == R.id.navigation_order) {
                        navController.navigate(R.id.action_navigation_request_book_to_navigation_order);

                    } else if (destinationId == R.id.navigation_profile) {
                        navController.navigate(R.id.action_navigation_request_book_to_navigation_profile);

                    } else if (destinationId == R.id.navigation_networks_and_contacts) {
                        navController.navigate(R.id.action_navigation_request_book_to_navigation_networks_and_contacts);

                    }

                } else if (currentDestinationId == R.id.navigation_networks_and_contacts) {
                    if (destinationId == R.id.navigation_dashboard) {
                        navController.navigate(R.id.action_navigation_networks_and_contacts_to_navigation_dashboard);

                    } else if (destinationId == R.id.navigation_logout) {
                        logoutAlertDialog("Logout","Are you sure?","Logout","Cancel",R.id.action_navigation_networks_and_contacts_to_navigation_logout);

                    } else if (destinationId == R.id.navigation_order) {
                        navController.navigate(R.id.action_navigation_networks_and_contacts_to_navigation_order);

                    } else if (destinationId == R.id.navigation_profile) {
                        navController.navigate(R.id.action_navigation_networks_and_contacts_to_navigation_profile);

                    } else if (destinationId == R.id.navigation_request_book) {
                        navController.navigate(R.id.action_navigation_networks_and_contacts_to_navigation_request_book);

                    }

                } else if (currentDestinationId == R.id.navigation_profile) {
                    if (destinationId == R.id.navigation_dashboard) {
                        navController.navigate(R.id.action_navigation_profile_to_navigation_dashboard);

                    } else if (destinationId == R.id.navigation_logout) {
                        logoutAlertDialog("Logout","Are you sure?","Logout","Cancel",R.id.action_navigation_profile_to_navigation_logout);


                    } else if (destinationId == R.id.navigation_order) {
                        navController.navigate(R.id.action_navigation_profile_to_navigation_order);

                    } else if (destinationId == R.id.navigation_networks_and_contacts) {
                        navController.navigate(R.id.action_navigation_profile_to_navigation_networks_and_contacts);

                    } else if (destinationId == R.id.navigation_request_book) {
                        navController.navigate(R.id.action_navigation_profile_to_navigation_request_book);

                    }

                } else if (currentDestinationId == R.id.navigation_all_categories) {
                    if (destinationId == R.id.navigation_dashboard) {
                        navController.navigate(R.id.action_navigation_all_categories_to_navigation_dashboard);

                    } else if (destinationId == R.id.navigation_logout) {
                        logoutAlertDialog("Logout","Are you sure?","Logout","Cancel",R.id.action_navigation_all_categories_to_navigation_logout);


                    } else if (destinationId == R.id.navigation_order) {
                        navController.navigate(R.id.action_navigation_all_categories_to_navigation_order);

                    } else if (destinationId == R.id.navigation_networks_and_contacts) {
                        navController.navigate(R.id.action_navigation_all_categories_to_navigation_networks_and_contacts);

                    } else if (destinationId == R.id.navigation_request_book) {
                        navController.navigate(R.id.action_navigation_all_categories_to_navigation_request_book);

                    } else if (destinationId == R.id.navigation_profile) {
                        navController.navigate(R.id.action_navigation_all_categories_to_navigation_profile);

                    }

                } else if (currentDestinationId == R.id.navigation_book_category) {
                    if (destinationId == R.id.navigation_dashboard) {
                        navController.navigate(R.id.action_navigation_book_category_to_navigation_dashboard);

                    } else if (destinationId == R.id.navigation_logout) {
                        logoutAlertDialog("Logout","Are you sure?","Logout","Cancel",R.id.action_navigation_book_category_to_navigation_logout);

                    } else if (destinationId == R.id.navigation_order) {
                        navController.navigate(R.id.action_navigation_book_category_to_navigation_order);

                    } else if (destinationId == R.id.navigation_networks_and_contacts) {
                        navController.navigate(R.id.action_navigation_book_category_to_navigation_networks_and_contacts);

                    } else if (destinationId == R.id.navigation_request_book) {
                        navController.navigate(R.id.action_navigation_book_category_to_navigation_request_book);

                    } else if (destinationId == R.id.navigation_profile) {
                        navController.navigate(R.id.action_navigation_book_category_to_navigation_profile);

                    }

                } else if (currentDestinationId == R.id.navigation_book_details) {
                    if (destinationId == R.id.navigation_dashboard) {
                        navController.navigate(R.id.action_navigation_book_details_to_navigation_dashboard);

                    } else if (destinationId == R.id.navigation_logout) {
                        logoutAlertDialog("Logout","Are you sure?","Logout","Cancel",R.id.action_navigation_book_details_to_navigation_logout);

                    } else if (destinationId == R.id.navigation_order) {
                        navController.navigate(R.id.action_navigation_book_details_to_navigation_order);

                    } else if (destinationId == R.id.navigation_networks_and_contacts) {
                        navController.navigate(R.id.action_navigation_book_details_to_navigation_networks_and_contacts);

                    } else if (destinationId == R.id.navigation_request_book) {
                        navController.navigate(R.id.action_navigation_book_details_to_navigation_request_book);

                    } else if (destinationId == R.id.navigation_profile) {
                        navController.navigate(R.id.action_navigation_book_details_to_navigation_profile);

                    }

                } else if (currentDestinationId == R.id.navigation_all_publishers) {
                    if (destinationId == R.id.navigation_dashboard) {
                        navController.navigate(R.id.action_navigation_all_publishers_to_navigation_dashboard);

                    } else if (destinationId == R.id.navigation_logout) {
                        logoutAlertDialog("Logout","Are you sure?","Logout","Cancel",R.id.action_navigation_all_publishers_to_navigation_logout);

                    } else if (destinationId == R.id.navigation_order) {
                        navController.navigate(R.id.action_navigation_all_publishers_to_navigation_order);

                    } else if (destinationId == R.id.navigation_networks_and_contacts) {
                        navController.navigate(R.id.action_navigation_all_publishers_to_navigation_networks_and_contacts);

                    } else if (destinationId == R.id.navigation_request_book) {
                        navController.navigate(R.id.action_navigation_all_publishers_to_navigation_request_book);

                    } else if (destinationId == R.id.navigation_profile) {
                        navController.navigate(R.id.action_navigation_all_publishers_to_navigation_profile);

                    }

                }
                drawerLayout.close();

                return true;
            }
        });
    }

    private void logoutAlertDialog(String title, String message, String submit, String cancel, int id){
        AlertDialog.Builder builder=new AlertDialog.Builder(this,R.style.CustomAlertDialog);

        View view = LayoutInflater.from(this).inflate(R.layout.alert_custom_title,null);
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
                
                Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment_activity_main).navigate(id);
                alertDialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               
                alertDialog.dismiss();

            }
        });
        


    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isOpen()) {
            drawerLayout.close();
        } else {
            super.onBackPressed();
        }
    }
}