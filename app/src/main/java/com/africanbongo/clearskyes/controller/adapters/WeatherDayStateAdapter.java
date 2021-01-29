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

public class WeatherDayStateAdapter extends FragmentStateAdapter {

    MainActivity activity;
    private final String location;

    public WeatherDayStateAdapter(@NonNull FragmentActivity fragmentActivity, String location) {
        super(fragmentActivity);

        activity = (MainActivity) fragmentActivity;
        this.location = location;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 0) {
            return WeatherTodayFragment.newInstance(activity, location);
        } else {
            return WeatherDayFragment.newInstance(activity, location, position);
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
