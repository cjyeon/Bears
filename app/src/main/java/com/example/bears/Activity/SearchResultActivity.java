package com.example.bears.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bears.Model.BeaconModel;
import com.example.bears.R;
import com.example.bears.Retrofit.RetrofitBuilder;
import com.example.bears.Retrofit.RetrofitService;
import com.example.bears.Room.BookmarkDB;
import com.example.bears.Room.BookmarkEntity;
import com.example.bears.Utils.StationByUidItem;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.ArmaRssiFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity implements BeaconConsumer {
    LinearLayout ll_bookmark, ll_bell;
    ImageView iv_backbtn, iv_star, iv_bell;
    TextView tv_busnum, tv_arrivaltime, tv_arrivalbusstop, tv_curBusStop;
    static String busnumber, ars_Id, stationName, result, vehId1, nextStation, beaId, checkVehId,checkVehId2;
    String stationByUidUrl, BusStopServiceKey, seconds, minutes, current_result;
    public HashMap<String, String> StationByResultMap;
    String[] array;
    RetrofitService retrofitService;
    Button btn_beacon;
    BeaconManager beaconManager;
    private List<Beacon> beaconList;    // ????????? ???????????? ????????? ?????? ?????????
    int i, repeat, j = 0;
    String TAG = "?????? ?????????";
    long[] vibratePattern;
    Vibrator vibrator;
    Region region;
    Thread timeChange;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // 0.5??? ?????? -> 1??? ?????? -> 0.5??? ?????? -> 1??? ??????
        vibratePattern = new long[]{200, 1000, 200, 1000};
        // ?????? ??????
        repeat = -1;
        Intent intent = getIntent();
        busnumber = intent.getStringExtra("busnumber");
        ars_Id = intent.getStringExtra("ars_Id");
        stationName = intent.getStringExtra("stationName");
        current_result = intent.getStringExtra("arrmsg1");
        vehId1 = intent.getStringExtra("vehId1");
        nextStation = intent.getStringExtra("nextStation");
        beaId = null;
        checkVehId = null;
        checkVehId2=vehId1;
        BusStopServiceKey = "SPJi5n0Hw%2Fbd8BBVjSB1hS8hnWIi95BW8oRu%2BN9lFGt%2Bpqu6gfnEPwYfXuOMsJ8ko8nJ1A1EWDOs1oNPommygQ%3D%3D";

        beaconList = new ArrayList<>();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        result = null;
        ll_bookmark = findViewById(R.id.ll_bookmark);
        ll_bell = findViewById(R.id.ll_bell);
        iv_bell = findViewById(R.id.iv_bell);
        iv_backbtn = findViewById(R.id.iv_backbtn);
        iv_star = findViewById(R.id.iv_star);
        tv_busnum = findViewById(R.id.tv_searchbusnum);
        tv_arrivaltime = findViewById(R.id.tv_arrivaltime);
        tv_arrivalbusstop = findViewById(R.id.tv_arrivalbusstop);
        tv_curBusStop = findViewById(R.id.tv_curBusStop);
        btn_beacon = findViewById(R.id.btn_beacon);
        tv_busnum.setText(busnumber);
        tv_curBusStop.setText(stationName);

        beaconList = new ArrayList<>();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);

