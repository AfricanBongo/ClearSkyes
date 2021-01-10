package com.africanbongo.clearskyes.controller.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherHour;
public class WeatherHoursRecyclerViewAdapter extends RecyclerView.Adapter<WeatherHoursRecyclerViewAdapter.WeatherHourViewHolder> {

    private final WeatherHour[] weatherHours;

    public WeatherHoursRecyclerViewAdapter(WeatherHour[] weatherHours) {
        this.weatherHours = weatherHours;
    }

    @Override
    public WeatherHourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_hour_view, parent, false);
        return new WeatherHourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WeatherHourViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return weatherHours.length;
    }

    public class WeatherHourViewHolder extends RecyclerView.ViewHolder {

        public WeatherHourViewHolder(View view) {
            super(view);
        }

    }
}