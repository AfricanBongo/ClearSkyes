package com.africanbongo.clearskyes.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.africanbongo.clearskyes.R;
import com.google.android.material.appbar.MaterialToolbar;

public class LocationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        MaterialToolbar locationsToolbar = findViewById(R.id.locations_toolbar);
        setSupportActionBar(locationsToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
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
            case R.id.add_new_location:
                Intent intent = new Intent(this, SearchableActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}