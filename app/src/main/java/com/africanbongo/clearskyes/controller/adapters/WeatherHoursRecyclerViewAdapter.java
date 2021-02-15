package com.africanbongo.clearskyes.controller.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.activities.WeatherDetailActivity;
import com.africanbongo.clearskyes.model.WeatherHour;
import com.africanbongo.clearskyes.model.WeatherTemp;
import com.africanbongo.clearskyes.util.WeatherJsonUtil;
import com.africanbongo.clearskyes.util.WeatherTimeUtil;

/**
 * Adapter for setting up and feeding the recycler view.
 * Using views holding data models from {@link WeatherHour} objects
 */
public class WeatherHoursRecyclerViewAdapter extends RecyclerView.Adapter<WeatherHoursRecyclerViewAdapter.WeatherHourViewHolder> {

    private final FragmentActivity activity;
    private final WeatherHour[] weatherHours;
    private final WeatherTemp.Degree degreesType;

    public WeatherHoursRecyclerViewAdapter(FragmentActivity activity, WeatherHour[] weatherHours, WeatherTemp.Degree degreesType) {
        this.activity = activity;
        this.weatherHours = weatherHours;
        this.degreesType = degreesType;
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
        String temp = hour.getActualTemp().getTemp(degreesType) + WeatherTemp.Degree.DEGREE_SIGN;

        holder.hourTemp.setText(temp);
        holder.hourChanceOfRain.setText(chanceOfRain);

        // When the view is clicked open the WeatherDetailActivity
        holder.container.setOnClickListener(l -> {
            String imageTransitionName =
                    activity.getString(R.string.weather_detail_image_transition);
            String tempTransitionName =
                    activity.getString(R.string.temp_text_transition);
            String timeTransitionName =
                    activity.getString(R.string.time_text_transition);
            Intent intent = new Intent(activity, WeatherDetailActivity.class);
            intent.setAction(WeatherHour.INTENT_ACTION);
            intent.putExtra(WeatherJsonUtil.INTENT_EXTRA, hour);
            intent.putExtra(WeatherTimeUtil.INTENT_EXTRA, hour.getTime());
            ActivityOptionsCompat optionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                            activity,
                            Pair.create(holder.hourWeatherImage, imageTransitionName),
                            Pair.create(holder.hourTemp, tempTransitionName),
                            Pair.create(holder.hourTime, timeTransitionName)
                    );
            ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
        });
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