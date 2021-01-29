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
import com.africanbongo.clearskyes.model.weather.WeatherToday;

/*
Custom view that shows the current hour weather details
Credit to: https://stackoverflow.com/questions/39961044/custom-view-with-two-textviews
 */
public class CurrentWeatherViewUp extends ConstraintLayout {

    private TextView nowTemp;
    private TextView feelsLikeTemp;
    private TextView uvIndex;
    private TextView conditionText;
    private TextView chanceOfRain;
    private final Context context;

    // Where the current weather icon is to be contained
    private ImageView iconImageView;

    public CurrentWeatherViewUp(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CurrentWeatherViewUp(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CurrentWeatherViewUp(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public CurrentWeatherViewUp(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes, Context context1) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context1;
        init();
    }

    private void init() {
        // Inflate the xml and attach to this class
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater
                .from(context)
                .inflate(R.layout.currentweather_view_up, this, true);

        // Get the views within the view group
        nowTemp = constraintLayout.findViewById(R.id.now_temp);
        feelsLikeTemp = constraintLayout.findViewById(R.id.feels_like_temp);
        uvIndex = constraintLayout.findViewById(R.id.now_uv_index);
        conditionText = constraintLayout.findViewById(R.id.now_condition_text);
        chanceOfRain = constraintLayout.findViewById(R.id.chance_of_rain);

        iconImageView = constraintLayout.findViewById(R.id.weather_now_image);
    }

    /**
     * Loads {@link WeatherToday} info into this custom view
     * @param today
     */
    public void loadData(WeatherToday today) {
        // Load ViewUp elements
        setUvIndex(today.getUvLevel());
        setFeelsLikeTemp((int) today.getNowWeather().getFeelsLikeTemp().getTempC());
        setNowTemp((int) today.getNowWeather().getActualTemp().getTempC());

        String conditionText = today.getNowWeather().getConditions().getConditionText();

        setConditionText(conditionText);

        // Load weather icon
        today.getNowWeather().getConditions().loadConditionImage(iconImageView);
        iconImageView.setContentDescription(conditionText);
    }

    public void setNowTemp(int temperature) {
        String temp = temperature + "°";
        nowTemp.setText(temp);
    }

    public void setFeelsLikeTemp(int temperature) {
        String temp = "Feels like " + temperature + "°";
        feelsLikeTemp.setText(temp);
    }

    public void setUvIndex(String UVIndex) {
        uvIndex.setText(UVIndex);
    }

    public void setConditionText(String weatherCondition) {
        conditionText.setText(weatherCondition);
    }

    public void setChanceOfRain(int rainPercentageChance) {
        String rain = rainPercentageChance + "%";
        chanceOfRain.setText(rain);
    }
}