//        // ?????? ????????? ????????????. ???????????? ???????????? ???????????????.
//        //        beaconManager.bind(this);
//        beaconManager.bindInternal(this);
        // ???????????? ?????? ?????? ??? ????????? ??????
        class SelectRunnable implements Runnable {
            @Override
            public void run() {
                if (BookmarkDB.getInstance(getApplicationContext()).bookmarkDao().getCountBookmark(busnumber, ars_Id) == 0) {
                    i = 1;
                    iv_star.setImageResource(R.drawable.star_outlined);
                } else {
                    i = 0;
                    iv_star.setImageResource(R.drawable.star_filled);
                }
            }
        }
        SelectRunnable selectRunnable = new SelectRunnable();
        Thread t = new Thread(selectRunnable);
        t.start();

        timeChange = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted())
                    try {
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {
                            @Override
                            public void run() {
                                try {
                                    stationByUidUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?" +
                                            "serviceKey=" + BusStopServiceKey +
                                            "&arsId=" + ars_Id;

                                    StationByUidItem stationByUidItem = new StationByUidItem(stationByUidUrl, busnumber);
                                    stationByUidItem.execute();

                                    StationByResultMap = stationByUidItem.get();
                                    current_result = StationByResultMap.get("arrmsg1");
                                    vehId1 = StationByResultMap.get("vehId1");
                                    if (!vehId1.equals(checkVehId2)){
                                        iv_bell.setImageResource(R.drawable.bell);
                                        btn_beacon.setEnabled(false);
                                        btn_beacon.setTextColor(getColor(R.color.colorgrey));
                                        btn_beacon.setBackgroundResource(R.drawable.arrival_alarm_false);
                                        checkVehId2 = vehId1;
                                    }
                                    Log.d("StationByUid ??????", "arrmsg1 : " + current_result+"   vehId: "+vehId1);
                                    if (current_result.contains("[??????]")) {
                                        current_result = current_result.replaceAll("\\[??????\\] ", "");
                                    }
                                    if (current_result.equals("[???????????????] ")) {
                                        current_result = current_result.replaceAll("\\[", "").replaceAll("\\]", "");
                                        tv_arrivaltime.setText(current_result);
                                        tv_arrivalbusstop.setText("");
                                    } else if (current_result.equals("??? ??????") || current_result.equals("????????????") || current_result.equals("????????????")) {
                                        tv_arrivaltime.setText(current_result);
                                        tv_arrivalbusstop.setText("");
                                    } else {
                                        array = current_result.split("\\[");
                                        minutes = array[0].substring(0, current_result.indexOf("???"));

                                        if (current_result.contains("???"))
                                            seconds = array[0].substring(current_result.indexOf("???") + 1, current_result.indexOf("???"));
                                        else
                                            seconds = "0";

                                        if (countDownTimer != null) {
                                            countDownTimer.cancel();
                                        }
                                        countDown(minutes, seconds);
                                        tv_arrivalbusstop.setText(array[1].substring(0, array[1].length() - 1));
                                    }
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
            }
        });
        timeChange.start();

        iv_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeChange.interrupt();
                if(handler!=null){
                    handler.removeMessages(0);
                }
                finish();
            }
        });

        btn_beacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                j = 1 - j;
                if(j == 0) {
                    btn_beacon.setText("???????????? ????????????");
                    btn_beacon.setBackground(getDrawable(R.drawable.arrival_alarm_true));
                    beaconManager.unbindInternal(SearchResultActivity.this);
                    beaconManager.stopRangingBeacons(region);
                    Log.d("?????? ????????????", "??????????????????");
                    handler.removeMessages(0);
                } else {
                    btn_beacon.setText("???????????? ????????????");
                    btn_beacon.setBackground(getDrawable(R.drawable.search_result_border));
                    // ?????? ????????? ????????????. ???????????? ???????????? ???????????????.
                    beaconManager.bindInternal(SearchResultActivity.this);
                    handler.sendEmptyMessage(0);
                }
            }
        });

        // ?????? ??????????????? DB ?????? ?????? -> ?????? ??? ??? ????????? ??????
        class InsertRunnable implements Runnable {
            @Override
            public void run() {
                BookmarkEntity bookmarkEntity = new BookmarkEntity(stationName, ars_Id, busnumber, nextStation);
                BookmarkDB.getInstance(getApplicationContext()).bookmarkDao().insert(bookmarkEntity);
                Log.d("room ????????? ??????", stationName);
            }
        }

        class DeleteRunnable implements Runnable {
            @Override
            public void run() {
                BookmarkEntity bookmarkEntity = new BookmarkEntity(stationName, ars_Id, busnumber, nextStation);
                BookmarkDB.getInstance(getApplicationContext()).bookmarkDao().deleteById(busnumber, ars_Id);
                Log.d("room ????????? ??????", stationName);
            }
        }

        ll_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1 - i;
                if (i == 0) {
                    iv_star.setImageResource(R.drawable.star_filled);
                    //???????????? ??????
                    InsertRunnable insertRunnable = new InsertRunnable();
                    Thread insert = new Thread(insertRunnable);
                    insert.start();
                } else {
                    iv_star.setImageResource(R.drawable.star_outlined);
                    //??????????????? ??????
                    DeleteRunnable deleteRunnable = new DeleteRunnable();
                    Thread delete = new Thread(deleteRunnable);
                    delete.start();
                }
            }
        });

        ll_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ?????????????????? ??????
                if (!vehId1.equals(checkVehId) ) { //?????? ?????? ???????????? vehid ?????? ??????????????? ???????????????? ?????????????????? ??????
                    Log.d("vehid??? checkVehid ?????????: ", vehId1 + "    ch: " + checkVehId);
                    retrofitService = RetrofitBuilder.getRetrofit().create(RetrofitService.class);
                    Call<BeaconModel> call = retrofitService.NoticeBusStop(stationName, vehId1);

                    call.enqueue(new Callback<BeaconModel>() {
                        @Override
                        public void onResponse(Call<BeaconModel> call, Response<BeaconModel> response) {
                            if (response.isSuccessful()) {
                                Log.d("?????? ??????", response.message());
                                BeaconModel beaModel = response.body();
                                checkVehId = vehId1;
                                if (beaModel.getCode().equals("200")) {
                                    Toast.makeText(SearchResultActivity.this, "????????? ???????????????."
                                            , Toast.LENGTH_SHORT).show();
                                    iv_bell.setImageResource(R.drawable.bell_enabled);
                                    Log.v("Code", beaModel.getCode());
                                    Log.v("Message", beaModel.getMessage());
                                    beaId = beaModel.getBeaId();
                                    Log.v("beaId", beaId);

                                    btn_beacon.setEnabled(true);
                                    btn_beacon.setTextColor(getColor(R.color.colorPrimary));
                                    btn_beacon.setBackgroundResource(R.drawable.arrival_alarm_true);
                                } else {
                                    Toast.makeText(SearchResultActivity.this, "????????????"
                                            , Toast.LENGTH_SHORT).show();
                                    Log.d("ssss", response.message());
                                }
                            } else if (response.code() == 404) {
                                Toast.makeText(SearchResultActivity.this, "????????? ????????? ??????????????????"
                                        , Toast.LENGTH_SHORT).show();

                                Log.d("ssss", response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<BeaconModel> call, Throwable t) {
                            Log.d("ssss", t.getMessage());
                        }
                    });
                }
            }
        });
    }

    public void countDown(String minutes, String seconds) {
        long conversionTime = 0;
        // 1000 ????????? 1???
        // 60000 ????????? 1???
        // 60000 * 3600 = 1??????

        // ????????????
        conversionTime = Long.valueOf(minutes) * 60 * 1000 + Long.valueOf(seconds) * 1000;

        // ????????? ?????? : ????????? ?????? (???????????? 30?????? 30 x 1000(??????))
        // ????????? ?????? : ??????( 1000 = 1???)
        countDownTimer = new CountDownTimer(conversionTime, 1000) {
            // ?????? ???????????? ??? ??????
            public void onTick(long millisUntilFinished) {
                // ?????????
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000));
                String min = String.valueOf(getMin / (60 * 1000)); // ???
                // ?????????
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // ?????????

                if (min.equals("0"))
                    tv_arrivaltime.setText(second + "??? ??? ?????? ??????");
                else tv_arrivaltime.setText(min + "??? " + second + "??? ??? ?????? ??????");
            }
            public void onFinish() {
            // ???????????? ?????????
            }
        };

        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        beaconManager.unbind(this);
        beaconManager.unbindInternal(this);
        beaconManager.stopRangingBeacons(region);

        if(handler!=null){
            handler.removeMessages(0);
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) { //??? ????????? ????????? ??????????????????
                // textView.setText("?????? \n");
                Log.d(TAG, "?????? ?????? ??????!");
            }
            @Override
            public void didExitRegion(Region region) {
                Log.d(TAG, "Exit : " + region.getId1());
                Log.d(TAG, "????????? ????????? ?????????");
            }
            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                Log.d(TAG, "??????????????? 0 ???????????? 1 : " + i);
            }
        });

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    beaconList.clear();
                    for (Beacon beacon : beacons) {
                        beaconList.add(beacon);
                    }
                }
            }
        });
        try {
//            beaconManager.startMonitoringBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            Log.d("???????????????: ",beaId);
//            region = new Region("myRangingUniqueId", Identifier.parse("74278bda-b644-4520-8f0c-720eaf059935"), null, null);
            region = new Region("myRangingUniqueId", Identifier.parse(beaId), null, null);
//            region = new Region("myRangingUniqueId", null, null, null);

            Log.d("???????????????: ",beaId);
            region = new Region("myRangingUniqueId", Identifier.parse(beaId), null, null);

            beaconManager.startRangingBeaconsInRegion(region);
            Log.d("region???: ", region.toString());
        } catch (RemoteException e) {
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        //        @RequiresApi(api = Build.VERSION_CODES.O)
        public void handleMessage(Message msg) {
            for (Beacon beacon : beaconList) {
                double distance = Double.parseDouble(String.format("%.3f", beacon.getDistance()));
                Log.d("?????? ??????", "ID : " + beacon.getId2() + " / " + "Distance : " + distance + "m\n");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (distance < 2.0) {
                        vibrator.vibrate(VibrationEffect.createWaveform(vibratePattern, repeat));
                    }
                }
            }
            handler.sendEmptyMessageDelayed(0, 500);// ?????? ????????? 0.5????????? ??????
        }};

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timeChange.interrupt();
        finish();
    }
}