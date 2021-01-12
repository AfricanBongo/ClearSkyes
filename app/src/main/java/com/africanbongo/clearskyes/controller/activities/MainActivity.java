package com.africanbongo.clearskyes.controller.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.adapters.WeatherDayStateAdapter;
import com.africanbongo.clearskyes.controller.animations.ZoomOutPageTransformer;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar mainToolbar;
    private ViewPager2 mainViewPager;
    private View errorPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        mainViewPager = findViewById(R.id.main_viewpager);
        errorPage = findViewById(R.id.warning_layout);

        mainViewPager.setAdapter(new WeatherDayStateAdapter(this));
        mainViewPager.setPageTransformer(new ZoomOutPageTransformer());
    }

    /**
     * Hide the main view pager and show the error page
     * @return The {@link View} containing the error page
     */
    public View showError() {
        if (mainViewPager != null || !this.isDestroyed()) {
            mainViewPager.setVisibility(View.GONE);

            errorPage.setVisibility(View.VISIBLE);

            return errorPage;
        }
        return null;
    }
}