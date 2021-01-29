package com.africanbongo.clearskyes.controller.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.model.weather.WeatherLocation;
import com.africanbongo.clearskyes.model.weatherapi.util.LocationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Adapter used to share views containing info about the locations shown in the app.
 */
public class LocationsRecyclerViewAdapter extends RecyclerView.Adapter<LocationsRecyclerViewAdapter.LocationViewHolder> {

    private List<WeatherLocation> weatherLocations;
    private RecyclerView attachedRecyclerView;

    public LocationsRecyclerViewAdapter(List<WeatherLocation> locations, RecyclerView recyclerView) {
        weatherLocations = locations;
        attachedRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.location_view, parent, false);

        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        WeatherLocation location = weatherLocations.get(position);

        String city = location.getCity();
        String restText = location.getStandardStringLocation();
        holder.cityText.setText(city);
        holder.restText.setText(restText);

        // TODO Allow to delete location
    }

    @Override
    public int getItemCount() {
        return weatherLocations.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView cityText;
        private TextView restText;
        private ImageButton deleteButton;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            cityText = itemView.findViewById(R.id.location_city);
            restText = itemView.findViewById(R.id.location_rest);
            deleteButton = itemView.findViewById(R.id.loc_delete);
        }
    }
}
