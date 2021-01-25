package com.africanbongo.clearskyes.controller.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.model.weather.WeatherLocation;

public class SearchableRecyclerViewAdapter extends RecyclerView.Adapter<SearchableRecyclerViewAdapter.SearchableViewHolder>{

    private WeatherLocation[] weatherLocations;

    public void setData(WeatherLocation[] weatherLocations) {
        this.weatherLocations = weatherLocations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.new_location_view, parent, false);


        return new SearchableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchableViewHolder holder, int position) {
        if (weatherLocations != null) {
            WeatherLocation location = weatherLocations[position];
            holder.locationView.setText(location.getSimpleName());
        }

    }

    @Override
    public int getItemCount() {
        if (weatherLocations != null) {
            return weatherLocations.length;
        }

        return 0;
    }

    public class SearchableViewHolder extends RecyclerView.ViewHolder {
        private TextView locationView;

        public SearchableViewHolder(@NonNull View itemView) {
            super(itemView);
            this.locationView = itemView.findViewById(R.id.new_location_text);
        }
    }
}
