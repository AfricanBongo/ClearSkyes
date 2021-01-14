package com.africanbongo.clearskyes.controller.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.africanbongo.clearskyes.R;

import static com.africanbongo.clearskyes.model.MiscMethods.getUVLevel;

/*
Custom view that shows the current hour weather details
Credit to: https://stackoverflow.com/questions/39961044/custom-view-with-two-textviews
 */
public class CurrentWeatherViewUp extends ConstraintLayout {

    private ConstraintLayout constraintLayout;

    private TextView nowTemp;
    private TextView feelsLikeTemp;
    private TextView uvIndex;
    private TextView conditionText;
    private TextView chanceOfRain;
    private Context context;

    // Where the current weather icon is to be contained
    private ImageView iconImageView;

    public CurrentWeatherViewUp(@NonNull Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public CurrentWeatherViewUp(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public CurrentWeatherViewUp(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    public CurrentWeatherViewUp(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // Inflate the xml and attach to this class
        constraintLayout =
                (ConstraintLayout) LayoutInflater
                        .from(context)
                        .inflate(R.layout.currentweather_view_up, this, true);

        // Get the views within the view group
        nowTemp = constraintLayout.findViewById(R.id.now_temp);
        feelsLikeTemp = constraintLayout.findViewById(R.id.feels_like_temp);
        uvIndex = constraintLayout.findViewById(R.id.now_uv_index);
        conditionText = constraintLayout.findViewById(R.id.now_condition_text);
        chanceOfRain = constraintLayout.findViewById(R.id.chance_of_rain);

        iconImageView = constraintLayout.findViewById(R.id.weather_now_image);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CurrentWeatherViewUp);

        int currentTemp = a.getInteger(R.styleable.CurrentWeatherViewUp_current_temp, 0);
        int feelsTemp = a.getInteger(R.styleable.CurrentWeatherViewUp_feels_like_temp, 0);
        int sunIndex = a.getInteger(R.styleable.CurrentWeatherViewUp_uv_index, 0);
        String condText = a.getString(R.styleable.CurrentWeatherViewUp_condition_text);
        int chanceORain = a.getInteger(R.styleable.CurrentWeatherViewUp_chance_of_rain, 0);

        setNowTemp(currentTemp);
        setFeelsLikeTemp(feelsTemp);
        setUvIndex(getUVLevel(sunIndex));
        setChanceOfRain(chanceORain);
        setConditionText(condText);

        a.recycle();
    }

    public void setNowTemp(int temperature) {
        nowTemp.setText(temperature + "°");
    }

    public void setFeelsLikeTemp(int temperature) {
        feelsLikeTemp.setText("Feels like " + temperature + "°");
    }

    public void setUvIndex(String UVIndex) {
        uvIndex.setText(UVIndex);
    }

    public void setConditionText(String weatherCondition) {
        conditionText.setText(weatherCondition);
    }

    public void setChanceOfRain(int rainPercentageChance) {
        chanceOfRain.setText(rainPercentageChance + "%");
    }

    public ImageView getIconImageView() {
        return iconImageView;
    }
}
