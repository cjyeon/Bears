package com.example.bears.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bears.BookmarkAdapter;
import com.example.bears.R;
import com.example.bears.Room.BookmarkDB;
import com.example.bears.Room.BookmarkDao;
import com.example.bears.Room.BookmarkEntity;

import java.util.List;


public class BookmarkActivity extends AppCompatActivity {
    static ImageView iv_backbtn;
    RecyclerView rv_bookmark;
    BookmarkAdapter bookmarkAdapter;

    private BookmarkDB bookmarkDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        iv_backbtn = findViewById(R.id.iv_backbtn);
        rv_bookmark = findViewById(R.id.rv_bookmark);

        iv_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bookmarkDB = BookmarkDB.getInstance(this);
        rv_bookmark.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_bookmark.setLayoutManager(layoutManager);
        bookmarkAdapter = new BookmarkAdapter(bookmarkDB, this);
        rv_bookmark.setAdapter(bookmarkAdapter);

        //UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨)
        bookmarkDB.bookmarkDao().getAll().observe(this, new Observer<List<BookmarkEntity>>() {
            @Override
            public void onChanged(List<BookmarkEntity> data) {
                bookmarkAdapter.setItem(data);
            }
        });

    }
}