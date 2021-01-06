package com.africanbongo.clearskyes.controller.adapters;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.africanbongo.clearskyes.controller.fragments.WeatherDayFragment;

import java.time.LocalDateTime;

public class WeatherDayStateAdapter extends FragmentStateAdapter {

    public WeatherDayStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        LocalDateTime now = LocalDateTime.now();
        // TODO Write to select fragment
        return new WeatherDayFragment(position);
    }

    @Override
    public int getItemCount() {
        // The days in a week
        return 7;
    }
}
