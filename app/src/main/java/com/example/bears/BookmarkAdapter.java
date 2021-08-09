package com.example.bears;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bears.Activity.BookmarkActivity;
import com.example.bears.Room.BookmarkDB;
import com.example.bears.Room.BookmarkDao;
import com.example.bears.Room.BookmarkEntity;
import com.example.bears.Utils.StationByUidItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.logging.LogRecord;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.MyViewHolder> {
    Context context;
    static List<BookmarkEntity> bookmarkEntities = new ArrayList<>();
    private BookmarkDB bookmarkDB;
    String stationByUidUrl, BusStopServiceKey, busNum, stationId, stationName, nextStation, result, current_result, vehId1, seconds, minutes;
    public HashMap<String, String> StationByResultMap;
    String [] array;

    int i = 0;

    public BookmarkAdapter(BookmarkDB bookmarkDB, Context context) {
        this.bookmarkDB = bookmarkDB;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return bookmarkEntities.size();
    }

    public static List<BookmarkEntity> getItems() {return bookmarkEntities;}

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_bookmark, parent, false);

        context = parent.getContext();

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BusStopServiceKey = "SPJi5n0Hw%2Fbd8BBVjSB1hS8hnWIi95BW8oRu%2BN9lFGt%2Bpqu6gfnEPwYfXuOMsJ8ko8nJ1A1EWDOs1oNPommygQ%3D%3D";

        class Timechange implements Runnable {
            @Override
            public void run() {
                while (!Thread.interrupted())
                    try {
                        Thread.sleep(5000);
                        ((Activity)context).runOnUiThread(new Runnable() // start actions in UI thread
                        {
                            @Override
                            public void run() {
                                try {
                                    for (i = 0; i < bookmarkEntities.size(); i++) {
                                        stationName = bookmarkEntities.get(position).getStationName();
                                        stationId = bookmarkEntities.get(position).getStationId();
                                        busNum = bookmarkEntities.get(position).getBusNum();

                                        stationByUidUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?" +
                                                "serviceKey=" + BusStopServiceKey +
                                                "&arsId=" + stationId;

                                        StationByUidItem stationByUidItem = new StationByUidItem(stationByUidUrl, busNum);
                                        stationByUidItem.execute();

                                        StationByResultMap = stationByUidItem.get();

                                        try {
                                            current_result = StationByResultMap.get("arrmsg1");
                                        } catch (NullPointerException e) {
                                            Log.d("api 도착시간 널값", "arrmsg1 : " + result);
                                        }

                                        vehId1 = StationByResultMap.get("vehId1");

                                        if (!current_result.equals(result)) {
                                            result = current_result;
                                            Log.d("StationByUid 결과", "arrmsg1 : " + result);
                                            try {
                                                if (!result.equals("[차고지출발]") || !result.equals("운행종료")) {
                                                    array = result.split("\\[");
                                                    minutes = array[0].substring(0, result.indexOf("분"));
                                                    seconds = array[0].substring(result.indexOf("분") + 1, result.indexOf("초"));
                                                    countDown(minutes, seconds, holder);
                                                }
                                            } catch (Exception e) {
                                                holder.tv_arrivaltime.setText(result);
                                            }
                                            if (!result.equals("곧 도착")) {
                                                String result2 = array[1].substring(0, array[1].length() - 1);
                                                holder.tv_arrivalbusstop.setText(result2);
                                            }
                                        }
                                    }
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        // ooops
                    }
            }
        };

        // 메인 스레드에서 DB 접근 불가 -> 읽고 쓸 때 스레드 사용
        class SelectRunnable implements Runnable {
            @Override
            public void run() {
                    stationName = bookmarkEntities.get(position).getStationName();
                    stationId = bookmarkEntities.get(position).getStationId();
                    busNum = bookmarkEntities.get(position).getBusNum();
                    nextStation = bookmarkEntities.get(position).getNextStation();

                    holder.stationName.setText(stationName);
                    holder.stationId.setText(stationId);
                    holder.tv_busnum.setText(busNum);
                    holder.stationDirec.setText(nextStation + " 방면");
            }
        }

        for (i = 0; i < bookmarkEntities.size(); i++) {
            SelectRunnable selectRunnable = new SelectRunnable();
            Thread t = new Thread(selectRunnable);
            t.start();

            Timechange timechange = new Timechange();
            Thread tt = new Thread(timechange);
            tt.start();
        }

//        holder.tv_arrivaltime.setText(bookmarkData.get(position).getArrival_time());
//        holder.tv_arrivalbusstop.setText(bookmarkData.get(position).getArrival_busstop());

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView stationName, stationId, stationDirec;
        private TextView tv_busnum, tv_arrivaltime, tv_arrivalbusstop;
        private ImageView iv_star;

        int i = 0;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            stationName = itemView.findViewById(R.id.tv_busstopname);
            stationId = itemView.findViewById(R.id.tv_busstopid);
            stationDirec = itemView.findViewById(R.id.tv_busdirection);
            tv_busnum = itemView.findViewById(R.id.tv_busnum);
            tv_arrivaltime = itemView.findViewById(R.id.tv_arrivaltime);
            tv_arrivalbusstop = itemView.findViewById(R.id.tv_arrivalbusstop);
            iv_star = itemView.findViewById(R.id.iv_star);

            iv_star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    i = 1 - i;
                    if (i == 0) {
                        iv_star.setImageResource(R.drawable.star_filled);
                        //북마크에 추가
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                BookmarkDB.getInstance(itemView.getContext()).bookmarkDao().insert((BookmarkEntity) getItems().get(position));
                            }
                        }).start();
                    }
                    else{
                        iv_star.setImageResource(R.drawable.star_outlined);
                        //북마크데이터에서 삭제
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                BookmarkDB.getInstance(itemView.getContext()).bookmarkDao().delete((BookmarkEntity) getItems().get(position));
                            }
                        }).start();
                    }
                }
            });
        }
    }

    public void setItem(List<BookmarkEntity> data) {
        bookmarkEntities = data;
        notifyDataSetChanged();
    }

    public void countDown(String minutes, String seconds, MyViewHolder holder) {

        long conversionTime = 0;

        // 변환시간
        conversionTime = Long.valueOf(minutes) * 60 * 1000 + Long.valueOf(seconds) * 1000;

        new CountDownTimer(conversionTime, 1000) {

            // 특정 시간마다 뷰 변경
            public void onTick(long millisUntilFinished) {

                // 분단위
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000));
                String min = String.valueOf(getMin / (60 * 1000)); // 몫

                // 초단위
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // 나머지

                holder.tv_arrivaltime.setText(min+"분 "+second+"초");
//                count_view.setText(hour + ":" + min + ":" + second);
            }

            // 제한시간 종료시
            public void onFinish() {
            }
        }.start();
    }
}
