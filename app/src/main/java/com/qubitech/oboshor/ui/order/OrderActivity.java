package com.qubitech.oboshor.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.datamodels.OrderDataModel;
import com.qubitech.oboshor.ui.order.recyclerview.OrderAdapter;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    private OrderViewModel orderViewModel;
    private TextView appBarTitle;
    private ImageView backBtn;
    private RecyclerView orderRecycler;
    private ArrayList<OrderDataModel> orderDataModels;
    private OrderAdapter orderAdapter;
    private ConstraintLayout emptyOrderConstraintLayout;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order);


        orderRecycler = findViewById(R.id.order_recyclerview);
        progressBar = findViewById(R.id.order_progress_bar);
        appBarTitle = findViewById(R.id.fragment_title);
        backBtn = findViewById(R.id.backBtn);
        emptyOrderConstraintLayout = findViewById(R.id.empty_order_container);

        appBarTitle.setText("Orderlist");
        orderDataModels = new ArrayList<>();

        orderAdapter = new OrderAdapter(this,orderDataModels);
        orderRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        orderRecycler.setAdapter(orderAdapter);

        progressBar.setVisibility(View.VISIBLE);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        orderViewModel.getOrdersForUser().observe(this, new Observer<ArrayList<OrderDataModel>>() {
            @Override
            public void onChanged(ArrayList<OrderDataModel> orderDataModels2) {

                orderAdapter.setOrderDataModels(orderDataModels2);
                progressBar.setVisibility(View.GONE);

                if(orderDataModels2.size()==0){
                    emptyOrderConstraintLayout.setVisibility(View.VISIBLE);
                    orderRecycler.setVisibility(View.GONE);
                }
                else {
                    emptyOrderConstraintLayout.setVisibility(View.GONE);
                    orderRecycler.setVisibility(View.VISIBLE);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderActivity.super.onBackPressed();
            }
        });

    }

}