package com.africanbongo.clearskyes.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherHour;

import java.time.LocalTime;


/**
 * A fragment representing a list of
 * {@link com.africanbongo.clearskyes.controller.fragments.WeatherHoursRecyclerViewAdapter.WeatherHourViewHolder} objects.
 */
public class WeatherHoursFragment extends Fragment {

    private static WeatherHour[] hours;

    public WeatherHoursFragment() {

    }

    public static WeatherHoursFragment newInstance(WeatherHour[] weatherHours) {
        hours = weatherHours;
        Bundle args = new Bundle();
        WeatherHoursFragment fragment = new WeatherHoursFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_hours_list, container, false);

        // Set the adapter
        if (view instanceof FrameLayout) {
            Context context = view.getContext();
            RecyclerView recyclerView = view.findViewById(R.id.now_weather_hours_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(new WeatherHoursRecyclerViewAdapter(hours));

            // Set the divider of the recycler view
            DividerItemDecoration divider =
                    new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);

            divider.setDrawable(ResourcesCompat
                    .getDrawable(getResources(), R.drawable.recycler_view_divider, null));

            recyclerView.addItemDecoration(divider);

            // Show the current hour on the recyclerView
            recyclerView.scrollToPosition(LocalTime.now().getHour());
        }
        return view;
    }
}