package com.africanbongo.clearskyes.controller.customviews;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.model.WeatherTime;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
Custom view shown on current today page at the bottom of the screen
Contains the animated sunset, sunrise, moon set, and moonrise icons
As well as text views to show the times of theses properties
 */
public class CurrentWeatherViewDown extends GridLayout {

    private GridLayout layout;
    private ImageView moonRiseImage;
    private ImageView moonSetImage;
    private ImageView sunRiseImage;
    private ImageView sunSetImage;
    private TextView moonRise;
    private TextView moonSet;
    private TextView sunRise;
    private TextView sunSet;

    private Context context;

    public CurrentWeatherViewDown(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public CurrentWeatherViewDown(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public CurrentWeatherViewDown(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    public CurrentWeatherViewDown(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        // Inflate the xml and attach to this class
        layout =
                (GridLayout) LayoutInflater
                        .from(context)
                        .inflate(R.layout.currentweather_view_down, this, true);

        // Get the views within the view group
        moonRiseImage = layout.findViewById(R.id.now_moon_rise_image);
        moonSetImage = layout.findViewById(R.id.now_moon_set_image);
        sunRiseImage = layout.findViewById(R.id.now_sun_rise_image);
        sunSetImage = layout.findViewById(R.id.now_sun_set_image);

        moonRise = layout.findViewById(R.id.now_moon_rise);
        moonSet = layout.findViewById(R.id.now_moon_set);
        sunSet = layout.findViewById(R.id.now_sun_set);
        sunRise = layout.findViewById(R.id.now_sun_rise);

        // Assign tooltips to the image views
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            moonRiseImage.setTooltipText("Moonrise");
            moonSetImage.setTooltipText("Moon set");
            sunRiseImage.setTooltipText("Sunrise");
            sunSetImage.setTooltipText("Sunset");
        }

        // Start animated vectors
        AnimatedVectorDrawable avdMoonRise = (AnimatedVectorDrawable) moonRiseImage.getDrawable();
        AnimatedVectorDrawable avdMoonSet = (AnimatedVectorDrawable) moonSetImage.getDrawable();
        AnimatedVectorDrawable avdSunRise = (AnimatedVectorDrawable) sunRiseImage.getDrawable();
        AnimatedVectorDrawable avdSunSet = (AnimatedVectorDrawable) sunSetImage.getDrawable();
        avdMoonRise.start();
        avdMoonSet.start();
        avdSunRise.start();
        avdSunSet.start();

    }

    public void setMoonRise(@NonNull String time) {
        moonRise.setText(time);
    }

    public void setMoonSet(@NonNull String time) {
        moonSet.setText(time);
    }

    public void setSunSet(@NonNull String time) {
        sunSet.setText(time);
    }

    public void setSunRise(@NonNull String time) {
        sunRise.setText(time);
    }
}
