package com.qubitech.oboshor.ui.networking;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.datamodels.CategoryDataModel;
import com.qubitech.oboshor.datamodels.HubDataModel;
import com.qubitech.oboshor.datamodels.UserDataModel;

import java.util.ArrayList;

class HubsViewHolder extends RecyclerView.ViewHolder{

    TextView name, address;
    ConstraintLayout constraintLayout;

    public HubsViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.recyclerview_hubs_name);
        address = itemView.findViewById(R.id.recyclerview_hubs_address);
        constraintLayout = itemView.findViewById(R.id.recyclerview_hubs_constraint_layout);

    }

}

public class HubsAdapter extends RecyclerView.Adapter<HubsViewHolder>{
    
    ArrayList<HubDataModel> hubDataModels;
    Activity activity;

    public HubsAdapter(Activity activity, ArrayList<HubDataModel> hubDataModels) {
        
        this.activity = activity;
        this.hubDataModels = hubDataModels;

    }

    @NonNull
    @Override
    public HubsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(activity).inflate(R.layout.recyclerview_hubs, parent, false);
        HubsViewHolder categoriesViewHolder = new HubsViewHolder(view);
        return categoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HubsViewHolder holder, int position) {

        HubDataModel hubDataModel = hubDataModels.get(position);

        holder.name.setText(hubDataModel.getName());
        holder.address.setText(hubDataModel.getAddress());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(hubDataModel.getIntent());
            }
        });


    }

    @Override
    public int getItemCount() {

        return hubDataModels.size();

    }

    public void setHubDataModels(ArrayList<HubDataModel> hubDataModels) {
        this.hubDataModels = hubDataModels;
        notifyDataSetChanged();
    }
}