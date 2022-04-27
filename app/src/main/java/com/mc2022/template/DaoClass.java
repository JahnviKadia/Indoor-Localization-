package com.mc2022.template;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoClass {
    @Insert
    void insert(WifiModel myModel);

    @Query("select * from wifi")
    List<WifiModel> getAllData();
}
