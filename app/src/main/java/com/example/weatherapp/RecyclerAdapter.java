package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    Double[] temperature;
    String[] time, weather;
    int[] ic_img;
    public RecyclerAdapter(Double[] temp, String[] time, String[] weather, int[] img){
        this.temperature = temp;
        this.time = time;
        this.weather = weather;
        this.ic_img = img;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView time, weather, temp;
        ImageView weather_icon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            weather = itemView.findViewById(R.id.weather);
            temp = itemView.findViewById(R.id.temperature);
            weather_icon = itemView.findViewById(R.id.weather_icon);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.weather_report_row, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        holder.time.setText(time[position]);
        holder.weather.setText(weather[position]);
        holder.temp.setText(String.valueOf(temperature[position]) +"\u2109");
        holder.weather_icon.setImageResource(ic_img[position]);
    }

    @Override
    public int getItemCount() {
        if(ic_img==null)return 0;
        return ic_img.length;
    }
}
