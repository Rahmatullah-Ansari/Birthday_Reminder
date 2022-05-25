package com.android.bdyr.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = { Entities.class },version = 2,exportSchema = true)
public abstract class DatabaseManager extends RoomDatabase {
    public static final String TABLE="EVENTS";
    public abstract DAO dao();
    private static DatabaseManager INSTANCE;
    public synchronized static DatabaseManager getINSTANCE(Context context){
        if (INSTANCE == null){
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),DatabaseManager.class,TABLE)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
