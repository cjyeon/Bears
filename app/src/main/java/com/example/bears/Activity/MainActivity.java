package com.example.bears.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bears.R;
import com.example.bears.Utils.BusStopOpenAPI;
import com.example.bears.Utils.STT;
import com.example.bears.Utils.StationByUidItem;
import com.example.bears.Utils.TTS;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    SpeechRecognizer mRecognizer;
    LinearLayout ll_voice, ll_bookmark, ll_driver;
    ImageView iv_voice;
    static TextView tv_mainbus;
    static EditText et_busstop;
    Button btn_search;
    static String tmX, tmY, BusStopUrl, BusStopServiceKey, ars_Id, stationByUidUrl;
    static String busnumber, stationName, current_result, vehId1, nextStation;
    int pressedTime = 0;
    final int PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    public Location location;
    STT stt;
    TTS tts;
    public HashMap<String, String> BusStopResultMap;
    public HashMap<String, String> StationByResultMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BusStopServiceKey = "SPJi5n0Hw%2Fbd8BBVjSB1hS8hnWIi95BW8oRu%2BN9lFGt%2Bpqu6gfnEPwYfXuOMsJ8ko8nJ1A1EWDOs1oNPommygQ%3D%3D";

        stt = new STT(this);
        tts = new TTS(this);
        btn_search = findViewById(R.id.btn_search);
        ll_voice = findViewById(R.id.ll_voice);
        ll_bookmark = findViewById(R.id.ll_bookmark);
        ll_driver = findViewById(R.id.ll_driver);
        tv_mainbus = findViewById(R.id.tv_mainbus);
        iv_voice = findViewById(R.id.iv_voice);
        et_busstop = findViewById(R.id.et_busstop);
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

        ll_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_mainbus.setText("");
                et_busstop.setText("");

                tts.speech("음성인식을 시작합니다.");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
                mRecognizer.setRecognitionListener(stt);
                mRecognizer.startListening(intent);

                BusStopUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?" +
                        "serviceKey=" + BusStopServiceKey +
                        "&tmX=" + tmX +
                        "&tmY=" + tmY +
                        "&radius=" + "200";

                try {
                    BusStopOpenAPI busStop = new BusStopOpenAPI(BusStopUrl);
                    busStop.execute();
                    BusStopResultMap = busStop.get();
                    ars_Id = Objects.requireNonNull(BusStopResultMap.get("arsId"));
                    stationName = Objects.requireNonNull(BusStopResultMap.get("stationNm"));
                    Log.d("정류장 이름", stationName);
                    Log.d("arsId 결과", ars_Id);


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("실패이유", "PerformAction", e.getCause());
                }

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

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (ars_Id != null) {
                        if (tv_mainbus.getText().toString() != null) {
                            busnumber = busnumber.replaceAll(" ", "");
                            stationByUidUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?" +
                                    "serviceKey=" + BusStopServiceKey +
                                    "&arsId=" + ars_Id;
                            StationByUidItem stationByUidItem = new StationByUidItem(stationByUidUrl, busnumber);
                            stationByUidItem.execute();
                            try {
                                StationByResultMap = stationByUidItem.get();
                                if (StationByResultMap != null) {
                                    Log.d("~!~!~!~!~!", ars_Id);

                                    current_result = StationByResultMap.get("arrmsg1");
                                    vehId1 = StationByResultMap.get("vehId1");
                                    nextStation = StationByResultMap.get("nxtStn");
//                                if(!current_result.equals(result)) {
//                                    result = current_result;
//                                    Log.d("StationByUid 결과", "arrmsg1 : " + result);
//                                    try {
//                                        if(!result.equals("[차고지출발]")){
//                                            array = result.split("\\[");
//                                            minutes = array[0].substring(0, result.indexOf("분"));
//                                            seconds = array[0].substring(result.indexOf("분") + 1, result.indexOf("초"));
//                                            countDown(minutes,seconds);
//                                        }
//                                    } catch (Exception e) {
//                                        tv_arrvaltime.setText(result);
//                                    }
//                                    if (result != "곧 도착") {
//                                        String result2 = array[1].substring(0, array[1].length() - 1);
//                                        tv_arrivalbusstop.setText(result2);
//                                    }
//                                }

                                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                                    intent.putExtra("busnumber", busnumber);
                                    intent.putExtra("ars_Id", ars_Id);
                                    intent.putExtra("stationName", stationName);
                                    intent.putExtra("arrmsg1", current_result);
                                    intent.putExtra("vehId1", vehId1);
                                    intent.putExtra("nextStation", nextStation);
                                    startActivity(intent);
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else tts.speech("버스 번호를 입력해주세요.");
                    } else tts.speech("정류장 번호를 입력해주세요.");
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

    @Override
    protected void onResume() {
        super.onResume();

        tv_mainbus.setText("");
        et_busstop.setText("");

        Intent intent2 = getIntent();
        busnumber = intent2.getStringExtra("busnumber");
        tv_mainbus.setText(busnumber);
        if (stationName != null) et_busstop.setText(stationName);
        else Log.d("stationNum", "널값");

//        if (ars_Id != null) {
//            if (busnumber != null) {
//                busnumber = busnumber.replaceAll(" ", "");
//                stationByUidUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?" +
//                        "serviceKey=" + BusStopServiceKey +
//                        "&arsId=" + ars_Id;
//                StationByUidItem stationByUidItem = new StationByUidItem(stationByUidUrl, busnumber);
//                stationByUidItem.execute();
//                try {
//                    StationByResultMap = stationByUidItem.get();
//                    Log.d("StationByUid 결과", "rtNm : " + StationByResultMap.get("rtNm"));
//                    Log.d("StationByUid 결과", "arrmsg1 : " + StationByResultMap.get("arrmsg1"));
//                    Log.d("StationByUid 결과", "arrmsg2 : " + StationByResultMap.get("arrmsg2"));
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            } else Log.d("arsId는 있음", ars_Id);
//        } else Log.d("arsId가 없음 ", "null 이라 안됨");

    }
}

