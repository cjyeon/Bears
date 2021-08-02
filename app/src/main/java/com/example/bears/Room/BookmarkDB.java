package com.example.bears.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BookmarkEntity.class}, version = 1)
public abstract class BookmarkDB extends RoomDatabase {

    private static BookmarkDB INSTANCE = null;

    public abstract BookmarkDao bookmarkDao();


    public static BookmarkDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    BookmarkDB.class, "bookmark.db")
                    .build();

        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
