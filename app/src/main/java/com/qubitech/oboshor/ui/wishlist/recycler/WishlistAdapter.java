package com.qubitech.oboshor.ui.wishlist.recycler;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.Repository.RemoteRepo;
import com.qubitech.oboshor.datamodels.WishListDataModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.qubitech.oboshor.api.RetrofitClient.BASE_URL;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistViewHolder> {

    List<WishListDataModel> wishListDataModels = new ArrayList<>();
    WishListOnclickListener wishListOnclickListener;
    RemoteRepo remoteRepo;

    public WishlistAdapter(Context ctx,WishListOnclickListener wishListOnclickListener ) {
        this.wishListOnclickListener = wishListOnclickListener;
        remoteRepo=new RemoteRepo((Application) ctx.getApplicationContext());

    }

    @NonNull
    @NotNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_wishlist, parent, false);
        WishlistViewHolder mvh = new WishlistViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WishlistViewHolder holder, int position) {
        holder.title.setText(wishListDataModels.get(position).getTitle());
        holder.author.setText(wishListDataModels.get(position).getAuthor());
        holder.price.setText(Integer.toString(wishListDataModels.get(position).getPrice()));
        holder.category.setText(wishListDataModels.get(position).getCategory());
        Picasso.get().load(BASE_URL + "/" + wishListDataModels.get(position).getImageUrl()).into(holder.bookImage);
        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishListOnclickListener.onclickAddToCart(wishListDataModels.get(position));
                wishListOnclickListener.onclickRemove(wishListDataModels.get(position).getBookId());

            }
        });

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishListOnclickListener.onclickRemove(wishListDataModels.get(position).getBookId());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return wishListDataModels.size();
    }

    public void setData(List<WishListDataModel> wishListDataModels) {
        this.wishListDataModels = wishListDataModels;
        notifyDataSetChanged();
    }
}