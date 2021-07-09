package com.example.bears;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.MyViewHolder> {
    Context context;
    static ArrayList<BookmarkData> bookmarkData;

    public BookmarkAdapter(ArrayList<BookmarkData> bookmarkData) {
        this.bookmarkData = bookmarkData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_bookmark, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_busnum.setText(bookmarkData.get(position).getBusnum());
        holder.tv_arrivaltime.setText(bookmarkData.get(position).getArrival_time());
        holder.tv_arrivalbusstop.setText(bookmarkData.get(position).getArrival_busstop());
        if(bookmarkData.get(position).isStar() == true){
            holder.iv_star.setImageResource(R.drawable.star_filled);
        }
        else{
            holder.iv_star.setImageResource(R.drawable.star_outlined);
        }
    }

    @Override
    public int getItemCount() {
        return bookmarkData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_busnum;
        private TextView tv_arrivaltime;
        private TextView tv_arrivalbusstop;
        private ImageView iv_star;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_busnum = itemView.findViewById(R.id.tv_busnum);
            tv_arrivaltime = itemView.findViewById(R.id.tv_arrivaltime);
            tv_arrivalbusstop = itemView.findViewById(R.id.tv_arrivalbusstop);
            iv_star = itemView.findViewById(R.id.iv_star);

            iv_star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (bookmarkData.get(position).isStar() == true) {
                        iv_star.setImageResource(R.drawable.star_filled);
                        //북마크에 추가
                    }
                    else{
                        iv_star.setImageResource(R.drawable.star_outlined);
                        bookmarkData.get(position).setStar(false);
                        //북마크데이터에서 삭제
                    }
                }
            });
        }
    }
}
