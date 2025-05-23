package com.qubitech.oboshor.ui.dashboard.recyclerview;

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

import com.qubitech.oboshor.BuildConfig;
import com.qubitech.oboshor.R;
import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.datamodels.CategoryDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class CategoriesViewHolder extends RecyclerView.ViewHolder{

    TextView categoryTitle;
    ImageView categoryImage;
    ConstraintLayout categoryConstraintLayout;

    public CategoriesViewHolder(@NonNull View itemView) {
        super(itemView);

        categoryTitle = itemView.findViewById(R.id.recyclerview_categories_title);
        categoryImage = itemView.findViewById(R.id.recyclerview_categories_image);
        categoryConstraintLayout = itemView.findViewById(R.id.recyclerview_categories_constraint_layout);

    }

}

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesViewHolder>{
    
    ArrayList<CategoryDataModel> categoryDataModels;
    Activity activity;
    int type;

    public CategoriesAdapter(Activity activity, ArrayList<CategoryDataModel> categoryDataModels, int type) {
        
        this.activity = activity;
        this.categoryDataModels = categoryDataModels;
        this.type = type;
        
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if(type == 1){
            view= LayoutInflater.from(activity).inflate(R.layout.recyclerview_categories, parent, false);
        }
        else {
            view = LayoutInflater.from(activity).inflate(R.layout.recyclerview_publishers,parent,false);
        }

        CategoriesViewHolder categoriesViewHolder = new CategoriesViewHolder(view);
        return categoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {

        CategoryDataModel categoryDataModel = categoryDataModels.get(position);

        holder.categoryTitle.setText(categoryDataModel.getCategoryName());
        holder.categoryTitle.setSelected(true);

        String name = "category_"+(position+1);

        holder.categoryImage.setImageResource(R.drawable.ic_book);

        holder.categoryConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("category", "all-categories");
                bundle.putString("category-name", categoryDataModel.getCategoryName());
                Navigation.findNavController(activity, R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_dashboard_to_navigation_book_category, bundle);

            }
        });

    }

    @Override
    public int getItemCount() {

        if(categoryDataModels.size() <= 8) {
            return categoryDataModels.size();
        }
        else{
            return 8;

        }
    }

    public void setCategoryDataModels(ArrayList<CategoryDataModel> categoryDataModels){

        this.categoryDataModels = categoryDataModels;
        notifyDataSetChanged();
    }
}