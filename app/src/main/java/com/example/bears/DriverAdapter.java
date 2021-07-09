package com.example.bears;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.MyViewHolder> {
    Context context;
    static ArrayList<DriverData> driverData;

    public DriverAdapter(ArrayList<DriverData> driverData) {
        this.driverData = driverData;
    }

    @NonNull
    @Override
    public DriverAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_driver, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DriverAdapter.MyViewHolder holder, int position) {
        holder.tv_busstop.setText(driverData.get(position).getBusstop());
    }

    @Override
    public int getItemCount() {
        return driverData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_busstop;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_busstop = itemView.findViewById(R.id.tv_busstop);
        }
    }
}

