package com.africanbongo.clearskyes.controller.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
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
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.navigation.NavigationView;

import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private CustomNavigationView navigationView;
    private MaterialButtonToggleGroup toggleGroup;
    private ViewPager2 mainViewPager;
    private View errorPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

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
        navigationView.setNavigationItemSelectedListener(this);

        // Set up view pager
        mainViewPager = findViewById(R.id.main_viewpager);

        // Load location buttons and last location open onto the navigation drawer
        toggleGroup = navigationView.getLocationsGroup();
        loadLocations();

        mainViewPager.setPageTransformer(new ZoomOutPageTransformer());
        errorPage = findViewById(R.id.warning_layout);
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
            WeatherDayStateAdapter presentAdapter = (WeatherDayStateAdapter) mainViewPager.getAdapter();

            if (presentAdapter != null) {
                String location = presentAdapter.getLocation();
                mainViewPager.setAdapter(new WeatherDayStateAdapter(this, location));
            }

            // Only switch the views if there's an internet connection
            // Otherwise the recurrence of the showError() method animation will overlap this one
            SwitchFadeAnimation animation = new SwitchFadeAnimation();
            animation.switchViews(errorPage, mainViewPager, SwitchFadeAnimation.NORMAL_DURATION);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    /**
     * Check if there any locations that had been saved
     * If present load the locations into the app
     * If not, prompt the user to add location(s)
     */
    public void loadLocations() {
        MaterialButton manageLocationsButton = navigationView.getManageLocationsButton();

        SharedPreferences locationPreferences = getSharedPreferences("locations", MODE_PRIVATE);
        Set<String> locations = locationPreferences.getStringSet("locationSet", null);

        if (locations != null) {
            // Loop through the set add a button to the toggle group
            locations.forEach(this::addLocationButton);

            int preferredLocation = locationPreferences.getInt("preferredLocation", -1);
            LocationButton locationButton;

            if (preferredLocation != -1) {
                locationButton = (LocationButton) toggleGroup.getChildAt(preferredLocation);
            } else {
                locationButton = (LocationButton) toggleGroup.getChildAt(0);
            }

            // Load in location and show the weather info for the location
            String location = locationButton.getLocation();
            locationButton.setChecked(true);
            mainViewPager.setAdapter(new WeatherDayStateAdapter(this, location));

        } else {
            // Open the drawer and prompt user to add a location
            drawerLayout.openDrawer(GravityCompat.START);

            // Animate the view to catch attention
            float blinkButtonAlpha = 0f;
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(manageLocationsButton, "alpha",
                    1f ,blinkButtonAlpha);

            alphaAnimator.setRepeatCount(5);
            alphaAnimator.setRepeatMode(ValueAnimator.REVERSE);
            alphaAnimator.setDuration(1500L).start();

            getSupportActionBar().setTitle("No location selected");
        }

        // If the manageLocationsButton is clicked open LocationsActivity
        manageLocationsButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, LocationsActivity.class);
            startActivity(intent);
        });
    }

    public void addLocationButton(final String location) {

        if (!location.isEmpty()) {
            LocationButton newButton = new LocationButton(this, location);

            // Add the button to the toggle group
            final int insertPos = toggleGroup.getChildCount();
            toggleGroup.addView(newButton, insertPos);

            // When the button is pressed change toolbar title and load new state adapter
            toggleGroup.addOnButtonCheckedListener(
                    (MaterialButtonToggleGroup group, int checkedId, boolean isChecked) -> {

                        if (isChecked) {
                            LocationButton checkedButton = (LocationButton) toggleGroup.getChildAt(insertPos);

                            String buttonLocation = checkedButton.getLocation();

                            // If this button is checked create new viewpager adapter
                            // Only if it doesn't already
                            if (checkedButton.isChecked()) {

                                WeatherDayStateAdapter currentAdapter =
                                        (WeatherDayStateAdapter) mainViewPager.getAdapter();

                                if (currentAdapter != null) {
                                    if (!currentAdapter.getLocation().equals(buttonLocation)) {

                                        // set adapter and close drawer
                                        mainViewPager.setAdapter(new WeatherDayStateAdapter(this, buttonLocation));
                                        getSupportActionBar().setTitle(buttonLocation);

                                    } else {
                                        checkedButton.setChecked(true);
                                    }
                                } else {
                                    mainViewPager.setAdapter(new WeatherDayStateAdapter(this, buttonLocation));
                                }


                                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                }
                            }
                        }
                    });

        }
    }
}