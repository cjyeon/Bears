package com.example.bears.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bears.DriverAdapter;
import com.example.bears.DriverData;
import com.example.bears.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;

public class DriverActivity extends AppCompatActivity {
    Button btn_logout;
    RecyclerView rv_driver;
    static ArrayList<DriverData> driverData;
    static DriverAdapter driverAdapter;
    LinearLayoutManager linearLayoutManager;
    TextView tv_beaconnum, tv_driverBusNum;
    private Socket mSocket;
    JSONObject data;
    static String id, busNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        btn_logout = findViewById(R.id.btn_logout);
        rv_driver = findViewById(R.id.rv_driver);
        tv_beaconnum = findViewById(R.id.tv_beaconnum);
        tv_driverBusNum = findViewById(R.id.tv_driverbusnum);
        data = new JSONObject();
        driverData = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(this);
        rv_driver.setLayoutManager(linearLayoutManager);
        driverAdapter = new DriverAdapter(driverData);
        rv_driver.setAdapter(driverAdapter);
        Intent intent = getIntent();
        id = intent.getStringExtra("beaconID");
        busNum = intent.getStringExtra("busNum");
        tv_beaconnum.setText(id);
        tv_driverBusNum.setText(busNum);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그아웃 진행
                Intent intent = new Intent(DriverActivity.this, LoginActivity.class);
                startActivity(intent);
                mSocket.emit("logout", id);
                mSocket.close();
                finish();
            }
        });
        init();

    }

    private void init() {
        try {
            mSocket = IO.socket("http://3.36.159.238:8080");
//            mSocket = IO.socket("http://ec2-3-36-159-238.ap-northeast-2.compute.amazonaws.com:3000/");

            mSocket.on(Socket.EVENT_CONNECT, args -> {
                try {
                    Gson gson = new Gson();

                    data.put("username", id);
                    data.put("roomNumber", id.substring(id.length() - 2));
                    String jsonPlace = gson.toJson(data);
                    mSocket.emit("enter", jsonPlace);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });
            mSocket.on("board", args -> {
                for (int i = 0; i < args.length; i++) {
                    String s = args[0].toString();
                    s = s.replace("[", "");
                    s = s.replace("]", "");
                    s = s.replace("\"", "");
                    Log.d("정류장 정보: ", s);
                    String[] strarr = s.split(",");
                    for (String s1 : strarr) {
                        s1 = "[" + s1 + "]" + " 정류장 ";
                        driverData.add(new DriverData(s1));//데이터 넣기
                    }
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        driverAdapter.notifyDataSetChanged();
                        // Stuff that updates the UI

                    }
                });

//                driverAdapter.setItem(driverData);
            });
            mSocket.connect();
            Log.d("소켓통신", "Connection success : " + mSocket.id());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.d("예외처리로 들어옴", "Connection success : " + mSocket.id());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}