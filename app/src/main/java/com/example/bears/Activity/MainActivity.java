package com.example.bears.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.bears.R;
import com.example.bears.SpeechToText;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    SpeechRecognizer mRecognizer;
    LinearLayout ll_voice, ll_bookmark, ll_driver;
    ImageView iv_searchbtn;
    EditText et_searchnum;
    int pressedTime = 0;
    final int PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    public Location location;
    String tmX,tmY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll_voice = findViewById(R.id.ll_voice);
        ll_bookmark = findViewById(R.id.ll_bookmark);
        ll_driver = findViewById(R.id.ll_driver);

        iv_searchbtn = findViewById(R.id.iv_searchbtn);
        et_searchnum = findViewById(R.id.et_searchnum);
        ;
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        //권한체크
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET,Manifest.permission.RECORD_AUDIO})
                .check();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        performAction();


        ll_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
                mRecognizer.setRecognitionListener(listener);
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
                    Toast.makeText(MainActivity.this, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show();
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
    private SpeechToText listener = new SpeechToText(this);

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
                            tmY=String.valueOf(location.getLatitude());
                            Log.d("경도", "getLongitude: " + location.getLongitude());
                            tmX=String.valueOf(location.getLongitude());


                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (pressedTime == 0) {
            Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
            pressedTime = (int) System.currentTimeMillis();
        } else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if (seconds > 2000) {
                Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
                pressedTime = 0;
            } else {
                super.onBackPressed();
//                finish(); // app 종료 시키기
            }
        }
    }
}

