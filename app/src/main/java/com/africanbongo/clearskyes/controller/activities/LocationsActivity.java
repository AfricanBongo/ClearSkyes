package com.africanbongo.clearskyes.controller.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.adapters.LocationsRecyclerViewAdapter;
import com.africanbongo.clearskyes.model.weather.WeatherLocation;
import com.africanbongo.clearskyes.model.weatherapi.util.LocationUtil;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LocationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        MaterialToolbar locationsToolbar = findViewById(R.id.locations_toolbar);
        setSupportActionBar(locationsToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        recyclerView = findViewById(R.id.locations_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        loadData(recyclerView);
    }


    public void loadData(RecyclerView recyclerView) {
        SharedPreferences locationPreferences = getSharedPreferences(LocationUtil.SP_LOCATIONS, MODE_PRIVATE);

        // Load locations into recyclerview
        if (locationPreferences != null) {
            Set<String> locations =
                    locationPreferences.getStringSet(LocationUtil.SP_LOCATION_SET, null);

            if (locations != null) {
                List<WeatherLocation> weatherLocations =
                        new ArrayList<>(LocationUtil.deserializeAll(locations));

                if (!weatherLocations.isEmpty()) {
                    recyclerView.setAdapter(new LocationsRecyclerViewAdapter(weatherLocations, recyclerView));
                    return;
                }
            }
        }

        // Otherwise show TextView to add location
        TextView addTextView = findViewById(R.id.loc_add_text);
        addTextView.setVisibility(View.VISIBLE);
    }

    public void openSearchableActivity(View view) {
        Intent intent = new Intent(this, SearchableActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.locations_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            // Allow the user to add a location to the app by entering through the search dialog
            case R.id.add_new_location: {
                openSearchableActivity(null);
                break;
            }

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}