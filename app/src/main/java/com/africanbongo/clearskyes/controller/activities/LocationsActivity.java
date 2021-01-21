package com.africanbongo.clearskyes.controller.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
    }
}