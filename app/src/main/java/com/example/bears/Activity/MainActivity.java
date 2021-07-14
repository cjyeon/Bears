package com.example.bears.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bears.Model.StationByPosModel;
import com.example.bears.R;
import com.example.bears.Retrofit.RetrofitBuilder;
import com.example.bears.Retrofit.RetrofitService;
import com.example.bears.STT;
import com.example.bears.TTS;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    SpeechRecognizer mRecognizer;
    LinearLayout ll_voice, ll_bookmark, ll_driver;
    ImageView iv_searchbtn, iv_voice;
    EditText et_searchnum;
    String tmX, tmY;
    int pressedTime = 0;
    final int PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    public Location location;

    STT stt;
    TTS tts;

    RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stt = new STT(this);
        tts = new TTS(this);

        ll_voice = findViewById(R.id.ll_voice);
        ll_bookmark = findViewById(R.id.ll_bookmark);
        ll_driver = findViewById(R.id.ll_driver);

        iv_voice = findViewById(R.id.iv_voice);
        iv_searchbtn = findViewById(R.id.iv_searchbtn);
        et_searchnum = findViewById(R.id.et_searchnum);

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        //권한체크
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO})
                .check();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        performAction();

//        stationBuPos();

        ll_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speech("음성인식을 시작합니다.");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
                mRecognizer.setRecognitionListener(stt);
                mRecognizer.startListening(intent);
            }
        });

        ll_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, BookmarkActivity.class);
                startActivity(intent);
            }
        });

        ll_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        iv_searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, SearchResultActivity.class);
                if (!et_searchnum.getText().toString().isEmpty()) {
                    intent.putExtra("busnumber", et_searchnum.getText().toString());
                    startActivity(intent);
                    et_searchnum.setText(null); // 수정해야함
                } else
                    tts.speech("검색어를 입력해주세요");
            }
        });
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            performAction();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    @SuppressLint("MissingPermission")
    private void performAction() {
        fusedLocationClient.getLastLocation()
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("실패이유", "PerformAction", e.getCause());
                    }
                })
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        Log.d("위치", "performAction " + location);

                        if (location != null) {
                            // Logic to handle location object
                            Log.d("위도", "getLatitude: " + location.getLatitude());
                            tmY = String.valueOf(location.getLatitude());
                            Log.d("경도", "getLongitude: " + location.getLongitude());
                            tmX = String.valueOf(location.getLongitude());

                        }
                    }
                });
    }

    public void stationBuPos() {
        retrofitService = RetrofitBuilder.getStationInfo().create(RetrofitService.class);

        Call<StationByPosModel> call = retrofitService.getStationByPos(
                "SPJi5n0Hw%2Fbd8BBVjSB1hS8hnWIi95BW8oRu%2BN9lFGt%2Bpqu6gfnEPwYfXuOMsJ8ko8nJ1A1EWDOs1oNPommygQ%3D%3D",
                "126.892473", "37.473272", "100");

        call.enqueue(new Callback<StationByPosModel>() {
            @Override
            public void onResponse(Call<StationByPosModel> call, Response<StationByPosModel> response) {
                Log.d("연결 성공", response.message());
                Log.d("XML", response.body().toString());

//                Log.d("아아아악", response.body().getServiceResult().getMsgBody().toString());


//                if (response.body() != null) {
//                    dataInfo = dataList.calender_results;
//                    if (response.body().getCode().equals("200")) {
//                        writingListAdapter = new WritingListAdapter(getApplicationContext(), dataInfo);
//                        recyclerView.setAdapter(writingListAdapter);
//                        tv_empty.setVisibility(View.GONE);
//                    } else {
//                        writingListAdapter = new WritingListAdapter(getApplicationContext(), dataInfo);
//                        recyclerView.setAdapter(writingListAdapter);
//                        tv_empty.setVisibility(View.VISIBLE);
//                    }
//                }
            }

            @Override
            public void onFailure(Call<StationByPosModel> call, Throwable t) {
                Log.d("ssss", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (pressedTime == 0) {
            tts.speech(" 한 번 더 누르면 종료됩니다.");
            pressedTime = (int) System.currentTimeMillis();
        } else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if (seconds > 2000) {
                tts.speech(" 한 번 더 누르면 종료됩니다.");
                pressedTime = 0;
            } else {
                super.onBackPressed();
//                finish(); // app 종료 시키기
            }
        }
    }
}

