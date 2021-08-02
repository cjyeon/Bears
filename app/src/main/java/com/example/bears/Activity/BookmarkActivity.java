package com.example.bears.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.bears.BookmarkAdapter;
import com.example.bears.BookmarkData;
import com.example.bears.R;
import com.example.bears.RecyclerDecoration;
import com.example.bears.Room.BookmarkDB;
import com.example.bears.Room.BookmarkDao;
import com.example.bears.Room.BookmarkEntity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Tag;

public class BookmarkActivity extends AppCompatActivity {
    ImageView iv_backbtn;
    RecyclerView rv_bookmark;
    List<BookmarkEntity> bookmarkData;
    BookmarkAdapter bookmarkAdapter;
    LinearLayoutManager linearLayoutManager;

    private BookmarkDao bookmarkDao;
    private BookmarkDB bookmarkDB;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        iv_backbtn = findViewById(R.id.iv_backbtn);
        rv_bookmark = findViewById(R.id.rv_bookmark);

        RecyclerDecoration decoration_height = new RecyclerDecoration(20);
        rv_bookmark.addItemDecoration(decoration_height);

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
        bookmarkAdapter = new BookmarkAdapter(bookmarkDB);
        rv_bookmark.setAdapter(bookmarkAdapter);

        //UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨)
        bookmarkDB.bookmarkDao().getAll().observe(this, new Observer<List<BookmarkEntity>>() {
            @Override
            public void onChanged(List<BookmarkEntity> data) {
                bookmarkAdapter.setItem(data);
            }
        });

//        BookmarkDB bookmarkDB = BookmarkDB.getInstance(this);
//        bookmarkDao = bookmarkDB.bookmarkDao();

        // 메인 스레드에서 DB 접근 불가 -> 읽고 쓸 때 스레드 사용
//        class SelectRunnable implements Runnable {
//            @Override
//            public void run() {
//                List<BookmarkEntity> bookmarkEntities = bookmarkDao.getAll();
//
//                for (i = 0; i < bookmarkEntities.size(); i++) {
//                    Log.d("onCreate: getAll() : " , bookmarkEntities.get(i).getBusNum());
//                }
//            }
//        }
//        SelectRunnable selectRunnable = new SelectRunnable();
//        Thread t = new Thread(selectRunnable);
//        t.start();

        //test
//        bookmarkData.add(new BookmarkData("88", "11분 30후 도착 예정", "3" + "정거장 전", true));
//        bookmarkData.add(new BookmarkData("81-1", "15분 후 도착 예정", "4" + "정거장 전", true));

    }
}