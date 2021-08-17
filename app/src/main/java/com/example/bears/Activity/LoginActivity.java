package com.example.bears.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bears.Model.LoginModel;
import com.example.bears.R;
import com.example.bears.Retrofit.RetrofitBuilder;
import com.example.bears.Retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ImageView iv_closebtn;
    Button btn_login;
    EditText et_id, et_pw;
    RetrofitService retrofitService;

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
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    //서버에서 아이디 비번 일치하는 지 확인
                    retrofitService = RetrofitBuilder.getRetrofit().create(RetrofitService.class);
                    String id = et_id.getText().toString();
                    Call<LoginModel> call = retrofitService.getLoginCheck(id, et_pw.getText().toString());
                    call.enqueue(new Callback<LoginModel>() {
                        @Override
                        public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                            if (response.isSuccessful()) {
                                Log.d("연결 성공", response.message());
                                LoginModel loginModel = response.body();
                                Log.v("Code", loginModel.getCode());
                                Log.v("Message", loginModel.getMessage());
                                Log.v("BusNum", loginModel.getBusNum());
                                if (loginModel.getCode().equals("200")) {
                                    Intent intent = new Intent(LoginActivity.this, DriverActivity.class);
                                    intent.putExtra("beaconID",id);
                                    intent.putExtra("busNum", loginModel.getBusNum());
                                    startActivity(intent);
                                    finish();
                                } else if (loginModel.getCode().equals("200")){
                                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해주세요"
                                            , Toast.LENGTH_SHORT).show();
                                    Log.d("ssss", response.message());
                                }
                            } else if (response.code() == 404) {
                                Toast.makeText(LoginActivity.this, "인터넷 연결을 확인해주세요"
                                        , Toast.LENGTH_SHORT).show();
                                Log.d("ssss", response.message());
                            } else {
                                Toast.makeText(LoginActivity.this, "서비스 점검중입니다."
                                        , Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginModel> call, Throwable t) {
                            Log.d("ssss", t.getMessage());
                        }
                    });
                }
            }
        });
    }
}