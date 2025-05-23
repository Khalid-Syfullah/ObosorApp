package com.qubitech.oboshor.ui.wishlist.recycler;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qubitech.oboshor.R;

import org.jetbrains.annotations.NotNull;

public class WishlistViewHolder extends RecyclerView.ViewHolder {
        TextView title,author,category,price;
        ImageView bookImage,removeBtn;
        Button addToCart;
    public WishlistViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
            title = itemView.findViewById(R.id.recyclerview_wishlist_title);
            author = itemView.findViewById(R.id.recyclerview_wishlist_author);
            category = itemView.findViewById(R.id.recyclerview_wishlist_category);
            price=itemView.findViewById(R.id.recyclerview_wishlist_amount);
            bookImage=itemView.findViewById(R.id.recyclerview_wishlist_image);
            addToCart=itemView.findViewById(R.id.recyclerview_wishlist_add_to_cart_btn);
            removeBtn=itemView.findViewById(R.id.recyclerview_wishlist_remove_btn);
    }
}
