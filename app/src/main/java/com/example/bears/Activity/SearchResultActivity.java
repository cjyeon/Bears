package com.example.bears.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bears.R;

public class SearchResultActivity extends AppCompatActivity {
    LinearLayout ll_bookmark, ll_bell;
    ImageView iv_backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        String busnumber = intent.getStringExtra("busnumber");

        ll_bookmark = findViewById(R.id.ll_bookmark);
        ll_bell = findViewById(R.id.ll_bell);
        iv_backbtn = findViewById(R.id.iv_backbtn);

        iv_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 북마크기능,,
            }
        });

        ll_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버스기사에게 알림
            }
        });


    }
}