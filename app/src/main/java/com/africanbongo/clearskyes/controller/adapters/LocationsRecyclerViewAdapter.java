package com.africanbongo.clearskyes.controller.adapters;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.model.WeatherLocation;
import com.africanbongo.clearskyes.util.WeatherLocationUtil;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Adapter used to share views containing info about the locations shown in the app.
 */
public class LocationsRecyclerViewAdapter extends RecyclerView.Adapter<LocationsRecyclerViewAdapter.LocationViewHolder> {

    private final List<LocationViewHolder> viewHolders;
    private final List<WeatherLocation> weatherLocations;
    private final RecyclerView attachedRecyclerView;

    public LocationsRecyclerViewAdapter(List<WeatherLocation> locations, RecyclerView recyclerView) {
        viewHolders = new ArrayList<>();
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
        viewHolders.add(holder);
        WeatherLocation location = weatherLocations.get(position);

        String city = location.getCity();
        String restText = location.getStandardStringLocation();
        holder.cityText.setText(city);
        holder.restText.setText(restText);

        checkForFavouriteLocation(holder, location);

        // Allow to delete location
        holder.deleteButton.setOnClickListener(e -> {
            String serializedLocation = WeatherLocationUtil.serialize(location);
            int adapterPosition = holder.getAdapterPosition();

            View parent = (View) attachedRecyclerView.getParent();

            // Create snackbar to show upon deletion
            String snackText = "Location: \"" + location.getShortStringLocation() +
                    "\" has been deleted";
            String actionText = "UNDO";
            Snackbar snackbar =
                    Snackbar.make(parent, snackText,
                            BaseTransientBottomBar.LENGTH_SHORT);

            // Delete location from app and shared preference
            weatherLocations.remove(position);
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(holder.rootLayout.getContext());
            Set<String> locations = preferences.getStringSet(WeatherLocationUtil.SP_LOCATION_SET, null);
            String preferredLocation = preferences.getString(WeatherLocationUtil.SP_FAV_LOCATION, null);
            locations.remove(serializedLocation);

            SharedPreferences.Editor editor = preferences.edit();

            if (preferredLocation != null) {
                // if it was a favourite location remove from SharedPreferences too
                if (location.equals(WeatherLocationUtil.deserialize(preferredLocation))) {
                    editor.remove(WeatherLocationUtil.SP_FAV_LOCATION);

                    // Discolor in the case it is added back into the app
                    colorHolder(holder, false);
                }
            }

            // Apply the changes
            editor
                    .putStringSet(WeatherLocationUtil.SP_LOCATION_SET, locations)
                    .apply();

            notifyItemRemoved(adapterPosition);

            // Restore the location is user presses undo action
            snackbar.setAction(actionText, l -> {
                weatherLocations.add(position, location);
                locations.add(serializedLocation);
                preferences
                        .edit()
                        .putStringSet(WeatherLocationUtil.SP_LOCATION_SET, locations)
                        .apply();

                notifyItemInserted(adapterPosition);
            }).show();
        });

    }

    // Colours the view holder and discolors previous view
    public void pickFavouriteLocation(LocationViewHolder holder, WeatherLocation location) {
        if (viewHolders != null) {
            // Set favourite location
            SharedPreferences preferences
                    = PreferenceManager.getDefaultSharedPreferences(attachedRecyclerView.getContext());
            preferences
                    .edit()
                    .putString(WeatherLocationUtil.SP_FAV_LOCATION, WeatherLocationUtil.serialize(location))
                    .apply();

            // Discolor any other toggled view
            for (LocationViewHolder holder1 : viewHolders) {
                if (!holder1.equals(holder)) {
                    colorHolder(holder1, false);
                    continue;
                }

                colorHolder(holder1, true);
            }

            String toastText = "\"" + location.getLongStringLocation() + "\" has been set as favourite location";
            Toast.makeText(holder.rootLayout.getContext(), toastText, Toast.LENGTH_SHORT).show();
        }
    }

    // Used to change the holder's properties if its location is the favorite location
    public void colorHolder(LocationViewHolder holder, boolean favourite) {
        String toolTipText;
        int resourceColor;

        if (favourite) {
            resourceColor = R.color.yellow_dark;
            toolTipText = "Favourite location";
        } else {
            resourceColor = R.color.white;
            toolTipText = null;
        }

        holder.rootLayout.setBackgroundResource(resourceColor);
        holder.rootLayout.setTooltipText(toolTipText);
        holder.favourite = favourite;
    }

    // Check if the holder's location is the favourite location
    public void checkForFavouriteLocation(LocationViewHolder holder, WeatherLocation holderLocation) {
        // Allow user to pick favourite location
        holder.rootLayout.setOnClickListener(e -> pickFavouriteLocation(holder, holderLocation));
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(attachedRecyclerView.getContext());

        String favouriteLocation =
                preferences.getString(WeatherLocationUtil.SP_FAV_LOCATION, null);

        if (favouriteLocation != null) {
            if (holderLocation.equals(WeatherLocationUtil.deserialize(favouriteLocation))) {
                colorHolder(holder, true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return weatherLocations.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout rootLayout;
        private final TextView cityText;
        private final TextView restText;
        private final ImageButton deleteButton;
        private boolean favourite = false;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            rootLayout = (ConstraintLayout) itemView;
            cityText = rootLayout.findViewById(R.id.location_city);
            restText = rootLayout.findViewById(R.id.location_rest);
            deleteButton = rootLayout.findViewById(R.id.loc_delete);
        }
    }
}
