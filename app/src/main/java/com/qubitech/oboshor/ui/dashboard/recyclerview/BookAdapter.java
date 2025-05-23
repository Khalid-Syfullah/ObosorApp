package com.qubitech.oboshor.ui.dashboard.recyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.datamodels.BookDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.qubitech.oboshor.api.RetrofitClient.BASE_URL;

class BookViewHolder extends RecyclerView.ViewHolder{

    TextView bookTitle, bookCategory, bookAuthor, bookAmount, bookQuantity;
    ImageView bookImage;
    ConstraintLayout bookConstraintLayout;

    public BookViewHolder(@NonNull View itemView) {
        super(itemView);

        bookTitle = itemView.findViewById(R.id.recyclerview_book_body);
        bookCategory = itemView.findViewById(R.id.recyclerview_book_title);
        bookAuthor = itemView.findViewById(R.id.recyclerview_book_time);
        bookAmount = itemView.findViewById(R.id.recyclerview_book_amount);
        bookImage = itemView.findViewById(R.id.recyclerview_book_image);
        bookQuantity = itemView.findViewById(R.id.recyclerview_book_quantity);
        bookConstraintLayout = itemView.findViewById(R.id.recyclerview_book_card_view);

    }

}

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder>{
    
    private ArrayList<BookDataModel> bookDataModels;
    private Activity activity;
    private int type;

    public BookAdapter(Activity activity, ArrayList<BookDataModel> booksDataModels, int type) {
        
        this.activity = activity;
        this.bookDataModels = booksDataModels;
        this.type = type;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view ;
        if(type == 1){
            view= LayoutInflater.from(activity).inflate(R.layout.recyclerview_book_horizontal, parent, false);
        }
        else {
            view = LayoutInflater.from(activity).inflate(R.layout.recyclerview_book,parent,false);
        }

        BookViewHolder bookViewHolder = new BookViewHolder(view);
        return bookViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        BookDataModel bookDataModel = bookDataModels.get(position);

        holder.bookTitle.setText(bookDataModel.getTitle());
        holder.bookCategory.setText(bookDataModel.getCategory());
        holder.bookAuthor.setText(bookDataModel.getAuthor());
        holder.bookAmount.setText(String.valueOf(bookDataModel.getPrice()));
        Picasso.get().load(BASE_URL + "/" +bookDataModel.getImage()).into(holder.bookImage);
        holder.bookTitle.setSelected(true);
        holder.bookCategory.setSelected(true);
        holder.bookAuthor.setSelected(true);


        if(StaticData.CURRENT_FRAGMENT.equals("order-details")){
            holder.bookQuantity.setVisibility(View.VISIBLE);
            holder.bookQuantity.setText("Quantity: "+bookDataModel.getQuantity());
        }

        holder.bookConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("book_details",bookDataModel);



                if(StaticData.CURRENT_FRAGMENT.equals("dashboard")){

                    Navigation.findNavController(activity,R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_dashboard_to_navigation_book_details, bundle);

                }

                else if(StaticData.CURRENT_FRAGMENT.equals("book_category")){

                    Navigation.findNavController(activity,R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_book_category_to_navigation_book_details, bundle);
                }

                else{
                    Log.d("Fragment","Fragment: "+StaticData.CURRENT_FRAGMENT);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return bookDataModels.size();
    }


    public void setBookDataModels(ArrayList<BookDataModel> bookDataModels) {
        this.bookDataModels = bookDataModels;
        notifyDataSetChanged();
    }

}