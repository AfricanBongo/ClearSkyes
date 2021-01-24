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
import com.africanbongo.clearskyes.model.weather.WeatherDay;

/**
 * Custom view for holding minimal {@link com.africanbongo.clearskyes.model.weather.WeatherDay} data
 */
public class DayWeatherViewUp extends ConstraintLayout {

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
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater
                .from(getContext())
                .inflate(R.layout.dayweather_view_up, this, true);

        dayAvgTempView = layout.findViewById(R.id.day_avg_temp);
        dayConditionText = layout.findViewById(R.id.day_condition_text);
        dayMaxMinTemp = layout.findViewById(R.id.day_max_min_temp);
        dayUVIndex = layout.findViewById(R.id.day_uv_index);
        dayWeatherIcon = layout.findViewById(R.id.weather_day_image);
    }

    /**
     * Load weather info into this custom view
     * @param day {@link WeatherDay} object where its members will be used to populate this view
     */
    public void loadData(WeatherDay day) {
        String conditionText = day.getConditions().getConditionText();

        setAvgTemp(day.getAvgTemp().getTempC());
        setDayUVIndex(day.getUvLevel());
        setMaxAndMinTemp(day.getMaxTemp().getTempC(),
                day.getMinTemp().getTempC());
        setConditionText(conditionText);

        day.getConditions().loadConditionImage(dayWeatherIcon);
    }

    private void setAvgTemp(double temp) {
        String temperature = temp + "°";
        dayAvgTempView.setText(temperature);
    }

    private void setMaxAndMinTemp(double maxTemp, double minTemp) {
        String temps = maxTemp + "°" + " / " + minTemp + "°";
        dayMaxMinTemp.setText(temps);
    }

    private void setConditionText(String conditionString) {
        dayConditionText.setText(conditionString);
    }

    private void setDayUVIndex(String uvIndex) {
        dayUVIndex.setText(uvIndex);
    }
}
