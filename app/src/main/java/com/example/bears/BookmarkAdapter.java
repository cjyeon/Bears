package com.example.bears;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bears.Room.BookmarkDB;
import com.example.bears.Room.BookmarkDao;
import com.example.bears.Room.BookmarkEntity;

import java.util.ArrayList;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.MyViewHolder> {
    Context context;
    static List<BookmarkEntity> bookmarkEntities = new ArrayList<>();
    private BookmarkDB bookmarkDB;

//    BookmarkDB bookmarkDB = BookmarkDB.getInstance(context);
//    BookmarkDao bookmarkDao = bookmarkDB.bookmarkDao();
    int i = 0;

    public BookmarkAdapter(BookmarkDB bookmarkDB) {
        this.bookmarkDB = bookmarkDB;
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
//        BookmarkDB bookmarkDB = BookmarkDB.getInstance(context);
//        BookmarkDao bookmarkDao = bookmarkDB.bookmarkDao();

        // 메인 스레드에서 DB 접근 불가 -> 읽고 쓸 때 스레드 사용
        class SelectRunnable implements Runnable {
            @Override
            public void run() {
//                bookmarkEntities = BookmarkDao.getAll();

                for (i = 0; i < bookmarkEntities.size(); i++) {
                    holder.stationName.setText(bookmarkEntities.get(position).getStationName());
                    holder.stationId.setText(bookmarkEntities.get(position).getStationId());
                    holder.tv_busnum.setText(bookmarkEntities.get(position).getBusNum());
                    Log.d("onCreate: getAll() : " , bookmarkEntities.get(position).getBusNum());
                }
            }
        }
        SelectRunnable selectRunnable = new SelectRunnable();
        Thread t = new Thread(selectRunnable);
        t.start();
//        holder.stationName.setText(bookmarkData.get(position).getStationName());
//        holder.stationId.setText(bookmarkData.get(position).getStationId());
//        holder.stationDirec.setText(bookmarkData.get(position).getStationDirec());
//        holder.tv_busnum.setText(bookmarkData.get(position).getBusnum());
//        holder.tv_arrivaltime.setText(bookmarkData.get(position).getArrival_time());
//        holder.tv_arrivalbusstop.setText(bookmarkData.get(position).getArrival_busstop());

//        if(bookmarkData.get(position).isStar() == true){
//            holder.iv_star.setImageResource(R.drawable.star_filled);
//        }
//        else{
//            holder.iv_star.setImageResource(R.drawable.star_outlined);
//        }
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
//                        if (bookmarkData.get(position).isStar() == true) {
                        iv_star.setImageResource(R.drawable.star_filled);
                        //북마크에 추가
                    }
                    else{
                        iv_star.setImageResource(R.drawable.star_outlined);
//                        bookmarkData.get(position).setStar(false);
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
}
