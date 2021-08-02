package com.example.bears.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookmarkDao {
    @Query("SELECT * FROM bookmarkentity")
    LiveData<List<BookmarkEntity>> getAll();

//    @Query("SELECT * FROM music WHERE id IN (:musicIds)")
//    List<BookmarkEntity> loadAllByIds(int[] musicIds);

    @Insert
    void insert(BookmarkEntity bookmarkEntities);

    @Delete
    void delete(BookmarkEntity bookmarkEntity);
}
