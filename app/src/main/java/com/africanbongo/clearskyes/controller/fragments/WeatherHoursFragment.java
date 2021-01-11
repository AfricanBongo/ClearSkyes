package com.africanbongo.clearskyes.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherHour;


/**
 * A fragment representing a list of
 * {@link com.africanbongo.clearskyes.controller.fragments.WeatherHoursRecyclerViewAdapter.WeatherHourViewHolder} objects.
 */
public class WeatherHoursFragment extends Fragment {

    private final WeatherHour[] hours;

    public WeatherHoursFragment(WeatherHour[] hours) {
        this.hours = hours;
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

            divider.setDrawable(getResources().getDrawable(R.drawable.recycler_view_divider, null));

            recyclerView.addItemDecoration(divider);
        }
        return view;
    }
}