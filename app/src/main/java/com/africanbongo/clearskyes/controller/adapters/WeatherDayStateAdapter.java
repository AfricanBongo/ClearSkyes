package com.africanbongo.clearskyes.controller.adapters;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.africanbongo.clearskyes.controller.activities.MainActivity;
import com.africanbongo.clearskyes.controller.fragments.WeatherDayFragment;
import com.africanbongo.clearskyes.controller.fragments.WeatherTodayFragment;
import com.africanbongo.clearskyes.model.weather.WeatherTemp;

public class WeatherDayStateAdapter extends FragmentStateAdapter {

    MainActivity activity;
    private final String location;
    private WeatherTemp.Degree degree;

    public WeatherDayStateAdapter(@NonNull FragmentActivity fragmentActivity, String location, WeatherTemp.Degree degree) {
        super(fragmentActivity);
        activity = (MainActivity) fragmentActivity;
        this.location = location;
        this.degree = degree;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 0) {
            return WeatherTodayFragment.newInstance(activity, location, degree);
        } else {
            return WeatherDayFragment.newInstance(activity, location, position, degree);
        }
    }

    @Override
    public int getItemCount() {
        // The days in a week
        return 8;
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
