package com.example.bears.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bears.R;
import com.example.bears.Utils.StationByUidItem;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class SearchResultActivity extends AppCompatActivity {
    LinearLayout ll_bookmark, ll_bell;
    ImageView iv_backbtn, iv_star;
    TextView tv_busnum, tv_arrvaltime, tv_arrivalbusstop;
    int i = 0;
    static String busnumber, ars_Id, stationNm;
    String stationByUidUrl, BusStopServiceKey;
    public HashMap<String, String> StationByResultMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        busnumber = intent.getStringExtra("busnumber");
        ars_Id = intent.getStringExtra("ars_Id");
        stationNm = intent.getStringExtra("stationNm");
        BusStopServiceKey = "SPJi5n0Hw%2Fbd8BBVjSB1hS8hnWIi95BW8oRu%2BN9lFGt%2Bpqu6gfnEPwYfXuOMsJ8ko8nJ1A1EWDOs1oNPommygQ%3D%3D";

        ll_bookmark = findViewById(R.id.ll_bookmark);
        ll_bell = findViewById(R.id.ll_bell);
        iv_backbtn = findViewById(R.id.iv_backbtn);
        iv_star = findViewById(R.id.iv_star);
        tv_busnum = findViewById(R.id.tv_searchbusnum);
        tv_arrvaltime = findViewById(R.id.tv_arrivaltime);
        tv_arrivalbusstop = findViewById(R.id.tv_arrivalbusstop);

        tv_busnum.setText(busnumber);


        if (ars_Id != null) {
            if (busnumber != null) {
                busnumber = busnumber.replaceAll(" ", "");
                stationByUidUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?" +
                        "serviceKey=" + BusStopServiceKey +
                        "&arsId=" + ars_Id;
                StationByUidItem stationByUidItem = new StationByUidItem(stationByUidUrl, busnumber);
                stationByUidItem.execute();
                try {
                    StationByResultMap = stationByUidItem.get();
                    Log.d("StationByUid 결과", "rtNm : " + StationByResultMap.get("rtNm"));
                    Log.d("StationByUid 결과", "arrmsg1 : " + StationByResultMap.get("arrmsg1"));
                    Log.d("StationByUid 결과", "arrmsg2 : " + StationByResultMap.get("arrmsg2"));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else Log.d("arsId는 있음", ars_Id);
        } else Log.d("arsId가 없음 ", "null 이라 안됨");

        Thread timeChange = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted())
                    try {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {
                            @Override
                            public void run() {
                                try {
                                    StationByUidItem stationByUidItem = new StationByUidItem(stationByUidUrl, busnumber);
                                    stationByUidItem.execute();
                                    StationByResultMap = stationByUidItem.get();
                                    Log.d("StationByUid 결과", "arrmsg1 : " + StationByResultMap.get("arrmsg1"));
                                    tv_arrvaltime.setText(StationByResultMap.get("arrmsg1"));
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        // ooops
                    }
            }
        });
        timeChange.start();


        iv_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeChange.interrupt();
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
                } else {
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