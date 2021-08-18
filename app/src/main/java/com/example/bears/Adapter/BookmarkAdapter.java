package com.example.bears.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bears.Activity.SearchResultActivity;
import com.example.bears.R;
import com.example.bears.Room.BookmarkDB;
import com.example.bears.Room.BookmarkEntity;
import com.example.bears.Utils.StationByUidItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.MyViewHolder> {
    Context context;
    static List<BookmarkEntity> bookmarkEntities = new ArrayList<>();
    String stationByUidUrl, BusStopServiceKey, busNum, stationId, stationName, nextStation, current_result, vehId1, seconds, minutes;
    public HashMap<String, String> StationByResultMap;
    String[] array;
    BookmarkDB bookmarkDB;
    ArrayList<String> arr_vehId1 = new ArrayList<>();
    ArrayList<String> arr_arrmsg1 = new ArrayList<>();
    CountDownTimer countDownTimer;

    public BookmarkAdapter(Context context, BookmarkDB bookmarkDB) {
        this.context = context;
        this.bookmarkDB = bookmarkDB;
    }

    @Override
    public int getItemCount() {
        return bookmarkEntities.size();
    }

    public static List<BookmarkEntity> getItems() {
        return bookmarkEntities;
    }

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
                        ((Activity) context).runOnUiThread(new Runnable() // start actions in UI thread
                        {
                            @Override
                            public void run() {
                                try {
                                    array = null;
                                    stationName = bookmarkEntities.get(position).getStationName();
                                    stationId = bookmarkEntities.get(position).getStationId();
                                    busNum = bookmarkEntities.get(position).getBusNum();

                                    Log.d("타임체인지 실행 : ", busNum);

                                    stationByUidUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?" +
                                            "serviceKey=" + BusStopServiceKey +
                                            "&arsId=" + stationId;

                                    StationByUidItem stationByUidItem = new StationByUidItem(stationByUidUrl, busNum);
                                    stationByUidItem.execute();

                                    StationByResultMap = stationByUidItem.get();

                                    try {
                                        current_result = StationByResultMap.get("arrmsg1");
                                        vehId1 = StationByResultMap.get("vehId1");

                                        arr_arrmsg1.add(current_result);
                                        arr_vehId1.add(vehId1);
                                    } catch (NullPointerException e) {
                                        Log.d("api 도착시간 널값", "arrmsg1 : " + current_result);
                                    }

                                    arr_arrmsg1.set(position, current_result);

                                    if (current_result.contains("[막차]")) {
                                        current_result = current_result.replaceAll("\\[막차\\] ", "");
                                    }
                                    if (current_result.equals("[차고지출발]")) {
                                        current_result = current_result.replaceAll("\\[", "").replaceAll("\\]", "");
                                        holder.tv_arrivaltime.setText(current_result);
                                        holder.tv_arrivalbusstop.setText("");
                                    } else if (current_result.equals("곧 도착") || current_result.equals("운행종료") || current_result.equals("출발대기")) {
                                        holder.tv_arrivaltime.setText(current_result);
                                        holder.tv_arrivalbusstop.setText("");
                                    } else {
                                        array = current_result.split("\\[");
                                        minutes = array[0].substring(0, current_result.indexOf("분"));

                                        if (current_result.contains("초"))
                                            seconds = array[0].substring(current_result.indexOf("분") + 1, current_result.indexOf("초"));
                                        else
                                            seconds = "0";

//                                        if (countDownTimer != null) {
//                                            countDownTimer.cancel();
//                                        }
                                        countDown(minutes, seconds, holder);
                                        holder.tv_arrivalbusstop.setText(array[1].substring(0, array[1].length() - 1));
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
        }
        ;

        // 메인 스레드에서 DB 접근 불가 -> 읽고 쓸 때 스레드 사용
        class SelectRunnable implements Runnable {
            @Override
            public void run() {
                for (int i = 0; i < bookmarkEntities.size(); i++) {
                    stationName = bookmarkEntities.get(position).getStationName();
                    stationId = bookmarkEntities.get(position).getStationId();
                    busNum = bookmarkEntities.get(position).getBusNum();
                    nextStation = bookmarkEntities.get(position).getNextStation();

                    holder.stationName.setText(stationName);
                    holder.stationId.setText(stationId);
                    holder.tv_busnum.setText(busNum);
                    holder.stationDirec.setText(nextStation + " 방면");
                    holder.iv_star.setImageResource(R.drawable.star_filled);
                }
            }
        }

        SelectRunnable selectRunnable = new SelectRunnable();
        Thread t = new Thread(selectRunnable);
        t.start();

        Timechange timechange = new Timechange();
        Thread tt = new Thread(timechange);
        tt.start();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(v.getContext(), SearchResultActivity.class);

                    intent.putExtra("busnumber", bookmarkEntities.get(position).getBusNum());
                    intent.putExtra("ars_Id", bookmarkEntities.get(position).getStationId());
                    intent.putExtra("stationName", bookmarkEntities.get(position).getStationName());
                    intent.putExtra("arrmsg1", arr_arrmsg1.get(position));
                    intent.putExtra("vehId1", arr_vehId1.get(position));
                    intent.putExtra("nextStation", bookmarkEntities.get(position).getNextStation());
                    v.getContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });

            iv_star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    iv_star.setImageResource(R.drawable.star_outlined);
                    //북마크데이터에서 삭제
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            BookmarkDB.getInstance(itemView.getContext()).bookmarkDao().delete((BookmarkEntity) getItems().get(position));
                        }
                    }).start();
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

        countDownTimer = new CountDownTimer(conversionTime, 1000) {

            // 특정 시간마다 뷰 변경
            public void onTick(long millisUntilFinished) {

                // 분단위
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000));
                String min = String.valueOf(getMin / (60 * 1000)); // 몫

                // 초단위
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // 나머지

                if (min.equals("0"))
                    holder.tv_arrivaltime.setText(second + "초 후 도착 예정");
                else holder.tv_arrivaltime.setText(min + "분 " + second + "초 후 도착 예정");
            }

            // 제한시간 종료시
            public void onFinish() {
            }
        };

        countDownTimer.start();
    }
}
