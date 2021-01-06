package com.africanbongo.clearskyes.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.africanbongo.clearskyes.R;

public class WeatherDayFragment extends Fragment {

    // The day of the week to display
    String day;

    public WeatherDayFragment(int dayOfTheWeek) {
        // Retrieve the day of the week
        day = getStringDayOfTheWeek(dayOfTheWeek);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_day, container, false);

        // Set the day to display
        TextView dayText = view.findViewById(R.id.day_textview);
        dayText.setText(day);

        return view;
    }

    public static String getStringDayOfTheWeek(int dayOfTheWeek) {
        String day;

        switch (dayOfTheWeek) {
            case 0: day = "Sunday"; break;
            case 1: day = "Monday"; break;
            case 2: day = "Tuesday"; break;
            case 3: day = "Wednesday"; break;
            case 4: day = "Thursday"; break;
            case 5: day = "Friday"; break;
            case 6: day = "Saturday"; break;

            default: day = null;
        }

        return day;
    }
}