package com.qubitech.oboshor.ui.book.recyclerview;

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
import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.datamodels.CategoryDataModel;
import com.qubitech.oboshor.datamodels.OrderDataModel;

import java.util.ArrayList;

class AllCategoriesViewHolder extends RecyclerView.ViewHolder{

    TextView allCategoriesTitle;
    ImageView allCategoriesImage;
    ConstraintLayout allCategoriesConstraintLayout;

    public AllCategoriesViewHolder(@NonNull View itemView) {
        super(itemView);

        allCategoriesTitle = itemView.findViewById(R.id.recyclerview_all_categories_title);
        allCategoriesImage = itemView.findViewById(R.id.recyclerview_all_categories_icon);
        allCategoriesConstraintLayout = itemView.findViewById(R.id.recyclerview_all_categories_constraint_layout);

    }

}

public class AllCategoriesAdapter extends RecyclerView.Adapter<AllCategoriesViewHolder>{
    
    ArrayList<CategoryDataModel> categoryDataModels;
    Activity activity;
    int pos;

    public AllCategoriesAdapter(Activity activity, ArrayList<CategoryDataModel> categoryDataModels, int pos) {
        
        this.activity = activity;
        this.categoryDataModels = categoryDataModels;
        this.pos = pos;

    }

    @NonNull
    @Override
    public AllCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(activity).inflate(R.layout.recyclerview_all_categories, parent, false);

        AllCategoriesViewHolder allCategoriesViewHolder = new AllCategoriesViewHolder(view);
        return allCategoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllCategoriesViewHolder holder, int position) {

        CategoryDataModel categoryDataModel = categoryDataModels.get(position);

        holder.allCategoriesTitle.setText(categoryDataModel.getCategoryName());

        holder.allCategoriesConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pos == 1){

                    if(categoryDataModel.getSubcategoryName().length != 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString("title", categoryDataModel.getCategoryName());
                        bundle.putStringArray("subcategory", categoryDataModel.getSubcategoryName());
                        Navigation.findNavController(activity, R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_all_categories_self, bundle);
                    }
                    else{
                        Bundle bundle = new Bundle();
                        bundle.putString("category", "all-categories");
                        bundle.putString("category-name", categoryDataModel.getCategoryName());
                        Navigation.findNavController(activity, R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_all_categories_to_navigation_book_category, bundle);

                    }

                }
                else if(pos == 2){

                    Bundle bundle = new Bundle();
                    bundle.putString("category", "all-categories");
                    bundle.putString("category-name", categoryDataModel.getCurrentCategoryName());
                    bundle.putString("subcategory-name", categoryDataModel.getCategoryName());
                    Navigation.findNavController(activity, R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_all_categories_to_navigation_book_category, bundle);


                }
            }
        });

        holder.allCategoriesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pos == 1){

                    Bundle bundle = new Bundle();
                    bundle.putString("category", "all-categories");
                    bundle.putString("category-name", categoryDataModel.getCategoryName());
                    Navigation.findNavController(activity, R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_all_categories_to_navigation_book_category, bundle);

                }
                else if(pos == 2){
                    Bundle bundle = new Bundle();
                    bundle.putString("category", "all-categories");
                    bundle.putString("category-name", categoryDataModel.getCurrentCategoryName());
                    bundle.putString("subcategory-name", categoryDataModel.getCategoryName());
                    Navigation.findNavController(activity, R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_all_categories_to_navigation_book_category, bundle);

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return categoryDataModels.size();
    }

    public void setCategoryDataModels(ArrayList<CategoryDataModel> categoryDataModels) {
        this.categoryDataModels = categoryDataModels;
        notifyDataSetChanged();
    }
}