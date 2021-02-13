package com.africanbongo.clearskyes.controller.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.adapters.WeatherDayStateAdapter;
import com.africanbongo.clearskyes.controller.animations.SwitchFadeAnimation;
import com.africanbongo.clearskyes.controller.animations.ZoomOutPageTransformer;
import com.africanbongo.clearskyes.controller.customviews.CustomNavigationView;
import com.africanbongo.clearskyes.controller.customviews.LocationButton;
import com.africanbongo.clearskyes.controller.notification.NotificationReceiver;
import com.africanbongo.clearskyes.model.weather.WeatherLocation;
import com.africanbongo.clearskyes.model.weather.WeatherTemp;
import com.africanbongo.clearskyes.util.BackgroundTaskUtil;
import com.africanbongo.clearskyes.util.NotificationUtil;
import com.africanbongo.clearskyes.util.WeatherLocationUtil;
import com.africanbongo.clearskyes.util.WeatherTimeUtil;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements MaterialButtonToggleGroup.OnButtonCheckedListener, Reloadable {

    // View holding the tab and view pager layout
    private LinearLayout tabAndViewPagerParent;
    private DrawerLayout drawerLayout;
    private CustomNavigationView navigationView;
    private ViewPager2 mainViewPager;
    private View errorPage;

    // Values fetched from Settings Page
    private WeatherTemp.Degree degree;
    private int forecastDays;

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

        getSettings();
        // Load location buttons and last location open onto the navigation drawer
        initLocations();

        mainViewPager.setPageTransformer(new ZoomOutPageTransformer());
        errorPage = findViewById(R.id.warning_layout);
    }

    // Create a notification if enabled in settings
    public void createNotification(boolean notificationsOn, String time, WeatherTemp.Degree degree,
                                   @Nullable String favouriteLocation) {

        // Create intents to get notification broadcast receiver
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.setAction(NotificationUtil.NOTIFICATION_ACTION);
        intent.putExtra(NotificationUtil.NOTIFICATION_DEGREE, degree.getStringDegree());
        intent.putExtra(NotificationUtil.NOTIFICATION_LOCATION, favouriteLocation);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(),
                NotificationUtil.NOTIFICATION_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Get the alarm manager
        AlarmManager alarmManager = (AlarmManager)
                getSystemService(ALARM_SERVICE);

        // Fire the intent at the default or user specified time
        if (notificationsOn) {
            DateTimeFormatter normalFormatter =
                    DateTimeFormatter.ofPattern(WeatherTimeUtil.TIME_FORMAT);
            LocalTime notificationTime = LocalTime.parse(time, normalFormatter);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, notificationTime.getHour());
            calendar.set(Calendar.MINUTE, notificationTime.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            if (Calendar.getInstance().after(calendar)) {
                calendar.add(Calendar.DATE, 1);
            }

            alarmManager.cancel(alarmIntent);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);

        } else {
            // Cancel notifications if they're existing
            if (alarmIntent != null && alarmManager != null) {
                alarmManager.cancel(alarmIntent);
            }
        }
    }

    public void getSettings() {
        // Get values from settings page
        SharedPreferences preferences
                = PreferenceManager.getDefaultSharedPreferences(this);
        String tempKey = getString(R.string.degrees_key);
        String tempType = preferences.getString(tempKey, getString(R.string.degrees_default));
        String forecastDaysKey = getString(R.string.forecast_key);
        int forecastDefaultValue = getResources().getInteger(R.integer.forecast_default_value);
        forecastDays = preferences.getInt(forecastDaysKey, forecastDefaultValue);
        degree = WeatherTemp.Degree.getDegree(tempType);

        // Check if notifications on
        String notificationTimeKey = getString(R.string.notify_time_key);
        String notificationKey = getString(R.string.notify_show_key);
        String defaultTime = getString(R.string.notify_time_default);
        String favouriteLocation = preferences.getString(WeatherLocationUtil.SP_FAV_LOCATION, null);
        String time = preferences.getString(notificationTimeKey, defaultTime);
        boolean notificationsOn = preferences.getBoolean(notificationKey, true);

        // Create the notifications on a separate thread
        BackgroundTaskUtil.runTask(
                () -> createNotification(notificationsOn, time, degree, favouriteLocation)
        );

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


    @Override
    public View showError() {
        if (tabAndViewPagerParent != null || !this.isDestroyed()) {
            // Switch the view pager with the error page
            SwitchFadeAnimation animation = new SwitchFadeAnimation();
            animation.switchViews(tabAndViewPagerParent, errorPage, SwitchFadeAnimation.NORMAL_DURATION);

            return errorPage;
        }
        return null;
    }

    @Override
    public void reload() {
        if (mainViewPager != null || !this.isDestroyed()) {
            // Refresh the view pager
            WeatherDayStateAdapter presentAdapter = (WeatherDayStateAdapter) mainViewPager.getAdapter();

            if (presentAdapter != null) {
                String location = presentAdapter.getLocation();
                mainViewPager.setAdapter(new WeatherDayStateAdapter(this, location, degree, forecastDays));
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
            actionBarTitle = WeatherLocationUtil.NO_LOCATION_FOUND;
        }

        // Else load new adapter to view pager
        else {
            mainViewPager.setAdapter(new WeatherDayStateAdapter(this, location.getUrlLocation(), degree, forecastDays));
            actionBarTitle = WeatherLocationUtil.getLocationEmoticon() + location.getShortStringLocation();
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
            WeatherLocation activeLocation = checkedButton.getLocation();
            String buttonLocation = activeLocation.getUrlLocation();

            // If this button is checked create new viewpager adapter
            // Only if it doesn't already
            if (checkedButton.isChecked()) {

                WeatherDayStateAdapter currentAdapter =
                        (WeatherDayStateAdapter) mainViewPager.getAdapter();

                if (currentAdapter != null) {
                    if (!currentAdapter.getLocation().equals(buttonLocation)) {

                        // set adapter and close drawer
                        mainViewPager.setAdapter(new WeatherDayStateAdapter(this, buttonLocation, degree, forecastDays));

                        String actionTitle = WeatherLocationUtil.getLocationEmoticon() + checkedButton.getText();
                        getSupportActionBar().setTitle(actionTitle);
                    }
                } else {
                    mainViewPager.setAdapter(new WeatherDayStateAdapter(this, buttonLocation, degree, forecastDays));
                }

                // Save the location as the active location
                SharedPreferences preferences =
                        PreferenceManager.getDefaultSharedPreferences(this);

                preferences
                        .edit()
                        .putString(WeatherLocationUtil.SP_ACTIVE_LOCATION, WeatherLocationUtil.serialize(activeLocation))
                        .apply();
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        }
    }
}