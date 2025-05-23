package com.qubitech.oboshor.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.qubitech.oboshor.api.UserDao;
import com.qubitech.oboshor.api.RoomDB;
import com.qubitech.oboshor.datamodels.CartDataModel;
import com.qubitech.oboshor.datamodels.WishListDataModel;

import java.util.List;

public class LocalRepo {
    private UserDao userDao;
    private LiveData<List<CartDataModel>> cartData;
    private LiveData<List<WishListDataModel>> wishlistData;

    public LocalRepo(Application application) {
        RoomDB database = RoomDB.getInstance(application);
        userDao = database.userDao();
        cartData = userDao.getAllCartData();
        wishlistData = userDao.getWishlistData();
    }

    public void insertCartItem(CartDataModel cartDataModel) {
        userDao.insertCartItem(cartDataModel);
    }

    public LiveData<List<CartDataModel>> getAllCartData() {

        return cartData;
    }

    public void updateCartQuantity(CartDataModel cartDataModel) {
        userDao.updateCart(cartDataModel.getBookId(), cartDataModel.getQuantity());
    }

    public void updateCartPrice(String bookId, int price) {
        userDao.updateCartPrice(bookId, price);
    }

    public void deleteCartItem(String bookId) {
        userDao.deleteCartItem(bookId);
    }

    public void insertWishlistItem(WishListDataModel wishListDataModel) {
        userDao.insertWishlistItem(wishListDataModel);
    }

    public LiveData<List<WishListDataModel>> getWishlistData() {

        return wishlistData;
    }

    public void updateWishlistPrice(String bookId, int price) {
        userDao.updateWishlistPrice(bookId, price);
    }

    public void deleteWishlistItem(String bookId) {
        userDao.deleteWishlistItem(bookId);
    }


}
