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
import com.example.bears.RecyclerDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class DriverActivity extends AppCompatActivity {
    Button btn_logout;
    RecyclerView rv_driver;
    ArrayList<DriverData> driverData;
    DriverAdapter driverAdapter;
    LinearLayoutManager linearLayoutManager;
    TextView tv_beaconnum;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        btn_logout = findViewById(R.id.btn_logout);
        rv_driver = findViewById(R.id.rv_driver);
        tv_beaconnum = findViewById(R.id.tv_beaconnum);
        RecyclerDecoration decoration_height = new RecyclerDecoration(20);
        rv_driver.addItemDecoration(decoration_height);

        Intent intent = getIntent();
        String id = intent.getStringExtra("beaconID");
        tv_beaconnum.setText(id);


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그아웃 진행
                Intent intent = new Intent(DriverActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        init();

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

    private void init() {
        try {
            mSocket = IO.socket("http://10.0.2.2:80");
            Log.d("SOCKET", "Connection success : " + mSocket.id());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();

        JSONObject data = new JSONObject();
        try {
            data.put("key1", "value1");
            data.put("key2", "value2");
            mSocket.emit("event-name", data);
        } catch(JSONException e) {
            e.printStackTrace();
        }


        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on("chat-message", onMessageReceived);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

    // Socket서버에 connect 된 후, 서버로부터 전달받은 'Socket.EVENT_CONNECT' Event 처리.
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // your code...
        }
    };
    // 서버로부터 전달받은 'chat-message' Event 처리.
    private Emitter.Listener onMessageReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
            try {
                JSONObject receivedData = (JSONObject) args[0];
                String recievedMsg = receivedData.getString("key값");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}