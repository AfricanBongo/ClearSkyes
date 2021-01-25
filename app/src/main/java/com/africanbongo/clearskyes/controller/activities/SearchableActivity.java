package com.africanbongo.clearskyes.controller.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.adapters.SearchableRecyclerViewAdapter;
import com.africanbongo.clearskyes.model.weather.WeatherLocation;
import com.africanbongo.clearskyes.model.weatherapi.WeatherRequestQueue;
import com.africanbongo.clearskyes.model.weatherapi.util.LocationUtil;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 *     A searchable activity used to find a location.
 * </p>
 * <p>
 *    Utilises the WeatherApi's Search/Autocomplete request
 * </p>
 *
 */
public class SearchableActivity extends AppCompatActivity {

    private static final String SEARCH_TEXT = "Search results for ";

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        MaterialToolbar toolbar = findViewById(R.id.searchable_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        Intent intent = getIntent();

        recyclerView = findViewById(R.id.new_location_recycler_view);
        recyclerView
                .setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new SearchableRecyclerViewAdapter());

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            String searchResultsText = SEARCH_TEXT +"\"" + query + "\":";

            TextView textView = findViewById(R.id.search_results_text);
            textView.setText(searchResultsText);

            doSearch(query);
        }

    }

    // Load query result and display said results
    public void doSearch(String query) {
        ExecutorService service = Executors.newFixedThreadPool(2);

        service.submit(() -> {
            Response.Listener<JSONArray> locationListener = (JSONArray response) ->
                    service.submit(() -> {
                        try {
                            loadSearches(LocationUtil.parseIntoWeatherLocations(response));
                        } catch (JSONException e) {
                            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
                        }
                    });

            Response.ErrorListener errorListener = (e ->
                    Log.e(this.getClass().getSimpleName(), e.getMessage(), e));


            String requestURL = LocationUtil.SEARCH_URL + query;

            JsonArrayRequest locationRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    requestURL,
                    null,
                    locationListener,
                    errorListener
            );

            WeatherRequestQueue
                    .getWeatherRequestQueue(getApplicationContext())
                    .addRequest(locationRequest);
        });
    }

    private void loadSearches(WeatherLocation[] locations) {
        runOnUiThread(() -> {
            if (locations == null) {
                // TODO
            } else {
                SearchableRecyclerViewAdapter recyclerViewAdapter = (SearchableRecyclerViewAdapter)
                        recyclerView.getAdapter();

                recyclerViewAdapter.setData(locations);
            }
        });
    }


}