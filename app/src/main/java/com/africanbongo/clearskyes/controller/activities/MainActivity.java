package com.africanbongo.clearskyes.controller.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.adapters.WeatherDayStateAdapter;
import com.africanbongo.clearskyes.controller.animations.ZoomOutPageTransformer;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar mainToolbar;
    private ViewPager2 mainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        mainViewPager = findViewById(R.id.main_viewpager);

        mainViewPager.setAdapter(new WeatherDayStateAdapter(this));
        mainViewPager.setPageTransformer(new ZoomOutPageTransformer());
    }
}