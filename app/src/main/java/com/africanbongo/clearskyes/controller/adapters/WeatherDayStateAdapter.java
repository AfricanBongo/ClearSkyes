package com.africanbongo.clearskyes.controller.adapters;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.africanbongo.clearskyes.controller.activities.MainActivity;
import com.africanbongo.clearskyes.controller.fragments.WeatherTodayFragment;

public class WeatherDayStateAdapter extends FragmentStateAdapter {

    MainActivity activity;

    public WeatherDayStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

        activity = (MainActivity) fragmentActivity;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new WeatherTodayFragment(activity);
    }

    @Override
    public int getItemCount() {
        // The days in a week
        return 7;
    }
}
