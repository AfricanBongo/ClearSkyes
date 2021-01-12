package com.africanbongo.clearskyes.controller.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherHour;

/**
 * Adapter for setting up and feeding the recycler view.
 * Using views holding data models from {@link WeatherHour} objects
 */
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
        WeatherHour hour = weatherHours[position];

        // Load image first
        hour
                .getConditions()
                .loadConditionImage(holder.hourWeatherImage);

        // Load other info
        holder.hourTime.setText(hour.getTime());

        String chanceOfRain = hour.getChanceOfRain() + "%";
        String temp = hour.getActualTemp().getTempC() + "°";

        holder.hourTemp.setText(temp);
        holder.hourChanceOfRain.setText(chanceOfRain);
    }

    @Override
    public int getItemCount() {
        return weatherHours.length;
    }

    public class WeatherHourViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout container;

        private final TextView hourTemp;
        private final TextView hourChanceOfRain;
        private final TextView hourTime;

        private final ImageView hourWeatherImage;



        public WeatherHourViewHolder(View view) {
            super(view);

            container = (RelativeLayout) view;
            hourTemp = container.findViewById(R.id.hour_temp);
            hourChanceOfRain = container.findViewById(R.id.hour_chance_of_rain);
            hourWeatherImage = container.findViewById(R.id.hour_weather_image);
            hourTime = container.findViewById(R.id.hour_time);
        }

    }
}