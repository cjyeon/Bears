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

    @Insert
    void insert(BookmarkEntity bookmarkEntities);

    @Query("SELECT COUNT(*) FROM bookmarkentity WHERE busNum = :busNum AND stationId = :stationId")
    Integer getCountBookmark(String busNum, String stationId);

    @Query("DELETE FROM bookmarkentity WHERE busNum = :busNum AND stationId = :stationId")
    void deleteById(String busNum, String stationId);

    @Delete
    void delete(BookmarkEntity bookmarkEntity);
}
