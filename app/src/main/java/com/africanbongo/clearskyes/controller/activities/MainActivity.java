package com.africanbongo.clearskyes.controller.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.adapters.WeatherDayStateAdapter;
import com.africanbongo.clearskyes.controller.animations.SwitchFadeAnimation;
import com.africanbongo.clearskyes.controller.animations.ZoomOutPageTransformer;
import com.africanbongo.clearskyes.controller.customviews.CustomNavigationView;
import com.africanbongo.clearskyes.controller.customviews.LocationButton;
import com.africanbongo.clearskyes.model.weather.WeatherLocation;
import com.africanbongo.clearskyes.model.weatherapi.util.LocationUtil;
import com.africanbongo.clearskyes.model.weatherapi.util.WeatherTimeUtil;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity
        implements MaterialButtonToggleGroup.OnButtonCheckedListener {

    // View holding the tab and view pager layout
    private LinearLayout tabAndViewPagerParent;

    private DrawerLayout drawerLayout;
    private CustomNavigationView navigationView;
    private ViewPager2 mainViewPager;
    private View errorPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        tabAndViewPagerParent = findViewById(R.id.tab_viewpager_layout);

        // Set up navigation drawer
        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this, drawerLayout, mainToolbar,
                        R.string.open_drawer, R.string.close_drawer
                );

        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        mainViewPager = findViewById(R.id.main_viewpager);

        // Load location buttons and last location open onto the navigation drawer
        initLocations();

        mainViewPager.setPageTransformer(new ZoomOutPageTransformer());
        errorPage = findViewById(R.id.warning_layout);
    }

    // Attach the tab layout to the view pager
    // Only when the view pager has info
    public void attachTabAndPager() {
        if (mainViewPager != null) {
            TabLayout tabLayout = findViewById(R.id.main_tab_layout);
            new TabLayoutMediator(tabLayout, mainViewPager,
                    ((tab, position) -> {
                        LocalDate date = LocalDate.now().plusDays(position);
                        tab.setText(WeatherTimeUtil.getRelativeDay(date));
                    })
            ).attach();
        }
    }

    /**
     * Hide the main view pager and show the error page
     * @return The {@link View} containing the error page
     */
    public View showError() {
        if (tabAndViewPagerParent != null || !this.isDestroyed()) {
            // Switch the view pager with the error page
            SwitchFadeAnimation animation = new SwitchFadeAnimation();
            animation.switchViews(tabAndViewPagerParent, errorPage, SwitchFadeAnimation.NORMAL_DURATION);

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
            WeatherDayStateAdapter presentAdapter = (WeatherDayStateAdapter) mainViewPager.getAdapter();

            if (presentAdapter != null) {
                String location = presentAdapter.getLocation();
                mainViewPager.setAdapter(new WeatherDayStateAdapter(this, location));
                attachTabAndPager();
            }

            // Only switch the views if there's an internet connection
            // Otherwise the recurrence of the showError() method animation will overlap this one
            SwitchFadeAnimation animation = new SwitchFadeAnimation();
            animation.switchViews(errorPage, tabAndViewPagerParent, SwitchFadeAnimation.NORMAL_DURATION);
        }
    }

    @Override
    public void onBackPressed() {
        if (mainViewPager.getAdapter() != null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            }
            else if (mainViewPager.getCurrentItem() != 0) {
                mainViewPager.setCurrentItem(mainViewPager.getCurrentItem() - 1);
                return;
            }
        }

        super.onBackPressed();
    }

    /**
     * Check if there any locations that had been saved
     * If present load the locations into the app
     * If not, prompt the user to add location(s)
     */
    public void initLocations() {
        WeatherLocation location = navigationView.loadLocations();
        String actionBarTitle;

        // Prompt the user to add new locations
        if (location == null) {
            navigationView.promptForLocations(drawerLayout, true);
            actionBarTitle = LocationUtil.NO_LOCATION_FOUND;
        }

        // Else load new adapter to view pager
        else {
            mainViewPager.setAdapter(new WeatherDayStateAdapter(this, location.getUrlLocation()));
            actionBarTitle = LocationUtil.PUSHPIN_EMOJI + location.getShortStringLocation();
            attachTabAndPager();
        }

        getSupportActionBar().setTitle(actionBarTitle);

        // When the button is pressed change toolbar title and load new state adapter
        navigationView.addOnLocationButtonCheckedListener(this);
    }

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (isChecked) {

            LocationButton checkedButton = navigationView.findViewById(checkedId);
            String buttonLocation = checkedButton.getLocation().getUrlLocation();

            // If this button is checked create new viewpager adapter
            // Only if it doesn't already
            if (checkedButton.isChecked()) {

                WeatherDayStateAdapter currentAdapter =
                        (WeatherDayStateAdapter) mainViewPager.getAdapter();

                if (currentAdapter != null) {
                    if (!currentAdapter.getLocation().equals(buttonLocation)) {

                        // set adapter and close drawer
                        mainViewPager.setAdapter(new WeatherDayStateAdapter(this, buttonLocation));

                        String actionTitle = LocationUtil.PUSHPIN_EMOJI + checkedButton.getText();
                        getSupportActionBar().setTitle(actionTitle);
                    }
                } else {
                    mainViewPager.setAdapter(new WeatherDayStateAdapter(this, buttonLocation));
                }

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        }
    }
}