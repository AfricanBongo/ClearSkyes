package com.africanbongo.clearskyes.controller.adapters;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.model.WeatherLocation;
import com.africanbongo.clearskyes.util.WeatherLocationUtil;

import java.util.LinkedHashSet;
import java.util.Set;

public class SearchableRecyclerViewAdapter extends RecyclerView.Adapter<SearchableRecyclerViewAdapter.SearchableViewHolder>{

    private final FragmentActivity activity;
    private WeatherLocation[] weatherLocations;

    public SearchableRecyclerViewAdapter(FragmentActivity activity) {
        this.activity = activity;
    }
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

            String locationRest = location.getStandardStringLocation();

            holder.locationCityView.setText(location.getCity());
            holder.locationRestView.setText(locationRest);
            holder.layout.setTag(location);


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
        private final ConstraintLayout layout;
        private final TextView locationCityView;
        private final TextView locationRestView;

        public SearchableViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = (ConstraintLayout) itemView;
            locationCityView = layout.findViewById(R.id.new_location_city);
            locationRestView = layout.findViewById(R.id.new_location_rest);

            // When the layout is clicked, save the location and close activity
            layout.setOnClickListener(e -> {
                WeatherLocation location = (WeatherLocation) layout.getTag();

                SharedPreferences preferences =
                        PreferenceManager.getDefaultSharedPreferences(itemView.getContext());

                Set<String> locationSet = preferences.getStringSet(WeatherLocationUtil.SP_LOCATION_SET, null);

                if (locationSet != null) {

                    String locationString = WeatherLocationUtil.serialize(location);
                    Set<String> newLocationSet = new LinkedHashSet<>(locationSet);
                    newLocationSet.add(locationString);

                    boolean written = preferences
                            .edit()
                            .putStringSet(WeatherLocationUtil.SP_LOCATION_SET, newLocationSet)
                            .commit();

                    // Close activity if the location is saved successfully
                    if (written) {
                        Toast.makeText(activity, WeatherLocationUtil.SAVED_SUCCESS, Toast.LENGTH_SHORT).show();
                        NavUtils.navigateUpFromSameTask(activity);
                    } else {
                        Toast.makeText(activity, WeatherLocationUtil.SAVED_FAILURE, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
