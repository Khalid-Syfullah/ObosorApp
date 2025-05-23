package com.qubitech.oboshor.ui.cart.recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qubitech.oboshor.R;

import org.jetbrains.annotations.NotNull;

public class CartViewHolder extends RecyclerView.ViewHolder {
    TextView quantity,title,author,category,price;
    ImageView addBtn,removeBtn,bookImage;

    public CartViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        quantity = itemView.findViewById(R.id.recyclerview_cart_item_quantity);
        addBtn = itemView.findViewById(R.id.recyclerview_cart_add);
        removeBtn = itemView.findViewById(R.id.recyclerview_cart_remove);
        title = itemView.findViewById(R.id.recyclerview_cart_title);
        author = itemView.findViewById(R.id.recyclerview_cart_author);
        category = itemView.findViewById(R.id.recyclerview_cart_category);
        price=itemView.findViewById(R.id.recyclerview_cart_amount);
        bookImage=itemView.findViewById(R.id.recyclerview_cart_image);
    }
}
