package com.example.masluli.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MaslulDao {

    @Query("select * from Maslul")
    LiveData<List<Maslul>> getAll();

    @Query("select * from Maslul where userId = :userId")
    LiveData<List<Maslul>> getMyMaslulim(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Maslul... maslulim);

    @Delete
    void delete(Maslul maslul);

}
