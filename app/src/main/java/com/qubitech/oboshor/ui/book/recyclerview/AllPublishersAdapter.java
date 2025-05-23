package com.qubitech.oboshor.ui.book.recyclerview;

import static com.qubitech.oboshor.api.RetrofitClient.BASE_URL;

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
import com.qubitech.oboshor.datamodels.PublisherDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class AllPublishersViewHolder extends RecyclerView.ViewHolder{

    TextView allPublishersName;
    ImageView allPublishersImage;
    ConstraintLayout allPublishersConstraintLayout;

    public AllPublishersViewHolder(@NonNull View itemView) {
        super(itemView);

        allPublishersName = itemView.findViewById(R.id.recyclerview_all_publishers_title);
        allPublishersImage = itemView.findViewById(R.id.recyclerview_all_publishers_image);
        allPublishersConstraintLayout = itemView.findViewById(R.id.recyclerview_all_publishers_constraint_layout);

    }

}

public class AllPublishersAdapter extends RecyclerView.Adapter<AllPublishersViewHolder> {

    ArrayList<PublisherDataModel> publisherDataModels;
    Activity activity;

    public AllPublishersAdapter(Activity activity, ArrayList<PublisherDataModel> publisherDataModels) {

        this.activity = activity;
        this.publisherDataModels = publisherDataModels;

    }

    @NonNull
    @Override
    public AllPublishersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.recyclerview_all_publishers, parent, false);

        AllPublishersViewHolder allPublishersViewHolder = new AllPublishersViewHolder(view);
        return allPublishersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllPublishersViewHolder holder, int position) {

        PublisherDataModel publisherDataModel = publisherDataModels.get(position);

        holder.allPublishersName.setText(publisherDataModel.getPublisherName());
        Picasso.get().load(BASE_URL + "/" + publisherDataModel.getPublisherImg()).into(holder.allPublishersImage);
        holder.allPublishersConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("category", "all-publishers");
                bundle.putString("publisher-name", publisherDataModel.getPublisherName());
                Navigation.findNavController(activity, R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_all_publishers_to_navigation_book_category, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return publisherDataModels.size();
    }

    public void setPublisherDataModels(ArrayList<PublisherDataModel> publisherDataModels) {
        this.publisherDataModels = publisherDataModels;
        notifyDataSetChanged();
    }
}