package com.example.bears.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bears.R;

public class LoginActivity extends AppCompatActivity {
    ImageView iv_closebtn;
    Button btn_login;
    EditText et_id, et_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iv_closebtn = findViewById(R.id.iv_closebtn);
        btn_login = findViewById(R.id.btn_login);
        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);

        iv_closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 진행
                if (et_id.getText().toString().equals("") || et_pw.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력하세요",Toast.LENGTH_SHORT).show();
                } else {
                    //서버에서 아이디 비번 일치하는 지 확인받아와야 함
                    Intent intent = new Intent(LoginActivity.this, DriverActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}