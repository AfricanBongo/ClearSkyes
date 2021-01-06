package com.africanbongo.clearskyes.controller.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.customviews.CurrentWeatherViewUp;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherCondition;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar mainToolbar;
    private CurrentWeatherViewUp weatherViewUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherViewUp = findViewById(R.id.now_weatherview_up);
        mainToolbar = findViewById(R.id.main_toolbar);

        WeatherCondition condition =
                new WeatherCondition("Cloudy day", null, 1000, true);

        condition.loadConditionImage(weatherViewUp.getIconImageView());

        Drawable drawable = weatherViewUp.getIconImageView().getDrawable();

        if (drawable instanceof AnimatedVectorDrawable) {
            ((AnimatedVectorDrawable) drawable).start();
        }
        
        setSupportActionBar(mainToolbar);
    }
}