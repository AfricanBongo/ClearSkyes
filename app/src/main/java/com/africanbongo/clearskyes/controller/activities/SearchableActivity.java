package com.africanbongo.clearskyes.controller.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
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
import com.africanbongo.clearskyes.controller.animations.SwitchFadeAnimation;
import com.africanbongo.clearskyes.model.weather.WeatherLocation;
import com.africanbongo.clearskyes.model.weatherapi.ErrorPageListener;
import com.africanbongo.clearskyes.model.weatherapi.WeatherRequestQueue;
import com.africanbongo.clearskyes.util.WeatherLocationUtil;
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
public class SearchableActivity extends AppCompatActivity
        implements View.OnClickListener, SearchView.OnQueryTextListener, Reloadable {

    private static final String SEARCH_TEXT = "Search results for ";

    private View errorPage;
    private FrameLayout frameLayout;
    private RecyclerView recyclerView;
    private TextView searchResultsView;
    private SearchView searchView;
    private ImageButton backButton;

    private LoadingLayoutAnimation loadingLayoutAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        frameLayout = findViewById(R.id.new_frame);
        recyclerView = findViewById(R.id.new_location_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new SearchableRecyclerViewAdapter(this));

        RelativeLayout loadingLayout = findViewById(R.id.new_loading);
        loadingLayoutAnimation =
                new LoadingLayoutAnimation(loadingLayout, recyclerView);

        errorPage = findViewById(R.id.new_warning_layout);

        // When search View query is submitted
        searchView = findViewById(R.id.searchable_searchview);
        searchView.setOnQueryTextListener(this);

        // Close activity when back button is pressed
        backButton = findViewById(R.id.new_back_arrow);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (backButton != null) {
            backButton.callOnClick();
        }
    }

    // Load query result and display said results
    public void doSearch(String query) {
        ExecutorService service = Executors.newSingleThreadExecutor();

        loadingLayoutAnimation.start();

        service.submit(() -> {
            Response.Listener<JSONArray> locationListener = (JSONArray response) ->
                    service.submit(() -> {
                        try {
                            loadSearches(WeatherLocationUtil.parseIntoWeatherLocations(response), query);
                        } catch (JSONException e) {
                            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
                        }
                    });

            ErrorPageListener errorListener =
                    new ErrorPageListener(this, loadingLayoutAnimation);


            String requestURL = WeatherLocationUtil.SEARCH_URL + query;

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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.new_back_arrow) {
            NavUtils.navigateUpFromSameTask(this);
        }
    }

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
        // Do nothing
        return false;
    }

    @Override
    public View showError() {
        String query = searchView.getQuery().toString();
        String response = SEARCH_TEXT + "\"" + query + "\"" + " not found";
        searchResultsView.setText(response);
        // Switch the view pager with the error page
        SwitchFadeAnimation animation = new SwitchFadeAnimation();
        animation.switchViews(frameLayout, errorPage, SwitchFadeAnimation.NORMAL_DURATION);
        return errorPage;
    }

    @Override
    public void reload() {
        String query = searchView.getQuery().toString();
        onQueryTextSubmit(query);
        SwitchFadeAnimation animation = new SwitchFadeAnimation();
        animation.switchViews(errorPage, frameLayout, SwitchFadeAnimation.NORMAL_DURATION);
    }
}