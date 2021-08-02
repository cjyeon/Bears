package com.example.bears.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.bears.BookmarkAdapter;
import com.example.bears.R;
import com.example.bears.Room.BookmarkDB;
import com.example.bears.Room.BookmarkDao;
import com.example.bears.Room.BookmarkEntity;
import com.example.bears.Utils.StationByUidItem;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchResultActivity extends AppCompatActivity {
    LinearLayout ll_bookmark, ll_bell;
    ImageView iv_backbtn, iv_star;
    TextView tv_busnum, tv_arrvaltime, tv_arrivalbusstop;
    int i = 0;
    static String busnumber, ars_Id, stationNm,result;
    String stationByUidUrl, BusStopServiceKey,seconds,minutes,corrent_result;
    public HashMap<String, String> StationByResultMap;
    BookmarkAdapter bookmarkAdapter;
    String [] array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        busnumber = intent.getStringExtra("busnumber");
        ars_Id = intent.getStringExtra("ars_Id");
        stationNm = intent.getStringExtra("stationNm");
        BusStopServiceKey = "SPJi5n0Hw%2Fbd8BBVjSB1hS8hnWIi95BW8oRu%2BN9lFGt%2Bpqu6gfnEPwYfXuOMsJ8ko8nJ1A1EWDOs1oNPommygQ%3D%3D";
        result =null;
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
                                    corrent_result = StationByResultMap.get("arrmsg1");
                                    if(!corrent_result.equals(result)) {
                                        result = corrent_result;
                                        Log.d("StationByUid 결과", "arrmsg1 : " + result);
                                        try {
                                            array = result.split("\\[");
                                            minutes = array[0].substring(0, result.indexOf("분"));
                                            seconds = array[0].substring(result.indexOf("분") + 1, result.indexOf("초"));
                                            countDown();
//                                        tv_arrvaltime.setText(minutes+"분 "+seconds+"초");
                                        } catch (Exception e) {
                                            tv_arrvaltime.setText(result);
                                        }
                                        if (result != "곧도착") {
                                            String result2 = array[1].substring(0, array[1].length() - 1);
                                            tv_arrivalbusstop.setText(result2);
                                        }
                                    }





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

//        mContext = getApplicationContext();

        // 메인 스레드에서 DB 접근 불가 -> 읽고 쓸 때 스레드 사용
        class InsertRunnable implements Runnable {
            @Override
            public void run() {
                BookmarkEntity bookmarkEntity = new BookmarkEntity(stationNm, ars_Id, busnumber);
                BookmarkDB.getInstance(getApplicationContext()).bookmarkDao().insert(bookmarkEntity);
                Log.d("저장!!!@!@@!@", stationNm);
            }
        }

//        //UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨)
//        BookmarkDB.getInstance(getApplicationContext()).bookmarkDao().getAll().observe(this, new Observer<List<BookmarkEntity>>() {
//            @Override
//            public void onChanged(List<BookmarkEntity> data) {
//                bookmarkAdapter.setItem(data);
//            }
//        });

        ll_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1 - i;
                if (i == 0) {
                    iv_star.setImageResource(R.drawable.star_outlined);
                    //북마크에서 삭제
                    BookmarkDB.destroyInstance();
                } else {
                    iv_star.setImageResource(R.drawable.star_filled);
                    //북마크 추가
                    InsertRunnable insertRunnable = new InsertRunnable();
                    Thread t = new Thread(insertRunnable);
                    t.start();
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
    public void countDown() {

        long conversionTime = 0;

        // 1000 단위가 1초
        // 60000 단위가 1분
        // 60000 * 3600 = 1시간

        // 변환시간
        conversionTime = Long.valueOf(minutes) * 60 * 1000 + Long.valueOf(seconds) * 1000;

        // 첫번쨰 인자 : 원하는 시간 (예를들어 30초면 30 x 1000(주기))
        // 두번쨰 인자 : 주기( 1000 = 1초)
        new CountDownTimer(conversionTime, 1000) {

            // 특정 시간마다 뷰 변경
            public void onTick(long millisUntilFinished) {

//                // 시간단위
//                String hour = String.valueOf(millisUntilFinished / (60 * 60 * 1000));

                // 분단위
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000));
                String min = String.valueOf(getMin / (60 * 1000)); // 몫

                // 초단위
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // 나머지

//                // 밀리세컨드 단위
//                String millis = String.valueOf((getMin % (60 * 1000)) % 1000); // 몫

//                // 시간이 한자리면 0을 붙인다
//                if (hour.length() == 1) {
//                    hour = "0" + hour;
//                }
//
//                // 분이 한자리면 0을 붙인다
//                if (min.length() == 1) {
//                    min = "0" + min;
//                }

                tv_arrvaltime.setText(min+"분 "+second+"초");
//                count_view.setText(hour + ":" + min + ":" + second);
            }

            // 제한시간 종료시
            public void onFinish() {

                // 변경 후
//                count_view.setText("촬영종료!");

                // TODO : 타이머가 모두 종료될때 어떤 이벤트를 진행할지

            }
        }.start();
    }
}