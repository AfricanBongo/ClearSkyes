package com.africanbongo.clearskyes.controller.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.adapters.WeatherDayStateAdapter;
import com.africanbongo.clearskyes.controller.animations.SwitchFadeAnimation;
import com.africanbongo.clearskyes.controller.animations.ZoomOutPageTransformer;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 mainViewPager;
    private View errorPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar mainToolbar = findViewById(R.id.main_toolbar);
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
            // Switch the view pager with the error page
            SwitchFadeAnimation animation = new SwitchFadeAnimation();
            animation.switchViews(mainViewPager, errorPage, SwitchFadeAnimation.NORMAL_DURATION);

            return errorPage;
        }
        return null;
    }

    /**
     * Reload the view pager contents and hide error page
     */
    public void reloadViewPager() {
        if (mainViewPager != null || !this.isDestroyed()) {
            // Refresh the view pager
            mainViewPager.setAdapter(new WeatherDayStateAdapter(this));

            // Only switch the views if there's an internet connection
            // Otherwise the recurrence of the showError() method animation will overlap this one
            SwitchFadeAnimation animation = new SwitchFadeAnimation();
            animation.switchViews(errorPage, mainViewPager, SwitchFadeAnimation.NORMAL_DURATION);
        }
    }

    @Override
    public void onBackPressed() {
        if (mainViewPager.getAdapter() != null) {
            mainViewPager.setCurrentItem(0);
        }
    }
}