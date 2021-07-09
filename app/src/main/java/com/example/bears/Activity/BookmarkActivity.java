package com.example.bears.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bears.BookmarkAdapter;
import com.example.bears.BookmarkData;
import com.example.bears.R;
import com.example.bears.RecyclerDecoration;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {
    ImageView iv_backbtn;
    RecyclerView rv_bookmark;
    ArrayList<BookmarkData> bookmarkData;
    BookmarkAdapter bookmarkAdapter;
    LinearLayoutManager linearLayoutManager;

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

        bookmarkData = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(this);
        rv_bookmark.setLayoutManager(linearLayoutManager);
        bookmarkAdapter = new BookmarkAdapter(bookmarkData);
        rv_bookmark.setAdapter(bookmarkAdapter);

        //test
        bookmarkData.add(new BookmarkData("88", "11분 30후 도착 예정", "3" + "정거장 전", true));
        bookmarkData.add(new BookmarkData("81-1", "15분 후 도착 예정", "4" + "정거장 전", true));

    }
}