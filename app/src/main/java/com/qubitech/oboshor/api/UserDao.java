package com.qubitech.oboshor.api;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.qubitech.oboshor.datamodels.CartDataModel;
import com.qubitech.oboshor.datamodels.WishListDataModel;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Insert(onConflict = REPLACE)
    void insertCartItem(CartDataModel cartData);


    @Query("SELECT * FROM cart")
    LiveData<List<CartDataModel>> getAllCartData();

    @Query("Update cart set quantity=:quantity where bookId=:bookId")
    void updateCart(String bookId, int quantity);

    @Query("Update cart set price=:price where bookId=:bookId")
    void updateCartPrice(String bookId, int price);

    @Query("DELETE from cart where bookId=:bookId")
    void deleteCartItem(String bookId);

    @Query("Delete from cart where 1")
    void clearCart();


    @Insert(onConflict = REPLACE)
    void insertWishlistItem(WishListDataModel wishListData);


    @Query("SELECT * FROM wishlist")
    LiveData<List<WishListDataModel>> getWishlistData();

    @Query("Update wishlist set price=:price where bookId=:bookId")
    void updateWishlistPrice(String bookId, int price);

    @Query("DELETE from wishlist where bookId=:bookId")
    void deleteWishlistItem(String bookId);

}
