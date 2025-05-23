package com.qubitech.oboshor.ui.order.recyclerview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.datamodels.OrderDataModel;
import com.qubitech.oboshor.ui.order.OrderDetailsActivity;

import java.io.Serializable;
import java.util.ArrayList;

class OrderViewHolder extends RecyclerView.ViewHolder{

    TextView id, date, status, books, address, phone;
    Button trackBtn;
    ConstraintLayout constraintLayout;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        id = itemView.findViewById(R.id.recyclerview_order_id);
        status = itemView.findViewById(R.id.recyclerview_order_status);
        date = itemView.findViewById(R.id.recyclerview_order_date);
        books = itemView.findViewById(R.id.recyclerview_order_books);
        address = itemView.findViewById(R.id.recyclerview_order_address);
        phone = itemView.findViewById(R.id.recyclerview_order_phone);
        trackBtn = itemView.findViewById(R.id.recyclerview_order_track_btn);
        constraintLayout = itemView.findViewById(R.id.recyclerview_order_constraint_layout);

    }

}

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder>{
    
    ArrayList<OrderDataModel> orderDataModels;
    Activity activity;

    public OrderAdapter(Activity activity, ArrayList<OrderDataModel> orderDataModels) {
        
        this.activity = activity;
        this.orderDataModels = orderDataModels;

    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        view= LayoutInflater.from(activity).inflate(R.layout.recyclerview_order, parent, false);

        OrderViewHolder orderViewHolder = new OrderViewHolder(view);
        return orderViewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        OrderDataModel orderDataModel = orderDataModels.get(position);

        String[] parts = orderDataModel.getDate().split("T");
        orderDataModel.setDate(parts[0]);

        int totalBooks = 0;

        for(int i=0;i<orderDataModel.getOrder().size();i++){

            totalBooks+= orderDataModel.getOrder().get(i).getQuantity();
        }

        holder.id.setText(orderDataModel.getId());
        holder.status.setText(orderDataModel.getStatus());
        holder.date.setText(orderDataModel.getDate());
        holder.books.setText(String.valueOf(totalBooks));
        holder.address.setText(orderDataModel.getAddress());
        holder.phone.setText(orderDataModel.getPhone());



        if(orderDataModel.getStatus().equals("Placed")) {
            holder.status.setTextColor(activity.getResources().getColor(R.color.blue));
        }
        else if(orderDataModel.getStatus().equals("Canceled")) {
            holder.status.setTextColor(activity.getResources().getColor(R.color.red));
        }
        else if(orderDataModel.getStatus().equals("Pending")) {
            holder.status.setTextColor(activity.getResources().getColor(R.color.orange));
        }
        else if(orderDataModel.getStatus().equals("Confirm")) {
            holder.status.setTextColor(activity.getResources().getColor(R.color.teal));
        }
        else if(orderDataModel.getStatus().equals("Processing")) {
            holder.status.setTextColor(activity.getResources().getColor(R.color.yellow));
        }
        else if(orderDataModel.getStatus().equals("Shipped")) {
            holder.status.setTextColor(activity.getResources().getColor(R.color.orange));
        }
        else if(orderDataModel.getStatus().equals("Delivered")) {
            holder.status.setTextColor(activity.getResources().getColor(R.color.green));
        }

        holder.trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Bundle bundle = new Bundle();
                Intent intent = new Intent(activity, OrderDetailsActivity.class);
                intent.putExtra("order-status",orderDataModel.getStatus());

                Bundle bundle = new Bundle();
                bundle.putSerializable("data-model",orderDataModel);
                intent.putExtra("bundle",bundle);
                activity.startActivity(intent);

//                Navigation.findNavController(activity, R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_order_to_navigation_trackOrder, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderDataModels.size();
    }

    public void setOrderDataModels(ArrayList<OrderDataModel> orderDataModels) {
        this.orderDataModels = orderDataModels;
        notifyDataSetChanged();
    }
}