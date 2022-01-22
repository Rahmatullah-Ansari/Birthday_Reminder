package com.android.bdyr.Database;

import static com.android.bdyr.Database.DatabaseManager.TABLE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface DAO {
    //@Query (" SELECT * FROM "+ TABLE)
    //List<Entities> getAllData();

    @Insert
    void insertData(Entities...entities);
    @Delete
    void deleteData(Entities entities);
}
