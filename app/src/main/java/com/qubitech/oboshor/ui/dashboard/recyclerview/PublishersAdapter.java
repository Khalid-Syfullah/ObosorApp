package com.qubitech.oboshor.ui.dashboard.recyclerview;

import static com.qubitech.oboshor.api.RetrofitClient.BASE_URL;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
import com.qubitech.oboshor.datamodels.PublisherDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class PublishersViewHolder extends RecyclerView.ViewHolder{

    TextView publisherTitle;
    ImageView publisherImage;
    ConstraintLayout publisherConstraintLayout;

    public PublishersViewHolder(@NonNull View itemView) {
        super(itemView);

        publisherTitle = itemView.findViewById(R.id.recyclerview_categories_title);
        publisherImage = itemView.findViewById(R.id.recyclerview_categories_image);
        publisherConstraintLayout = itemView.findViewById(R.id.recyclerview_categories_constraint_layout);

    }

}

public class PublishersAdapter extends RecyclerView.Adapter<PublishersViewHolder>{
    
    ArrayList<PublisherDataModel> publisherDataModels;
    Activity activity;
    int type;

    public PublishersAdapter(Activity activity, ArrayList<PublisherDataModel> publisherDataModels) {
        
        this.activity = activity;
        this.publisherDataModels = publisherDataModels;
        this.type = type;
        
    }

    @NonNull
    @Override
    public PublishersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.recyclerview_publishers,parent,false);
        PublishersViewHolder publishersViewHolder = new PublishersViewHolder(view);
        return publishersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PublishersViewHolder holder, int position) {

        PublisherDataModel publisherDataModel = publisherDataModels.get(position);


        Picasso.get().load(BASE_URL + "/"+publisherDataModel.getPublisherImg()).into(holder.publisherImage);
        holder.publisherTitle.setText(publisherDataModel.getPublisherName());
        holder.publisherTitle.setSelected(true);

        holder.publisherConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("category", "all-publishers");
                bundle.putString("publisher-name", publisherDataModel.getPublisherName());
                Navigation.findNavController(activity, R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_dashboard_to_navigation_book_category, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(publisherDataModels.size() <= 4) {
            return publisherDataModels.size();
        }
        else{
            return 4;

        }
    }

    public void setPublisherDataModels(ArrayList<PublisherDataModel> publisherDataModels){
        this.publisherDataModels = publisherDataModels;
        notifyDataSetChanged();
    }
}