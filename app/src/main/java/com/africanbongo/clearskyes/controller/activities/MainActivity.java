package com.africanbongo.clearskyes.controller.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.adapters.WeatherDayStateAdapter;
import com.africanbongo.clearskyes.controller.animations.SwitchFadeAnimation;
import com.africanbongo.clearskyes.controller.animations.ZoomOutPageTransformer;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager2 mainViewPager;
    private View errorPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        // Set up navigation drawer
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this, drawerLayout, mainToolbar,
                        R.string.open_drawer, R.string.close_drawer
                );


        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set up view pager
        mainViewPager = findViewById(R.id.main_viewpager);
        errorPage = findViewById(R.id.warning_layout);

        mainViewPager.setAdapter(new WeatherDayStateAdapter(this));
        mainViewPager.setPageTransformer(new ZoomOutPageTransformer());

        // Load up credit image to WeatherAPI
        ImageView creditImage =
                navigationView
                        .findViewById(R.id.poweredby_view)
                        .findViewById(R.id.credit_image);

        Glide
                .with(this)
                .load(R.drawable.weatherapi_logo)
                .into(creditImage);

        // Open site upon clicking image
        creditImage.setOnClickListener(e -> {
            String websiteURL = "https://www.weatherapi.com";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteURL));
            startActivity(intent);
        });
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
            if (mainViewPager.getCurrentItem() != 0) {
                mainViewPager.setCurrentItem(mainViewPager.getCurrentItem() - 1);
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}