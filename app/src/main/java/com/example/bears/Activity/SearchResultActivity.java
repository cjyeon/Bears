package com.example.bears.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bears.R;

public class SearchResultActivity extends AppCompatActivity {
    LinearLayout ll_bookmark, ll_bell;
    ImageView iv_backbtn, iv_star;
    TextView tv_busnum, tv_arrvaltime, tv_arrivalbusstop;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        String busnumber = intent.getStringExtra("busnumber");

        ll_bookmark = findViewById(R.id.ll_bookmark);
        ll_bell = findViewById(R.id.ll_bell);
        iv_backbtn = findViewById(R.id.iv_backbtn);
        iv_star = findViewById(R.id.iv_star);
        tv_busnum = findViewById(R.id.tv_searchbusnum);
        tv_arrvaltime = findViewById(R.id.tv_arrivaltime);
        tv_arrivalbusstop = findViewById(R.id.tv_arrivalbusstop);

        tv_busnum.setText(busnumber);

        iv_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1 - i;
                if (i == 0) {
                    iv_star.setImageResource(R.drawable.star_outlined);
                    //북마크에서 삭제
                }
                else{
                    iv_star.setImageResource(R.drawable.star_filled);
                    //북마크 추가
                }
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