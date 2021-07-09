package com.example.bears.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bears.BookmarkAdapter;
import com.example.bears.BookmarkData;
import com.example.bears.DriverAdapter;
import com.example.bears.DriverData;
import com.example.bears.R;
import com.example.bears.RecyclerDecoration;

import java.util.ArrayList;
import java.util.List;

public class DriverActivity extends AppCompatActivity {
    Button btn_logout;
    RecyclerView rv_driver;
    ArrayList<DriverData> driverData;
    DriverAdapter driverAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        btn_logout = findViewById(R.id.btn_logout);
        rv_driver = findViewById(R.id.rv_driver);

        RecyclerDecoration decoration_height = new RecyclerDecoration(20);
        rv_driver.addItemDecoration(decoration_height);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그아웃 진행
                finish();
            }
        });

        driverData = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(this);
        rv_driver.setLayoutManager(linearLayoutManager);
        driverAdapter = new DriverAdapter(driverData);
        rv_driver.setAdapter(driverAdapter);

        //test
        driverData.add(new DriverData("성신여자대학교 입구역"));
        driverData.add(new DriverData("돈암2동주민센터입구.흥천사"));
        driverData.add(new DriverData("아리랑고개.아리랑시네미디어센터"));

    }
}