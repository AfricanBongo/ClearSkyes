package com.africanbongo.clearskyes.controller.adapters;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.africanbongo.clearskyes.controller.fragments.WeatherDayFragment;
import com.africanbongo.clearskyes.controller.fragments.WeatherTodayFragment;
import com.africanbongo.clearskyes.model.WeatherTemp;

public class WeatherDayStateAdapter extends FragmentStateAdapter {
    private final String location;
    private final WeatherTemp.Degree degree;
    private final int forecastDays;

    public WeatherDayStateAdapter(@NonNull FragmentActivity fragmentActivity, String location, WeatherTemp.Degree degree, int forecastDays) {
        super(fragmentActivity);
        this.location = location;
        this.degree = degree;
        this.forecastDays = forecastDays;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 0) {
            return WeatherTodayFragment.newInstance(location, degree);
        } else {
            return WeatherDayFragment.newInstance(location, position, degree);
        }
    }

    @Override
    public int getItemCount() {
        // The days in a week
        return forecastDays + 1;
    }

    /**
     * Get the location of the weather data associated with this button
     * @return {@link String} location eg. Harare
     */
    @NonNull
    public String getLocation() {
        return location;
    }


}
