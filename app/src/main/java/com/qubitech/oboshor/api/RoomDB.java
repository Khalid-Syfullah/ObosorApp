package com.qubitech.oboshor.api;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.qubitech.oboshor.datamodels.CartDataModel;
import com.qubitech.oboshor.datamodels.WishListDataModel;


@Database(entities = {CartDataModel.class, WishListDataModel.class}, version = 4, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database;

    private static String DATABASE_NAME = "cart_data";

    /**
     * @param context
     * @return RoomDB
     */
    public synchronized static RoomDB getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, DATABASE_NAME
            ).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return database;
    }


    public abstract UserDao userDao();

}
