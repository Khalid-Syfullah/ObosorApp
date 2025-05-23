package com.qubitech.oboshor.ui.wishlist.recycler;

import android.os.Bundle;

import com.qubitech.oboshor.datamodels.WishListDataModel;

public interface WishListOnclickListener {
    void onclickAddToCart(WishListDataModel wishListDataModel);
    void onclickRemove(String bookId);

}
