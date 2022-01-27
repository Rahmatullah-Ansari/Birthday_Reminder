package com.android.bdyr.Database;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DAO {
    @Query (" SELECT * FROM EVENTS")
    List<Entities> getAllData();
    @Insert
    void insertData(Entities entities);
    @Delete
    void deleteAllData(Entities entities);
    @Delete
    void delete(ArrayList<Entities> entities);
    @Query("UPDATE EVENTS SET Name = :bName ,Date =:bDate,Number =:bNumber ,Category =:bCat,Wish_Text =:bWish")
    void update(String bName,String bDate,String bNumber,String bCat,String bWish);
    @Query("SELECT * FROM EVENTS WHERE id = :sId")
    int getId(int sId);
}
