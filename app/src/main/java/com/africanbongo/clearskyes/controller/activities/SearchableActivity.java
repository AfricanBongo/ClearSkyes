package com.africanbongo.clearskyes.controller.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.adapters.SearchableRecyclerViewAdapter;
import com.africanbongo.clearskyes.controller.animations.LoadingLayoutAnimation;
import com.africanbongo.clearskyes.model.weather.WeatherLocation;
import com.africanbongo.clearskyes.model.weatherapi.WeatherRequestQueue;
import com.africanbongo.clearskyes.model.weatherapi.util.LocationUtil;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

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
    private TextView searchResultsView;

    private LoadingLayoutAnimation loadingLayoutAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        recyclerView = findViewById(R.id.new_location_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new SearchableRecyclerViewAdapter(this));

        RelativeLayout loadingLayout = findViewById(R.id.new_loading);
        loadingLayoutAnimation =
                new LoadingLayoutAnimation(this, loadingLayout, recyclerView);


        // When search View query is submitted
        SearchView searchView = findViewById(R.id.searchable_searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchResultsText = SEARCH_TEXT +"\"" + query + "\":";

                searchResultsView = findViewById(R.id.search_results_text);
                searchResultsView.setText(searchResultsText);

                // Reveal frame layout
                findViewById(R.id.new_frame).setVisibility(View.VISIBLE);

                searchView.clearFocus();
                doSearch(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Close activity when back button is pressed
        ImageButton backButton = findViewById(R.id.new_back_arrow);
        backButton.setOnClickListener(e -> {
            NavUtils.navigateUpFromSameTask(this);
        });
    }

    // Load query result and display said results
    public void doSearch(String query) {
        ExecutorService service = Executors.newFixedThreadPool(2);

        loadingLayoutAnimation.start();

        service.submit(() -> {
            Response.Listener<JSONArray> locationListener = (JSONArray response) ->
                    service.submit(() -> {
                        try {
                            loadSearches(LocationUtil.parseIntoWeatherLocations(response), query);
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

    private void loadSearches(WeatherLocation[] locations, String query) {
        runOnUiThread(() -> {
            SearchableRecyclerViewAdapter recyclerViewAdapter =
                    (SearchableRecyclerViewAdapter) recyclerView.getAdapter();

            if (locations == null) {
                String response = SEARCH_TEXT + "\"" + query + "\"" + " not found";
                searchResultsView.setText(response);
                recyclerViewAdapter.setData(null);
            } else {
                recyclerViewAdapter.setData(locations);
            }

            loadingLayoutAnimation.stop();
        });
    }


}