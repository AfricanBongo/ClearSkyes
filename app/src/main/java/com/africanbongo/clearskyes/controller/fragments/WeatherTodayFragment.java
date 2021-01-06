package com.africanbongo.clearskyes.controller.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.model.weatherobjects.CollectionWeatherDay;

/*
Fragment containing today's weather
 */
public class WeatherTodayFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        requestData(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_today, container, false);
    }

    public void requestData(Context context) {

    }
}