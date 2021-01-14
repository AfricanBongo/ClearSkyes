package com.africanbongo.clearskyes.controller.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.africanbongo.clearskyes.R;

/**
 * Custom view for holding minimal {@link com.africanbongo.clearskyes.model.weatherobjects.WeatherDay} data
 */
public class DayWeatherViewUp extends ConstraintLayout {

    private ConstraintLayout layout;
    private TextView dayAvgTempView;
    private TextView dayMaxMinTemp;
    private TextView dayConditionText;
    private TextView dayUVIndex;

    private ImageView dayWeatherIcon;

    public DayWeatherViewUp(@NonNull Context context) {
        super(context);
        init();
    }

    public DayWeatherViewUp(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DayWeatherViewUp(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DayWeatherViewUp(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        layout =
                (ConstraintLayout) LayoutInflater
                .from(getContext())
                .inflate(R.layout.dayweather_view_up, this, true);

        dayAvgTempView = layout.findViewById(R.id.day_avg_temp);
        dayConditionText = layout.findViewById(R.id.day_condition_text);
        dayMaxMinTemp = layout.findViewById(R.id.day_max_min_temp);
        dayUVIndex = layout.findViewById(R.id.day_uv_index);
        dayWeatherIcon = layout.findViewById(R.id.weather_day_image);
    }

    public void setAvgTemp(double temp) {
        dayAvgTempView.setText(temp + "°");
    }

    public void setMaxAndMinTemp(double maxTemp, double minTemp) {
        dayMaxMinTemp.setText(maxTemp + "°" + " / " + minTemp + "°");
    }

    public void setConditionText(String conditionString) {
        dayConditionText.setText(conditionString);
    }

    public void setDayUVIndex(String uvIndex) {
        dayUVIndex.setText(uvIndex);
    }

    public ImageView getDayWeatherIcon() {
        return dayWeatherIcon;
    }
}
