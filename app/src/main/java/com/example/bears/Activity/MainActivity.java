package com.example.bears.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
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

import com.example.bears.R;
import com.example.bears.Utils.BusStopOpenAPI;
import com.example.bears.Utils.STT;
import com.example.bears.Utils.TTS;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    SpeechRecognizer mRecognizer;
    LinearLayout ll_voice, ll_bookmark, ll_driver;
    ImageView iv_searchbtn, iv_voice;
    EditText et_searchnum;
    String tmX, tmY, url, servicekey;
    int pressedTime = 0;
    final int PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    public Location location;
    STT stt;
    TTS tts;
    public static String station_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        servicekey = "SPJi5n0Hw%2Fbd8BBVjSB1hS8hnWIi95BW8oRu%2BN9lFGt%2Bpqu6gfnEPwYfXuOMsJ8ko8nJ1A1EWDOs1oNPommygQ%3D%3D";

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

                url = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?" +
                        "serviceKey=" + servicekey +
                        "&tmX=" + tmX +
                        "&tmY=" + tmY +
                        "&radius=" + "200";

                try {
                    BusStopOpenAPI busstop = new BusStopOpenAPI(url);
                    busstop.execute();
                    station_id = busstop.get();
                    Log.d("결과", station_id);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("실패이유", "PerformAction", e.getCause());
                }
                ;


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

//    public class OpenAPI extends AsyncTask<Void, Void, String> {
//        private String url;
//
//        public OpenAPI(String url) {
//            this.url = url;
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            // parsing할 url 지정(API 키 포함해서)
//
//            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = null;
//            try {
//                dBuilder = dbFactoty.newDocumentBuilder();
//            } catch (ParserConfigurationException e) {
//                e.printStackTrace();
//            }
//            Document doc = null;
//            try {
//                doc = dBuilder.parse(url);
//            } catch (IOException | SAXException e) {
//                e.printStackTrace();
//            }
//
//            // root tag
//            doc.getDocumentElement().normalize();
//            System.out.println("Root element: " + doc.getDocumentElement().getNodeName()); // Root element: result
//
//            // 파싱할 tag
//            NodeList nList = doc.getElementsByTagName("itemList");
//
//            for(int temp = 0; temp < nList.getLength(); temp++){
//                Node nNode = nList.item(temp);
//                if(nNode.getNodeType() == Node.ELEMENT_NODE){
//
//                    Element eElement = (Element) nNode;
//                    Log.d("OPEN_API","arsId  : " + getTagValue("arsId", eElement));
//                    Log.d("OPEN_API","stationId  : " + getTagValue("stationId", eElement));
//                    Log.d("OPEN_API","stationNm : " + getTagValue("stationNm", eElement));
//                }	// for end
//            }	// if end
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String str) {
//            super.onPostExecute(str);
//        }
//    }
//
//    private String getTagValue(String tag, Element eElement) {
//        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
//        Node nValue = (Node) nlList.item(0);
//        if(nValue == null)
//            return null;
//        return nValue.getNodeValue();
//    }
}

